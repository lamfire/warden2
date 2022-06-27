package com.lamfire.warden.cmd;

public class CmdREQ <T>{
    private String cmd;
    private T data;

    public CmdREQ(){

    }

    public CmdREQ(String cmd, T data) {
        this.cmd = cmd;
        this.data = data;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
