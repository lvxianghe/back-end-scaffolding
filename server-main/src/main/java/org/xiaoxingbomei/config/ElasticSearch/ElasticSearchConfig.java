package org.xiaoxingbomei.config.ElasticSearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elasticsearch 配置
 */
@Configuration
public class ElasticSearchConfig
{

    @Value("${apollo.elasticsearch.hostname}")
    private String hostName;

    @Value("${apollo.elasticsearch.port}")
    private String port;

    @Value("${apollo.elasticsearch.scheme}")
    private String scheme; // http 或 https

    private static final String SCHEME = "http";    // 替换为
    private static final String USERNAME = "";      // 如果开启认证，填入用户名
    private static final String PASSWORD = "";      // 如果开启认证，填入密码

    /**
     * 配置 RestClient，用于连接 Elasticsearch 数据
     */
     // @Bean
     // public RestClient restClient()
     // {
     //     RestClientBuilder builder = RestClient.builder(new HttpHost(hostName, Integer.parseInt(port), scheme));
     //     return builder.build();
     // }

    // @Bean
        // public RestHighLevelClient restHighLevelClient()
    // {
    //     RestClientBuilder builder = RestClient.builder(new HttpHost(hostName, Integer.parseInt(port), scheme));
    //     return new RestHighLevelClient(builder);
    // }
    @Bean
    public ElasticsearchClient elasticsearchClient()
    {
        // 配置凭证（如果 Elasticsearch 开启了安全认证）
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        if (!USERNAME.isEmpty() && !PASSWORD.isEmpty())
        {
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(USERNAME, PASSWORD));
        }

        // 配置 RestClient
        RestClientBuilder builder = RestClient.builder(new HttpHost(hostName, Integer.parseInt(port), scheme))
                .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        // 创建 RestClientTransport
        RestClient restClient = builder.build();
        RestClientTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );

        // 返回 ElasticsearchClient
        return new ElasticsearchClient(transport);
    }

}
