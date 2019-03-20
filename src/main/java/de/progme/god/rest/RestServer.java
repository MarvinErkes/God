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

import de.progme.hermes.server.HermesServer;
import de.progme.hermes.server.HermesServerFactory;
import de.progme.iris.IrisConfig;
import de.progme.iris.config.Header;
import de.progme.iris.config.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Marvin Erkes on 27.06.2016.
 */
public class RestServer {

    private static Logger logger = LoggerFactory.getLogger(RestServer.class);

    private IrisConfig irisConfig;

    private HermesServer hermesServer;

    public RestServer(IrisConfig irisConfig) {

        this.irisConfig = irisConfig;
    }

    public void start() {

        Header restHeader = irisConfig.getHeader("rest");
        Key serverKey = restHeader.getKey("server");

        String ip = serverKey.getValue(0).asString();
        int port = serverKey.getValue(1).asInt();

        hermesServer = HermesServerFactory.create(new GodRestConfig(ip, port));
        hermesServer.start();

        logger.info("RESTful API listening on {}:{}", ip, port);
    }

    public void stop() {

        hermesServer.stop();
    }
}
