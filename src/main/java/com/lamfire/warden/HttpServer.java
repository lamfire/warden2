package com.lamfire.warden;

import com.lamfire.logger.Logger;
import com.lamfire.utils.Threads;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-8-12
 * Time: 上午11:07
 * To change this template use File | Settings | File Templates.
 */
public class HttpServer {

    private static Logger log = Logger.getLogger(HttpServer.class);
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture bindFuture;

    private String bind = "0.0.0.0";
    private int port = 8080;
    private int workerThreads = 32;

    public HttpServer(int port){
        this.port = port;
    }

    public HttpServer(String bind,int port){
        this.bind = bind;
        this.port = port;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public synchronized void startup() {
        bossGroup = new NioEventLoopGroup(4, Threads.makeThreadFactory("boss"));
        workerGroup = new NioEventLoopGroup(workerThreads, Threads.makeThreadFactory("worker"));
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new HttpServerInboundHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 100)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            bindFuture = bootstrap.bind(bind,port).sync();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public synchronized void shutdown(){
        try {
            bindFuture.channel().closeFuture().sync();

        }catch (Exception e){

        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            bossGroup = null;
            workerGroup = null;
            bindFuture = null;
            bootstrap = null;
        }
    }
}
