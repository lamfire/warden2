package com.lamfire.warden;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-8-12
 * Time: 下午2:03
 * To change this template use File | Settings | File Templates.
 */
class ResponseOutputStream extends OutputStream implements DataOutput {
    private ByteBuf buffer;

    ResponseOutputStream(ByteBuf buffer){
        this.buffer = buffer;
    }

    ResponseOutputStream(){
        this.buffer = Unpooled.buffer();
    }

    @Override
    public void write(int b) throws IOException {
        buffer.writeByte(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        buffer.writeBytes(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        buffer.writeBytes(b,off,len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        buffer.writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        buffer.writeByte(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        buffer.writeShort(v);
    }

    @Override
    public void writeChar(int v) throws IOException {
        buffer.writeChar(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        buffer.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        buffer.writeLong(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        buffer.writeFloat(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        buffer.writeDouble(v);
    }

    @Override
    public void writeBytes(String s) throws IOException {
        buffer.writeBytes(s.getBytes());
    }

    @Override
    public void writeChars(String s) throws IOException {
        buffer.writeBytes(s.getBytes());
    }

    @Override
    public void writeUTF(String s) throws IOException {
        buffer.writeBytes(s.getBytes());
    }

    public ByteBuf asByteBuf(){
        int length = buffer.readableBytes();
        buffer.capacity(length);
        return buffer;
    }
}
