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

package de.progme.god.strategy.impl;

import de.progme.god.strategy.BalancingStrategy;
import de.progme.god.util.BackendInfo;

import java.util.List;
import java.util.Random;

/**
 * Created by Marvin Erkes on 26.06.2016.
 */
public class RandomBalancingStrategy extends BalancingStrategy {

    private Random random = new Random();

    public RandomBalancingStrategy(List<BackendInfo> backend) {

        super(backend);
    }

    @Override
    public synchronized BackendInfo selectBackend(String originHost, int originPort) {

        return (!backend.isEmpty()) ? backend.get(random.nextInt(backend.size())) : null;
    }

    @Override
    public void disconnectedFrom(BackendInfo backendInfo) {

    }

    @Override
    public void removeBackendStrategy(BackendInfo backendInfo) {

    }

    @Override
    public void addBackendStrategy(BackendInfo backendInfo) {

    }
}
