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

package de.progme.god;

import de.progme.god.tcp.GodSocket;
import de.progme.god.udp.GodDatagram;
import de.progme.god.util.Mode;
import de.progme.iris.IrisConfig;

/**
 * Created by Marvin Erkes on 05.11.2016.
 */
public final class GodFactory {

    private GodFactory() {
        // No instance
    }

    public static God create(Mode mode, IrisConfig irisConfig) {

        switch (mode) {
            case TCP:
                return new GodSocket(irisConfig);
            case UDP:
                return new GodDatagram(irisConfig);
            default:
                return new GodSocket(irisConfig);
        }
    }
}
