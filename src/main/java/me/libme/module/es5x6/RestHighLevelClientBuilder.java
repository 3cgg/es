package me.libme.module.es5x6;

import me.libme.kernel._c.util.JStringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by J on 2017/9/24.
 */
public class RestHighLevelClientBuilder {

    private ESConfig esConfig=new ESConfig();

    public RestHighLevelClientBuilder addAddress(String host,int port){
        esConfig.addAddress(host, port);
        return this;
    }

    public RestHighLevelClientBuilder addHeader(String name,String value){
        esConfig.addHeader(name,value);
        return this;
    }

    public RestHighLevelClientBuilder auth(String userName,String password){
        esConfig.setUserName(userName);
        esConfig.setPassword(password);
        return this;
    }

    public RestHighLevelClientBuilder esConfig(ESConfig esConfig){
        this.esConfig=esConfig;
        return this;
    }


    public RestHighLevelClient build(){
        List<HttpHost> httpHosts=new ArrayList<>();
        Map<String ,Integer> addresses=esConfig.getAddresses();
        for(Map.Entry<String ,Integer> address : addresses.entrySet()){
            httpHosts.add(new HttpHost(address.getKey(),address.getValue(),"http"));
        }
        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[]{}));

        List<Header> defaultHeaders=new ArrayList<>();
        Map<String,String> headers=esConfig.getHeaders();
        for(Map.Entry<String,String> header :headers.entrySet()){
            defaultHeaders.add(new BasicHeader(header.getKey(), header.getValue()));
        }
        builder.setDefaultHeaders(defaultHeaders.toArray(new Header[]{}));

        if(JStringUtils.isNotNullOrEmpty(esConfig.getUserName())){
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(esConfig.getUserName(),esConfig.getPassword()));
            builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                    httpClientBuilder.disableAuthCaching();
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
            });
        }

        RestClient restClient= builder.build();

        RestHighLevelClient restHighLevelClient=new RestHighLevelClient(restClient);
        return restHighLevelClient;
    }



}
