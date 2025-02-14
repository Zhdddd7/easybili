package com.easybili.entities.component;


import co.elastic.clients.elasticsearch.xpack.XpackInfoRequest;
import com.easybili.entities.config.AppConfig;
import com.easybili.entities.dto.VideoInfoEsDto;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.enums.SearchOrderTypeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.UserInfo;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.query.UserInfoQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.SimplePage;
import com.easybili.mappers.UserInfoMapper;
import com.easybili.utils.JsonUtils;
import com.easybili.utils.StringTools;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("esSearchComponent")
@Slf4j
public class EsSearchComponent {
    @Resource
    private AppConfig appConfig;
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private UserInfoMapper userInfoMapper;

    private Boolean isExistIndex() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(appConfig.getEsIndexVideoName());
        return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    public void createIndex(){
        try{
            if(isExistIndex()){
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(appConfig.getEsIndexVideoName());
            request.settings("{\n" +
                    "  \"analysis\": {\n" +
                    "    \"analyzer\": {\n" +
                    "      \"comma\": {\n" +
                    "        \"type\": \"pattern\",\n" +
                    "        \"pattern\": \",\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }\n" +
                    "}", XContentType.JSON);
            request.mapping("{\n" +
                    "  \"properties\": {\n" +
                    "    \"videoId\": {\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"index\": false\n" +
                    "    },\n" +
                    "    \"userId\": {\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"index\": false\n" +
                    "    },\n" +
                    "    \"videoCover\": {\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"index\": false\n" +
                    "    },\n" +
                    "    \"videoName\": {\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"analyzer\": \"ik_max_word\"\n" +
                    "    },\n" +
                    "    \"tags\": {\n" +
                    "      \"type\": \"text\",\n" +
                    "      \"analyzer\": \"comma\"\n" +
                    "    },\n" +
                    "    \"playCount\": {\n" +
                    "      \"type\": \"integer\",\n" +
                    "      \"index\": false\n" +
                    "    },\n" +
                    "    \"danmuCount\": {\n" +
                    "      \"type\": \"integer\",\n" +
                    "      \"index\": false\n" +
                    "    },\n" +
                    "    \"collectCount\": {\n" +
                    "      \"type\": \"integer\",\n" +
                    "      \"index\": false\n" +
                    "    },\n" +
                    "    \"createTime\": {\n" +
                    "      \"type\": \"date\",\n" +
                    "      \"index\": false\n" +
                    "    }\n" +
                    "  }\n" +
                    "}", XContentType.JSON);
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            Boolean acknowledged =  createIndexResponse.isAcknowledged();

            if(!acknowledged){
                throw new BusinessException("ES init failed");
            }
        }catch(Exception e){
            log.error("ES init failed", e);
            throw new BusinessException("ES init failed");
        }
    }

     public void saveDoc(VideoInfo videoInfo){
        try {

            if(docExist(videoInfo.getVideoId())){
                updateDoc(videoInfo);
            }else{
                VideoInfoEsDto videoInfoEsDto = new VideoInfoEsDto();
                BeanUtils.copyProperties(videoInfo, videoInfoEsDto);
                videoInfoEsDto.setCollectCount(0);
                videoInfoEsDto.setPlayCount(0);
                videoInfoEsDto.setDanmuCount(0);
                IndexRequest request = new IndexRequest(appConfig.getEsIndexVideoName());
                request.id(videoInfo.getVideoId()).source(JsonUtils.convertObj2Json(videoInfoEsDto), XContentType.JSON);
                restHighLevelClient.index(request, RequestOptions.DEFAULT);
            }
        }catch(Exception e){
            log.error("error to save to es", e);
            throw new BusinessException("save to es failed");
        }
     }

     private Boolean docExist(String id){
        try{
            GetRequest getRequest = new GetRequest(appConfig.getEsIndexVideoName(), id);
            GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            return response.isExists();
        }catch (Exception e){
            log.error("a network exception happens");
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST.getMessage());
        }

     }

     private void updateDoc(VideoInfo videoInfo){
        try {
            videoInfo.setLastUpdateTime(null);
            videoInfo.setCreateTime(null);

            Map<String, Object> dataMap = new HashMap<>();
            Field[] fields = videoInfo.getClass().getDeclaredFields();
            for(Field field: fields){
                String methodName = "get" + StringTools.upperCaseFirstLetter(field.getName());
                Method method = videoInfo.getClass().getMethod(methodName);
                Object object = method.invoke(videoInfo);
                if(object instanceof String && !StringTools.isEmpty(object.toString()) || object != null && !(object instanceof String)){
                    dataMap.put(field.getName(), object);
                }
            }
            if(dataMap.isEmpty()){
                return ;
            }
            UpdateRequest updateRequest = new UpdateRequest(appConfig.getEsIndexVideoName(), videoInfo.getVideoId());
            updateRequest.doc(dataMap);
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            }catch (Exception e) {
                log.error("video save failed", e);
                throw new BusinessException("video save failed");
            }
    }

    public void updateDocCount(String videoId, String fieldName, Integer count){
        try {
            UpdateRequest updateRequest = new UpdateRequest(appConfig.getEsIndexVideoName(), videoId);
            Script script = new Script(ScriptType.INLINE, "painless", "ctx._source." + fieldName + " += params.count", Collections.singletonMap("count", count));
            updateRequest.script(script);
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        }catch (Exception e) {
            log.error("save to es failed", e);
            throw new BusinessException("save to es failed");
        }
    }

    // TODO delete video later
    public void delDoc(String videoId){
        DeleteRequest deleteRequest = new DeleteRequest(appConfig.getEsIndexVideoName(), videoId);
        try{
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        }
        catch (Exception e){
            log.error("delete es failed", e);
            throw new BusinessException("delete es failed");
        }
    }

    public PaginationResultVO<VideoInfo> search(Boolean highlight, String keyword, Integer orderType, Integer pageNo, Integer pageSize){
        try {
            SearchOrderTypeEnum searchOrderTypeEnum = SearchOrderTypeEnum.getByType(orderType);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, "videoName", "tags"));
            if(highlight){
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                highlightBuilder.field("videoName");
                highlightBuilder.preTags("<span class='highlight'>");
                highlightBuilder.postTags("</span>");
                searchSourceBuilder.highlighter(highlightBuilder);
            }
            // ranking
            searchSourceBuilder.sort("_score", SortOrder.ASC);
            if(orderType != null){
                searchSourceBuilder.sort(searchOrderTypeEnum.getField(), SortOrder.ASC);
            }
            pageNo = pageNo == null?1:pageNo;
            pageSize = pageSize == null?20:pageSize;
            searchSourceBuilder.size(pageSize);
            searchSourceBuilder.from((pageNo - 1) * pageSize);

            SearchRequest searchRequest = new SearchRequest(appConfig.getEsIndexVideoName());
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();
            Integer totalCount = (int)hits.getTotalHits().value;

            List<VideoInfo> videoInfoList = new ArrayList<>();
            List<String> userIdList = new ArrayList<>();

            for(SearchHit hit: hits.getHits()){
                VideoInfo videoInfo = JsonUtils.convertJson2Obj(hit.getSourceAsString(), VideoInfo.class);
                if(hit.getHighlightFields().get("videoName")!= null){
                    videoInfo.setVideoName(hit.getHighlightFields().get("videoName").fragments()[0].string());
                }
                videoInfoList.add(videoInfo);
                userIdList.add(videoInfo.getUserId());
            }

            UserInfoQuery userInfoQuery = new UserInfoQuery();
            userInfoQuery.setUserIdList(userIdList);
            List<UserInfo> userInfoList = userInfoMapper.selectList(userInfoQuery);
            Map<String, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(item ->item.getUserId(), Function.identity(),(data1, data2) ->data2 ));

            videoInfoList.forEach(item ->
                    {
                        item.setUserName(userInfoMap.get(item.getUserId()).getUserName());
                    }
            );

            return new PaginationResultVO<>(totalCount, pageSize, pageNo, videoInfoList);
        }catch (Exception e){
            log.error("search es failed", e);
            throw new BusinessException("search es failed");
        }
    }

    public List<VideoInfo> recommend(String keyword, String videoId, Integer pageNo, Integer pageSize){
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, "videoName", "tags"));
            // ranking
            searchSourceBuilder.sort("_score", SortOrder.ASC);
            pageNo = pageNo == null?1:pageNo;
            pageSize = pageSize == null?20:pageSize;
            searchSourceBuilder.size(pageSize);
            searchSourceBuilder.from((pageNo - 1) * pageSize);

            SearchRequest searchRequest = new SearchRequest(appConfig.getEsIndexVideoName());
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();
            Integer totalCount = (int)hits.getTotalHits().value;

            List<VideoInfo> videoInfoList = new ArrayList<>();
            List<String> userIdList = new ArrayList<>();

            for(SearchHit hit: hits.getHits()){
                VideoInfo videoInfo = JsonUtils.convertJson2Obj(hit.getSourceAsString(), VideoInfo.class);
                if(!videoInfo.getVideoId().equals(videoId)){
                    videoInfoList.add(videoInfo);
                    userIdList.add(videoInfo.getUserId());
                }
            }

            UserInfoQuery userInfoQuery = new UserInfoQuery();
            userInfoQuery.setUserIdList(userIdList);
            List<UserInfo> userInfoList = userInfoMapper.selectList(userInfoQuery);
            Map<String, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(item ->item.getUserId(), Function.identity(),(data1, data2) ->data2 ));

            videoInfoList.forEach(item ->
                    {
                        item.setUserName(userInfoMap.get(item.getUserId()).getUserName());
                    }
            );
            return videoInfoList;

//            return new PaginationResultVO<>(totalCount, pageSize, pageNo, videoInfoList);
        }catch (Exception e){
            log.error("search es failed", e);
            throw new BusinessException("search es failed");
        }
    }





}
