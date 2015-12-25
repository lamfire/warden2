package com.lamfire.warden;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-12-25
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class WardenOptions {
    private String bind = "0.0.0.0";
    private int port = 8080;
    private int workerThreads = 32;
    private String defaultResponseContentType = "text/plain";

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public String getDefaultResponseContentType() {
        return defaultResponseContentType;
    }

    public void setDefaultResponseContentType(String defaultResponseContentType) {
        this.defaultResponseContentType = defaultResponseContentType;
    }
}
