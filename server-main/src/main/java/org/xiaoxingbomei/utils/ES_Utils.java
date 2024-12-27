package org.xiaoxingbomei.utils;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.json.JsonData;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ES_Utils
{
    /**
     * es的检索key
     */
    public static String getKeyWord(String field) {
        return field + ".keyword";
    }

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    // ======================================== 索引操作 start ========================================

    // 创建索引
    public CreateIndexResponse createIndex(String indexName, TypeMapping mappings,IndexSettings settings) throws IOException
    {
        // 构建es请求
        CreateIndexRequest.Builder requestBuilder = new CreateIndexRequest.Builder().index(indexName);
        // 设置映射（如果提供）
        if(mappings!=null)
        {
            requestBuilder.mappings(mappings);
        }
        // 设置索引配置（如果提供）
        if(settings!=null)
        {
            requestBuilder.settings(settings);
        }
        //
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(requestBuilder.build());
        return createIndexResponse;
    }

    // 删除索引
    public DeleteIndexResponse deleteIndex(String indexName) throws IOException
    {
        // 构建es请求
        DeleteIndexRequest request = new DeleteIndexRequest.Builder()
                .index(indexName)
                .build();
        // 执行删除操作
        DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(request);
        return deleteIndexResponse;
    }

    // 检查索引是否存在
    public boolean indexExists(String indexName) throws IOException
    {
        GetIndexRequest request = new GetIndexRequest.Builder()
                .index(indexName)
                .build();
        try
        {
            elasticsearchClient.indices().get(request);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
    // ======================================== 索引操作 end ========================================


    // ======================================== 文档操作 start ========================================

    /**
     * 插入单条数据
     */
    public IndexResponse insertDocument(String indexName, String docId, Map<String, Object> document) throws IOException
    {
        IndexRequest request = new IndexRequest.Builder<Map<String, Object>>()
                .index(indexName)
                .id(docId)
                .document(document)
                .build();

        // 执行插入操作
        IndexResponse response = elasticsearchClient.index(request);
        return response;
    }

    /**
     * 查询单条数据
     */
    public Map<String, Object> getDocument(String indexName, String docId) throws IOException
    {
        GetRequest request = new GetRequest.Builder()
                .index(indexName)
                .id(docId)
                .build();

        // 执行查询操作
        GetResponse<Map> response = elasticsearchClient.get(request, Map.class);
        return response.source();
    }

    /**
     * 删除单条数据
     */
    public DeleteResponse deleteDocument(String indexName, String docId) throws IOException
    {
        DeleteRequest deleteRequest = new DeleteRequest.Builder()
                .index(indexName)
                .id(docId)
                .build();
        DeleteResponse deleteResponse = elasticsearchClient.delete(deleteRequest);
        return deleteResponse;
    }

    /**
     * 批量插入数据
     */
    public boolean bulkInsert(String indexName, Map<String, Map<String, Object>> documents) throws IOException
    {
        BulkRequest.Builder bulkRequest = new BulkRequest.Builder();

        for (Map.Entry<String, Map<String, Object>> entry : documents.entrySet())
        {
            bulkRequest.operations(op -> op.index(idx -> idx
                    .index(indexName)
                    .id(entry.getKey())
                    .document(entry.getValue())
            ));
        }

        BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest.build());
        return !bulkResponse.errors();
    }


    /**
     * 更新文档
     */
    public UpdateResponse<Map> updateDocument(String indexName, String docId, Map<String, Object> updatedData) throws IOException
    {
        UpdateRequest<Map, Map> request = new UpdateRequest.Builder<Map, Map>()
                .index(indexName)
                .id(docId)
                .doc(updatedData)
                .build();

        return elasticsearchClient.update(request, Map.class);
    }

    /**
     * 根据条件查询
     */
    public SearchResponse<Map> searchByQuery(String indexName, String queryText) throws IOException
    {
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(indexName)
                .query(q -> q
                        .match(MatchQuery.of(m -> m
                                .field("content")
                                .query(queryText)
                        ))
                )
                .build();

        // 执行查询
        return elasticsearchClient.search(searchRequest, Map.class);
    }

    /**
     * 批量插入或更新文档
     */
    public BulkResponse bulkInsertDocument(String indexName, Map<String,Map<String,Object>> docs) throws IOException
    {
        // 创建 bulk 请求
        BulkRequest.Builder br = new BulkRequest.Builder();

        // 遍历每个文档，构建插入或更新的操作
        for (Map.Entry<String, Map<String, Object>> entry : docs.entrySet())
        {
            String              docId = entry.getKey();   // id
            Map<String, Object> doc   = entry.getValue(); // 文档

            JsonData jsonData = JsonData.of(doc);
            // 创建index请求
            IndexRequest<JsonData> indexRequest = new IndexRequest.Builder<JsonData>()
                    .index(indexName)
                    .id(docId)
                    .document(jsonData)
                    .build();
            // 添加到Bulk请求中
            BulkOperation bulkOperation = new BulkOperation.Builder().index(op->op
                    .index(indexRequest.index())
                    .id(indexRequest.id())
                    .document(indexRequest.document())
            ).build();
            br.operations(bulkOperation);
        }
        // 批量执行请求
        return elasticsearchClient.bulk(br.build());
    }

    // ======================================== 文档操作 end ========================================

    // ======================================== 查询功能 start ========================================

    /**
     * match查询
     */
    public SearchResponse<Map> matchQuery(String indexName, String field, String value) throws IOException {
        SearchRequest request = new SearchRequest.Builder()
                .index(indexName)
                .query(q -> q.match(m -> m.field(field).query(value)))
                .build();

        return elasticsearchClient.search(request, Map.class);
    }

    /**
     * term查询
     */
    public SearchResponse<Map> termQuery(String indexName, String field, String value) throws IOException
    {
        // 构建term查询
        SearchRequest request = new SearchRequest.Builder()
                .index(indexName)
                .query(q -> q.term(t -> t.field(field).value(value)))
                .build();

        return elasticsearchClient.search(request, Map.class);
    }

    /**
     * bool查询
     */
    public SearchResponse<Map> boolQuery(String indexName, Map<String, String> mustMatch, Map<String, String> shouldMatch) throws IOException
    {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();

        if (mustMatch != null) {
            mustMatch.forEach((key, value) -> boolQuery.must(m -> m.match(t -> t.field(key).query(value))));
        }
        if (shouldMatch != null) {
            shouldMatch.forEach((key, value) -> boolQuery.should(m -> m.match(t -> t.field(key).query(value))));
        }

        SearchRequest request = new SearchRequest.Builder()
                .index(indexName)
                .query(q -> q.bool(boolQuery.build()))
                .build();

        return elasticsearchClient.search(request, Map.class);
    }

    /**
     * multi-match查询
     */
    public SearchResponse<Map> multiMatchQuery(String indexName, String value, List<String> fields) throws IOException
    {
        SearchRequest request = new SearchRequest.Builder()
                .index(indexName)
                .query(q -> q.multiMatch(mm -> mm.query(value).fields(fields)))
                .build();

        return elasticsearchClient.search(request, Map.class);
    }

    /**
     * 前缀查询
     */
    public SearchResponse<Map> prefixQuery(String indexName, String field, String prefix) throws IOException
    {
        SearchRequest request = new SearchRequest.Builder()
                .index(indexName)
                .query(q -> q.prefix(p -> p.field(field).value(prefix)))
                .build();

        return elasticsearchClient.search(request, Map.class);
    }

    /**
     * 分页与排序查询
     */
    public SearchResponse<Map> paginatedQuery(String indexName, Query query, int page, int size, String sortField, boolean ascending) throws IOException
    {
        SearchRequest request = new SearchRequest.Builder()
                .index(indexName)
                .query(query)
                .from((page - 1) * size)
                .size(size)
                .sort(s -> s.field(f -> f.field(sortField).order(ascending ? SortOrder.Asc : SortOrder.Desc)))
                .build();

        return elasticsearchClient.search(request, Map.class);
    }
    // ======================================== 查询功能 end ========================================

    // ==================== 聚合功能 ====================

    /**
     * terms聚合
     */
    public SearchResponse<Map> termsAggregation(String indexName, String field) throws IOException {
        Aggregation termsAgg = Aggregation.of(a -> a.terms(t -> t.field(field)));

        SearchRequest request = new SearchRequest.Builder()
                .index(indexName)
                .aggregations("terms_agg", termsAgg)
                .build();

        return elasticsearchClient.search(request, Map.class);
    }

    /**
     * range聚合
     */
//    public SearchResponse<Map> rangeAggregation(String indexName, String field, List<Double> ranges) throws IOException {
//        Aggregation rangeAgg = Aggregation.of(a -> a.range(r -> r.field(field).ranges(ranges.stream().map(
//                range -> new Aggregation.Range.Builder().to(range).build()
//        ).toList())));
//
//        SearchRequest request = new SearchRequest.Builder()
//                .index(indexName)
//                .aggregations("range_agg", rangeAgg)
//                .build();
//
//        return elasticsearchClient.search(request, Map.class);
//    }

    // ==================== 高级功能 ====================

    /**
     * 高亮查询
     */
    public SearchResponse<Map> highlightQuery(String indexName, String field, String value) throws IOException
    {
        SearchRequest request = new SearchRequest.Builder()
                .index(indexName)
                .query(q -> q.match(m -> m.field(field).query(value)))
                .highlight(h -> h.fields(field, hf -> hf))
                .build();

        return elasticsearchClient.search(request, Map.class);
    }

}
