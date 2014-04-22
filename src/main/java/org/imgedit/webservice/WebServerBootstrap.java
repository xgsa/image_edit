/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.imgedit.webservice;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


@Service
public class WebServerBootstrap {

    private static final Logger LOG = Logger.getLogger(WebServerBootstrap.class);

    @Value("${network.maxframesize}")
    private int MAX_FRAME_SIZE = 10*1024*1024;  // 10 Mb seems quite enough

    @Autowired
    private WebServerHandler webServerHandler;

    @Autowired
    private CliHandler cliHandler;


    @PostConstruct
    private void initialize() {
        if (cliHandler.isParsedSuccessfully()) {
            // Configure the server.
            ServerBootstrap bootstrap = new ServerBootstrap(
                    new NioServerSocketChannelFactory(
                            Executors.newCachedThreadPool(),
                            Executors.newCachedThreadPool())
            );

            // Set up the pipeline factory.
            bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
                public ChannelPipeline getPipeline() throws Exception {
                    return Channels.pipeline(
                            // NOTE: Add "new LoggingHandler(InternalLogLevel.ERROR)," for debug.
                            new HttpRequestDecoder(),
                            new HttpChunkAggregator(MAX_FRAME_SIZE),
                            new HttpResponseEncoder(),
                            new HttpContentCompressor(),
                            webServerHandler
                    );
                }
            });

            // Bind and start to accept incoming connections.
            LOG.info(String.format("Working in the '%s' directory, listening on the %s port...",
                    cliHandler.getBaseDirectory(), cliHandler.getPort()));
            bootstrap.bind(new InetSocketAddress(cliHandler.getPort()));
        }
    }
}
