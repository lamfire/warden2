package com.lamfire.warden;

import com.lamfire.logger.Logger;
import com.lamfire.utils.Threads;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.AttributeKey;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-8-12
 * Time: 上午11:07
 * To change this template use File | Settings | File Templates.
 */
public class HttpServer {
    private static final Logger LOGGER = Logger.getLogger(HttpServer.class);
    private final ActionRegistry registry = new ActionRegistry();
    private final WardenOptions options = new WardenOptions();
    private final ServerBootstrap bootstrap = new ServerBootstrap();;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture bindFuture;

    public HttpServer(int port){
        this.options.setPort(port);
    }

    public HttpServer(String bind,int port){
        this.options.setBind(bind);
        this.options.setPort(port);
    }

    public int getWorkerThreads() {
        return this.options.getWorkerThreads();
    }

    public void setWorkerThreads(int workerThreads) {
        this.options.setWorkerThreads(workerThreads);
    }

    public ActionRegistry getActionRegistry(){
        return registry;
    }

    public WardenOptions getOptions(){
        return options;
    }

    public void register(Class<? extends Action> actionClass) {
        registry.mapping(actionClass);
    }

    public void registerAll(String packageName) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        registry.mappingPackage(packageName);
    }

    public <T> void childOption(ChannelOption<T> childOption, T value) {
        this.bootstrap.childOption(childOption,value);
    }

    public <T> void childAttr(AttributeKey<T> childKey, T value) {
        this.bootstrap.childAttr(childKey,value);
    }

    public ServerBootstrap getBootstrap(){
        return this.bootstrap;
    }

    public synchronized void startup() {
        if(registry.isEmpty()){
            LOGGER.error("Not found actions,system shutdown now...");
            System.exit(-1);
        }
        bossGroup = new NioEventLoopGroup(4, Threads.makeThreadFactory("boss"));
        workerGroup = new NioEventLoopGroup(options.getWorkerThreads(), Threads.makeThreadFactory("worker"));
        try {
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new HttpServerInboundHandler(registry,options));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 100)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            bindFuture = bootstrap.bind(options.getBind(),options.getPort()).sync();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public synchronized void shutdown(){
        try {
            LOGGER.info("Shutdown listener channel...");
            bindFuture.channel().close().sync();
        }catch (Exception e){

        }

        try{
            LOGGER.info("Shutdown worker group...");
            workerGroup.shutdownGracefully();
            LOGGER.info("Shutdown boss group...");
            bossGroup.shutdownGracefully();
        }finally {
            bossGroup = null;
            workerGroup = null;
            bindFuture = null;
        }

    }
}
