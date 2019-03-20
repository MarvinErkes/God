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

package de.progme.god.tcp;

import de.progme.god.God;
import de.progme.god.tcp.pipeline.initialize.GodSocketChannelInitializer;
import de.progme.god.util.PipelineUtils;
import de.progme.iris.IrisConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Marvin Erkes on 26.06.2016.
 */
public class GodSocket extends God {

    private static Logger logger = LoggerFactory.getLogger(GodSocket.class);

    public GodSocket(IrisConfig irisConfig) {

        super(irisConfig);
    }

    @Override
    public Channel bootstrap(EventLoopGroup bossGroup, EventLoopGroup workerGroup, String ip, int port, int backlog, int readTimeout, int writeTimeout) throws Exception {

        logger.info("Bootstrapping socket server");

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(PipelineUtils.getServerChannel())
                .childHandler(new GodSocketChannelInitializer(readTimeout, writeTimeout))
                .childOption(ChannelOption.AUTO_READ, false);

        if (PipelineUtils.isEpoll()) {
            bootstrap.childOption(EpollChannelOption.EPOLL_MODE, EpollMode.LEVEL_TRIGGERED);

            logger.debug("Epoll mode is now level triggered");
        }

        return bootstrap
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, backlog)
                .bind(ip, port)
                .sync()
                .channel();
    }
}
