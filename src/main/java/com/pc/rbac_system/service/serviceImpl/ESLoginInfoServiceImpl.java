package com.pc.rbac_system.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.pc.rbac_system.mapper.LoginInfoMapper;
import com.pc.rbac_system.model.EsLoginInfo;
import com.pc.rbac_system.model.LoginInfo;
import com.pc.rbac_system.service.IESLoginInfoService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

@Service
public class ESLoginInfoServiceImpl implements IESLoginInfoService {
    @Autowired
    LoginInfoMapper loginInfoMapper;
    @Autowired
    @Qualifier("elasticsearchRestTemplate")
    RestHighLevelClient template;

    String esIndex = "login_index";

    @Override
    public boolean importAll() throws IOException {
        boolean index = ifCreateIndex();
        if (index){
            List<LoginInfo> jdbcList = loginInfoMapper.findAllLoginInfos();
            BulkRequest bulkRequest = new BulkRequest();
            jdbcList.stream().forEach(x->{
                bulkRequest.add(new IndexRequest("post").source(JSON.toJSONString(x),XContentType.JSON));
            });
            BulkResponse bulk = template.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(bulk.buildFailureMessage());
            return bulk.hasFailures();
        }
        return false;
    }


    //创建索引
    @Override
    public boolean ifCreateIndex() throws IOException {
        //查看索引是否存在，不存在创建
        if (!template.indices().exists(new GetIndexRequest(esIndex),RequestOptions.DEFAULT)){

            CreateIndexRequest createIndexRequest = new CreateIndexRequest(esIndex);
            XContentBuilder xContentBuilder = JsonXContent.contentBuilder();
            //设置数据格式
            xContentBuilder.startObject().startObject("properties")
                    .startObject("username").field("type","text").field("analyzer","ik_max_word").endObject()
                    .startObject("ip").field("type","text").endObject()
                    .startObject("loginTime").field("type","date").endObject()
                    .startObject("nickName").field("type","text").field("analyzer","ik_max_word").endObject()
                    .startObject("id").field("type","long").endObject()
                    .startObject("addr").field("type","text").endObject()
                    .startObject("loginInfoId").field("type","keyword").endObject()
                    .endObject()
                    .endObject();
            //设置分片 3个 ,每个分片 副本0个  因为es 只有一个节点， 所以副本为0 ，因为如果节点挂掉整个es就挂掉，副本无法晋升为分片。
            createIndexRequest.settings(Settings.builder().put("index.number_of_shards", 3)
                    .put("index.number_of_replicas", 0));
            createIndexRequest.mapping(xContentBuilder);
//            createIndexRequest.mapping("\"properties\": {\n" +
//                    "      \"username\" :{\n" +
//                    "        \"type\": \"text\",\n" +
//                    "        \"analyzer\": \"ik_max_word\"\n" +
//                    "      },\n" +
//                    "      \"ip\":{\n" +
//                    "        \"type\": \"text\"\n" +
//                    "      },\n" +
//                    "      \"loginTime\":{\n" +
//                    "        \"type\": \"date\"\n" +
//                    "      },\n" +
//                    "      \"userId\":{\n" +
//                    "        \"type\": \"long\"\n" +
//                    "      },\n" +
//                    "      \"addr\":{\n" +
//                    "        \"type\": \"text\"\n" +
//                    "      }\n" +
//                    "    }",XContentType.JSON);
            CreateIndexResponse createIndexResponse = template.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            return acknowledged;
        }

        return true;
    }

    @Override
    public void delete(String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(esIndex);
        deleteRequest.id(id);
        DeleteResponse delete = template.delete(deleteRequest,RequestOptions.DEFAULT);
        int status = delete.status().getStatus();
    }

    @Override
    public List<EsLoginInfo> search(String keyword, Integer pageNum, Integer pageSize) throws IOException {

        ifCreateIndex();

        SearchRequest searchRequest = new SearchRequest(esIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (!StringUtils.isEmpty(keyword)){
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            //查询关键字在 username和nickName中进行
            boolQueryBuilder.should(QueryBuilders.matchQuery("username",keyword))
                    .should(QueryBuilders.matchQuery("nickName",keyword));

            searchSourceBuilder.query(boolQueryBuilder);

            //设置高亮格式
            searchSourceBuilder.highlighter(
                    new HighlightBuilder().preTags("<span style='color:red'>").postTags("</span>").field("username").field("nickName"));

        }


        //设置分页
        searchSourceBuilder.from((pageNum-1)*pageSize);
        searchSourceBuilder.size(pageSize);

        searchRequest.source(searchSourceBuilder);

        SearchResponse search = template.search(searchRequest,RequestOptions.DEFAULT);

        SearchHits hits = search.getHits();
        List<EsLoginInfo> esLoginInfos = new ArrayList<>();

        hits.forEach(x->{
            EsLoginInfo info = JSON.parseObject(x.getSourceAsString(),EsLoginInfo.class);
            info.setLoginInfoId(x.getId());
            Map<String, HighlightField> highlightFields = x.getHighlightFields();
            HighlightField username = highlightFields.get("username");

            //设置高亮
            if (null != username){
                StringBuilder content = new StringBuilder();
                Text[] fragments = username.getFragments();
                for (Text t:fragments){
                    content.append(t.toString());
                }
                info.setUsername(content.toString());
            }
            HighlightField nickName = highlightFields.get("nickName");
            if (null != nickName){
                StringBuilder content = new StringBuilder();
                Text[] fragments = nickName.getFragments();
                for (Text t:fragments){
                    content.append(t.toString());
                }
                info.setNickName(content.toString());
            }
            esLoginInfos.add(info);
        });


        return esLoginInfos;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Page esLoginService(String keyword, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public SearchHits searchHit(String keyword, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public void putEsLogin(EsLoginInfo esLoginInfo) throws IOException {
        ifCreateIndex();
        IndexRequest indexRequest = new IndexRequest(esIndex);
        indexRequest.source(JSON.toJSONString(esLoginInfo),XContentType.JSON);
        IndexResponse index = template.index(indexRequest,RequestOptions.DEFAULT);
        int status = index.status().getStatus();
        System.out.println(status);
    }
}
