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

package de.progme.god.rest.response;

/**
 * Created by Marvin Erkes on 30.10.2016.
 */
@SuppressWarnings("FieldCanBeLocal")
public class GodStatsResponse extends GodResponse {

    private int connections;

    private int connectionsPerSecond;

    private int onlineBackendServers;

    private long currentReadBytes;

    private long currentWrittenBytes;

    private long lastReadThroughput;

    private long lastWriteThroughput;

    private long totalReadBytes;

    private long totalWrittenBytes;

    public GodStatsResponse(Status status, String message, int connections, int connectionsPerSecond, int onlineBackendServers, long currentReadBytes, long currentWrittenBytes, long lastReadThroughput, long lastWriteThroughput, long totalReadBytes, long totalWrittenBytes) {

        super(status, message);

        this.connections = connections;
        this.connectionsPerSecond = connectionsPerSecond;
        this.onlineBackendServers = onlineBackendServers;
        this.currentReadBytes = currentReadBytes;
        this.currentWrittenBytes = currentWrittenBytes;
        this.lastReadThroughput = lastReadThroughput;
        this.lastWriteThroughput = lastWriteThroughput;
        this.totalReadBytes = totalReadBytes;
        this.totalWrittenBytes = totalWrittenBytes;
    }
}
