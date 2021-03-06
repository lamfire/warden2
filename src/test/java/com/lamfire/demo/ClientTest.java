package com.lamfire.demo;

import com.lamfire.utils.*;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-11-20
 * Time: 上午9:51
 * To change this template use File | Settings | File Templates.
 */
public class ClientTest {
    final static AtomicInteger counter = new AtomicInteger();

    public static String post() throws IOException {
        HttpClient client = new HttpClient();
        client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
        client.setMethod("POST");
        client.setCharset("UTF-8");
        client.open("http://127.0.0.1:8844/echo");
        //client.open("http://192.168.9.125:8080");


        client.addPostParameter("name","lamfire(小林子)");
        client.addPostParameter("age", ""+ RandomUtils.nextInt());
        client.addPostParameter("items",""+ RandomUtils.nextInt());
        client.addPostParameter("items",""+RandomUtils.nextInt());
        client.post();

        byte[] ret = client.read();
        //System.out.println("POST_RESULT["+ ret.length +"]:" + new String(ret));
        client.close();
        return new String(ret);
    }

    public static String get() throws IOException {
        HttpClient client = new HttpClient();
        client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
        client.setMethod("GET");
        client.setCharset("UTF-8");
        client.open("http://127.0.0.1:8844/echo?name="+ URLUtils.encode("lamfire(小林子)","utf-8")+"&age=18&items=23&items=24");

        byte[] ret = client.read();
        //System.out.println("POST_RESULT["+ ret.length +"]:" + new String(ret));
        return new String(ret);
    }



    public static void main(String[] args)throws IOException {
        System.out.println(get());
        System.out.println(post());
    }
}
