/*
 * Copyright (c) 2016 "Marvin Erkes"
 *
 * This file is part of God.
 *
 * God is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.progme.god.task;

import com.google.common.collect.Lists;
import de.progme.god.God;
import de.progme.god.strategy.BalancingStrategy;
import de.progme.god.util.BackendInfo;

import java.util.List;

/**
 * Created by Marvin Erkes on 06.11.2016.
 */
public abstract class CheckBackendTask implements Runnable {

    protected final List<BackendInfo> backendInfo;

    protected BalancingStrategy balancingStrategy;

    public CheckBackendTask(BalancingStrategy balancingStrategy) {

        this.balancingStrategy = balancingStrategy;
        this.backendInfo = Lists.newArrayList(balancingStrategy.getBackend());
    }

    public abstract void check();

    public synchronized void addBackend(BackendInfo backendInfo) {

        this.backendInfo.add(backendInfo);
    }

    public synchronized void removeBackend(BackendInfo backendInfo) {

        this.backendInfo.remove(backendInfo);
    }

    @Override
    public void run() {

        if (!God.getServerChannel().isActive()) {
            return;
        }

        check();
    }
}
