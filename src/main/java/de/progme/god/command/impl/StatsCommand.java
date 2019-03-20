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

package de.progme.god.command.impl;

import de.progme.god.God;
import de.progme.god.command.Command;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Marvin Erkes on 31.10.2016.
 */
public class StatsCommand extends Command {

    private static Logger logger = LoggerFactory.getLogger(StatsCommand.class);

    public StatsCommand(String name, String description, String... aliases) {

        super(name, description, aliases);
    }

    @Override
    public boolean execute(String[] args) {

        logger.info("Connections: {}", God.getChannelGroup().size());
        if (God.getInstance().getConnectionsPerSecondTask() != null) {
            logger.info("Connections per second: {}", God.getInstance().getConnectionsPerSecondTask().getPerSecond());
        }
        logger.info("Online backend servers: {}", God.getBalancingStrategy().getBackend().size());

        GlobalTrafficShapingHandler trafficShapingHandler = God.getInstance().getTrafficShapingHandler();
        if (trafficShapingHandler != null) {
            TrafficCounter trafficCounter = trafficShapingHandler.trafficCounter();

            logger.info("Current bytes read: {}", trafficCounter.currentReadBytes());
            logger.info("Current bytes written: {}", trafficCounter.currentWrittenBytes());
            logger.info("Last read throughput: {}", trafficCounter.lastReadThroughput());
            logger.info("Last write throughput: {}", trafficCounter.lastWrittenBytes());
            logger.info("Total bytes read: {}", trafficCounter.cumulativeReadBytes());
            logger.info("Total bytes written: {}", trafficCounter.cumulativeWrittenBytes());
        }

        return true;
    }
}
