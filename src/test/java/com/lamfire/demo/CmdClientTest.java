package com.lamfire.demo;

import com.lamfire.demo.utils.MyDataCodec;
import com.lamfire.json.JSON;
import com.lamfire.utils.HttpClient;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.URLUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-11-20
 * Time: 上午9:51
 * To change this template use File | Settings | File Templates.
 */
public class CmdClientTest {
    final static AtomicInteger counter = new AtomicInteger();
    final static MyDataCodec codec = new MyDataCodec();
    public static void register()throws IOException {
        HttpClient client = new HttpClient();
        client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
        client.setMethod("POST");
        client.setCharset("UTF-8");
        client.open("http://127.0.0.1:8844/api");
        JSON json = new JSON();
        json.put("cmd","register");
        json.put("account","admin");
        json.put("password","password");
        client.post(codec.encode(null,json.toBytes()));
        byte[] ret = codec.decode(null,client.read());
        JSON resp = JSON.fromBytes(ret);
        System.out.println(resp);
    }

    public static void login()throws IOException {
        HttpClient client = new HttpClient();
        client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
        client.setMethod("POST");
        client.setCharset("UTF-8");
        client.open("http://127.0.0.1:8844/api");
        JSON json = new JSON();
        json.put("cmd","login");
        json.put("account","admin");
        json.put("password","password");

        client.post(codec.encode(null,json.toBytes()));
        byte[] ret = codec.decode(null,client.read());

        JSON resp = JSON.fromBytes(ret);
        System.out.println(resp);
    }

    public static void add()throws IOException {
        HttpClient client = new HttpClient();
        client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
        client.setMethod("POST");
        client.setCharset("UTF-8");
        client.open("http://127.0.0.1:8844/api");
        JSON json = new JSON();
        json.put("cmd","add");
        json.put("add",100);

        client.post(codec.encode(null,json.toBytes()));
        byte[] ret = codec.decode(null,client.read());

        JSON resp = JSON.fromBytes(ret);
        System.out.println(resp);
    }

    public static void del()throws IOException {
        HttpClient client = new HttpClient();
        client.setContentType(HttpClient.ContentType.application_x_www_form_urlencoded);
        client.setMethod("POST");
        client.setCharset("UTF-8");
        client.open("http://127.0.0.1:8844/api");
        JSON json = new JSON();
        json.put("cmd","del");
        json.put("add",100);

        client.post(codec.encode(null,json.toBytes()));
        byte[] ret = codec.decode(null,client.read());

        JSON resp = JSON.fromBytes(ret);
        System.out.println(resp);
    }


    public static void main(String[] args)throws IOException {
        register();
        login();
        add();
        del();
    }
}
