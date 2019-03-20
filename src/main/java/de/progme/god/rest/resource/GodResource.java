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

package de.progme.god.rest.resource;

import com.google.gson.Gson;
import de.progme.god.God;
import de.progme.god.rest.response.GodListResponse;
import de.progme.god.rest.response.GodResponse;
import de.progme.god.rest.response.GodStatsResponse;
import de.progme.god.strategy.BalancingStrategy;
import de.progme.god.task.ConnectionsPerSecondTask;
import de.progme.god.util.BackendInfo;
import de.progme.hermes.server.http.Request;
import de.progme.hermes.server.http.annotation.Path;
import de.progme.hermes.server.http.annotation.PathParam;
import de.progme.hermes.server.http.annotation.Produces;
import de.progme.hermes.server.http.annotation.method.GET;
import de.progme.hermes.shared.ContentType;
import de.progme.hermes.shared.http.Response;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Marvin Erkes on 27.06.2016.
 */
@Path("/god")
public class GodResource {

    private static final Response STATS_DISABLED;

    private static Logger logger = LoggerFactory.getLogger(GodResource.class);

    private static Gson gson = new Gson();

    private static GlobalTrafficShapingHandler trafficShapingHandler = God.getInstance().getTrafficShapingHandler();

    private static ConnectionsPerSecondTask connectionsPerSecondTask = God.getInstance().getConnectionsPerSecondTask();

    static {
        STATS_DISABLED = Response.ok().content(gson.toJson(new GodStatsResponse(GodResponse.Status.ERROR,
                "Stats are disabled",
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1))).build();
    }

    @GET
    @Path("/add/{name}/{ip}/{port}")
    @Produces(ContentType.APPLICATION_JSON)
    public Response add(Request httpRequest, @PathParam String name, @PathParam String ip, @PathParam String port) {

        BackendInfo found = null;
        synchronized (God.getBalancingStrategy().getBackend()) {
            for (BackendInfo info : God.getBalancingStrategy().getBackend()) {
                if (info.getName().equalsIgnoreCase(name)) {
                    found = info;
                    break;
                }
            }
        }

        if (found == null) {
            BackendInfo backend = new BackendInfo(name, ip, Integer.valueOf(port));
            God.getBalancingStrategy().addBackend(backend);
            God.getBackendTask().addBackend(backend);

            logger.info("Added backend server {}:{} to the load balancer", ip, port);

            return Response.ok().content(gson.toJson(new GodResponse(GodResponse.Status.OK,
                    "Successfully added server"))).build();
        } else {
            return Response.ok().content(gson.toJson(new GodResponse(GodResponse.Status.SERVER_ALREADY_ADDED,
                    "Server was already added"))).build();
        }
    }

    @GET
    @Path("/remove/{name}")
    @Produces(ContentType.APPLICATION_JSON)
    public Response remove(Request httpRequest, @PathParam String name) {

        BackendInfo found = null;
        synchronized (God.getBalancingStrategy().getBackend()) {
            for (BackendInfo info : God.getBalancingStrategy().getBackend()) {
                if (info.getName().equalsIgnoreCase(name)) {
                    found = info;
                    break;
                }
            }
        }

        if (found != null) {
            God.getBalancingStrategy().removeBackend(found);
            God.getBackendTask().removeBackend(found);

            logger.info("Removed backend server {} from the load balancer", name);

            return Response.ok().content(gson.toJson(new GodResponse(GodResponse.Status.OK,
                    "Successfully removed server"))).build();
        } else {
            return Response.ok().content(gson.toJson(new GodResponse(GodResponse.Status.SERVER_NOT_FOUND,
                    "Server not found"))).build();
        }
    }

    @GET
    @Path("/list")
    @Produces(ContentType.APPLICATION_JSON)
    public Response list(Request httpRequest) {

        BalancingStrategy balancingStrategy = God.getBalancingStrategy();
        if (balancingStrategy != null) {
            return Response.ok().content(gson.toJson(new GodListResponse(GodResponse.Status.OK, "List received",
                    balancingStrategy.getBackend()))).build();
        } else {
            return Response.ok().content(gson.toJson(new GodListResponse(GodResponse.Status.ERROR,
                    "Unable to get the balancing strategy",
                    null))).build();
        }
    }

    @GET
    @Path("/stats")
    @Produces(ContentType.APPLICATION_JSON)
    public Response stats(Request httpRequest) {

        if (trafficShapingHandler != null) {
            TrafficCounter trafficCounter = trafficShapingHandler.trafficCounter();

            return Response.ok().content(gson.toJson(new GodStatsResponse(GodResponse.Status.OK,
                    "OK",
                    God.getChannelGroup().size(),
                    connectionsPerSecondTask.getPerSecond(),
                    God.getBalancingStrategy().getBackend().size(),
                    trafficCounter.currentReadBytes(),
                    trafficCounter.currentWrittenBytes(),
                    trafficCounter.lastReadThroughput(),
                    trafficCounter.lastWriteThroughput(),
                    trafficCounter.cumulativeReadBytes(),
                    trafficCounter.cumulativeWrittenBytes()))).build();
        } else {
            return STATS_DISABLED;
        }
    }
}
