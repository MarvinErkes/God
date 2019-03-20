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

package de.progme.god.udp.pipeline;

import de.progme.god.util.ChannelUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Marvin Erkes on 04.11.2016.
 */
public class DatagramDownstreamHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Logger logger = LoggerFactory.getLogger(DatagramDownstreamHandler.class);

    private Channel channel;

    private InetSocketAddress sender;

    public DatagramDownstreamHandler(Channel channel, InetSocketAddress sender) {

        this.channel = channel;
        this.sender = sender;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket datagramPacket) throws Exception {

        if (channel.isActive()) {
            channel.writeAndFlush(new DatagramPacket(datagramPacket.content().retain(), sender));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ChannelUtil.close(ctx.channel());

        if (!(cause instanceof IOException)) {
            logger.error(cause.getMessage(), cause);
        }
    }
}
