package me.libme.module.es5x6;

import me.libme.kernel._c._m.JModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by J on 2017/9/24.
 */
public class ESConfig implements JModel {

    private Map<String,Integer> addresses=new HashMap<>();

    private Map<String,String> headers=new HashMap<>();

    private long timeout;

    private String userName;

    private String password;

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ESConfig addAddress(String host, int port){
        addresses.put(host,port);
        return this;
    }

    public ESConfig addHeader(String name,String value){
        headers.put(name, value);
        return this;
    }


    public Map<String, Integer> getAddresses() {
        return addresses;
    }

    public void setAddresses(Map<String, Integer> addresses) {
        this.addresses = addresses;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
