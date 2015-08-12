package com.lamfire.demo;

import com.lamfire.warden.ActionRegistry;
import com.lamfire.warden.HttpServer;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-8-12
 * Time: 下午2:39
 * To change this template use File | Settings | File Templates.
 */
public class ServerMain {
    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer(8844);
        server.registerAll(ServerMain.class.getPackage().getName());
        server.startup();

        System.out.println("Http Server listening on 8844 ...");

        //server.shutdown();
    }
}
