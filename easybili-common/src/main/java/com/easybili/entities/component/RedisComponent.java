package com.easybili.entities.component;

import com.easybili.constants.Constants;
import com.easybili.entities.config.AppConfig;
import com.easybili.entities.dto.*;
import com.easybili.entities.po.CategoryInfo;
import com.easybili.entities.po.VideoInfoFilePost;
import com.easybili.redis.RedisUtils;
import com.easybili.utils.DateUtils;
import com.easybili.utils.StringTools;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private AppConfig appConfig;

    public String saveCheckCode(String code){
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_PREFIX + checkCodeKey, code, Constants.REDIS_KEY_EXPIRES_ONE_MIN * 10);
        return checkCodeKey;
    }

    // retrieve checkcode while registering
    public String getCheckCode(String checkCodeKey){
        return (String) redisUtils.get(Constants.REDIS_KEY_PREFIX + checkCodeKey);
    }

    // delete checkcode to release storage
    public void cleanCheckCode(String checkCodeKey){
        redisUtils.delete(Constants.REDIS_KEY_PREFIX + checkCodeKey);
    }

    public void saveTokenInfo(TokenUserInfoDto tokenUserInfoDto){
        String token = UUID.randomUUID().toString();
        tokenUserInfoDto.setExpireAt(System.currentTimeMillis() + Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
        tokenUserInfoDto.setToken(token);
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB + token, tokenUserInfoDto, Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
    }

    public void updateTokenInfo(TokenUserInfoDto tokenUserInfoDto){
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB + tokenUserInfoDto.getToken(), tokenUserInfoDto, Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
    }


    public TokenUserInfoDto getTokenInfo(String token){
        return (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
    }

    public String getTokenInfo4Admin(String token){
        return (String) redisUtils.get(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }

    public String saveTokenInfo4Admin(String account) {
        String token = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_ADMIN + token, account, Constants.REDIS_KEY_EXPIRES_ONE_DAY);
        return token;
    }

    public void cleanToken(String token){
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_WEB + token);
    }


    public void saveCategoryList(List<CategoryInfo> categoryInfoList){
        redisUtils.set(Constants.REDIS_KEY_CATEGORY_LIST, categoryInfoList);
    }

    public List<CategoryInfo> getCategoryList(){
        return (List<CategoryInfo>) redisUtils.get(Constants.REDIS_KEY_CATEGORY_LIST);
    }

    public String savePreVideoFileInfo(String userId, String fileName, Integer chunks){
        String uploadId = StringTools.getRandomString(15);
        UploadingFileDto uploadingFileDto = new UploadingFileDto();
        uploadingFileDto.setChunks(chunks);
        uploadingFileDto.setFileName(fileName);
        uploadingFileDto.setUploadId(uploadId);
        uploadingFileDto.setChunkIndex(0);

        String day = DateUtils.formatNow("yyyyMMdd");
        String filePath = day + "/" + userId + uploadId;
        // here we are putting the uploading videos in the tmp folder
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.TEMP_FOLDER + filePath;
        File folderFile = new File(folder);
        if(!folderFile.exists()){
            folderFile.mkdirs();
        }
        uploadingFileDto.setFilePath(filePath);
        redisUtils.setex(Constants.REDIS_KEY_UPLOADING_FILE + userId + uploadId, uploadingFileDto, Constants.REDIS_KEY_EXPIRES_ONE_DAY);
        return uploadId;
    }

    public UploadingFileDto getUploadVideoFile(String userId, String uploadId){
        return (UploadingFileDto)redisUtils.get(Constants.REDIS_KEY_UPLOADING_FILE + userId + uploadId);
    }

    // pop the sys setting into the redis
    public SysSettingDto getSysSettingDto(){
        SysSettingDto sysSettingDto =  (SysSettingDto)redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        if(sysSettingDto == null){
            sysSettingDto = new SysSettingDto();
        }
        return sysSettingDto;
    }

    public void saveSysSettingDto(SysSettingDto sysSettingDto){
        redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto);
    }

    public void updateVideoFileInfo(String userId, UploadingFileDto uploadingFileDto){
        redisUtils.setex(Constants.REDIS_KEY_UPLOADING_FILE + userId + uploadingFileDto.getUploadId(), uploadingFileDto, Constants.REDIS_KEY_EXPIRES_ONE_DAY);
    }

    public void delVideoFileInfo(String userId, String uploadId){
        redisUtils.delete(Constants.REDIS_KEY_UPLOADING_FILE + userId + uploadId);
    }

    public void addFile2DelQueue(String videoId, List<String> deleteFilePathList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_FILE_DEL + videoId, deleteFilePathList, Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
    }

    public List<String> getDelFileList(String videoId) {
        return redisUtils.getQueueList(Constants.REDIS_KEY_FILE_DEL + videoId);
    }

    public void cleanDelFileList(String videoId) {
        redisUtils.delete(Constants.REDIS_KEY_FILE_DEL + videoId);
    }

    public void addFile2TransferQueue(List<VideoInfoFilePost> addFileList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_QUEUE_TRANSFER, addFileList, 0);
    }

    public void addFile2TransferQueue4Sig(VideoInfoFilePost videoInfoFilePost) {
        redisUtils.lpush(Constants.REDIS_KEY_QUEUE_TRANSFER, videoInfoFilePost, 0L);
    }

    public VideoInfoFilePost getFileFromTransferQueue(){
         return (VideoInfoFilePost) redisUtils.rpop(Constants.REDIS_KEY_QUEUE_TRANSFER);
    }

    public Integer reportVideoPlayOnline(String fileId, String deviceId){
        String userPlayOnlineKey = String.format(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_USER, fileId, deviceId);
        String playOnlineCountKey = String.format(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE, fileId);
        if(!redisUtils.keyExists(userPlayOnlineKey)){
            // the Frontend heartbeat is 5 s
            redisUtils.setex(userPlayOnlineKey, fileId, Constants.REDIS_KEY_EXPIRES_ONE_SECOND * 8);
            return redisUtils.incrementex(playOnlineCountKey, Constants.REDIS_KEY_EXPIRES_ONE_SECOND * 10).intValue();
        }
        redisUtils.expire(playOnlineCountKey, Constants.REDIS_KEY_EXPIRES_ONE_SECOND * 10);
        redisUtils.expire(userPlayOnlineKey, Constants.REDIS_KEY_EXPIRES_ONE_SECOND * 8);
        Integer count = (Integer) redisUtils.get(playOnlineCountKey);
        return count == null?1:count;
    }
    // decrease the online user
    public void decrementPlayOnlineCount(String key){
        redisUtils.decrement(key);
    }

    public void addKeywordCount(String keyword){
        redisUtils.zaddCount(Constants.REDIS_KEY_VIDEO_SEARCH_COUNT, keyword);
    }

    public List<String> getKeywordTop(Integer top){
        return redisUtils.getZSetList(Constants.REDIS_KEY_VIDEO_SEARCH_COUNT, top - 1);
    }

    public void addVideoPlay(VideoPlayInfoDto videoPlayInfoDto) {
        redisUtils.lpush(Constants.REDIS_KEY_QUEUE_VIDEO_PLAY, videoPlayInfoDto, null);
    }

    public VideoPlayInfoDto getVideoPlayFromQueue(){
        return (VideoPlayInfoDto) redisUtils.rpop(Constants.REDIS_KEY_QUEUE_VIDEO_PLAY);
    }

    public void recordVideoPlayCount(String videoId) {
        String day = DateUtils.formatNow("yyyyMMdd");
        redisUtils.incrementex(Constants.REDIS_KEY_VIDEO_PLAY_COUNT + day + ":" + videoId, Constants.REDIS_KEY_EXPIRES_ONE_DAY * 2);
    }

    public Map<String, Integer> getVideoPlayCount(String day) {
        Map<String, Integer> videoPlayMap = redisUtils.getBatch(Constants.REDIS_KEY_VIDEO_PLAY_COUNT + day);
        return videoPlayMap;
    }
}
