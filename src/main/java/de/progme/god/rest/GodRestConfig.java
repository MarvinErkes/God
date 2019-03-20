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

package de.progme.god.rest;

import de.progme.god.rest.resource.GodResource;
import de.progme.hermes.server.impl.HermesConfig;

/**
 * Created by Marvin Erkes on 27.06.2016.
 */
public class GodRestConfig extends HermesConfig {

    public GodRestConfig(String host, int port) {

        host(host);
        port(port);
        corePoolSize(2);
        maxPoolSize(4);
        backLog(50);
        register(GodResource.class);
    }
}
