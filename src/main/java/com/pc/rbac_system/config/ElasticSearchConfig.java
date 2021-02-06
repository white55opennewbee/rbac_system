package com.pc.rbac_system.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
    @Value("${esHost}")
    private String esHost;
    @Value("${esPort}")
    private Integer esport;
    @Bean
    public RestHighLevelClient elasticsearchRestTemplate(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(esHost, esport, "http")));
        return client;

    }
}
