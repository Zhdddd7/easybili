package com.easybili.admin.controller;

import com.easybili.constants.Constants;
import com.easybili.entities.config.AppConfig;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.vo.ResponseVO;

import com.easybili.utils.DateUtils;
import com.easybili.utils.FFmpegUtils;
import com.easybili.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;

@RestController
@RequestMapping("/file")
@Validated
@Slf4j
public class FileController extends BaseController<String>{
    @Resource
    private AppConfig appConfig;

    @Resource
    private FFmpegUtils fFmpegUtils;

    @RequestMapping("/uploadImage")
    public ResponseVO<String> uploadImage(@NotNull MultipartFile file, @NotNull Boolean createThumbnail) throws IOException {
        String month = DateUtils.formatNow("yyyyMM");
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.COVER_FOLDER + month;
        File folderFile = new File(folder);
        if(!folderFile.exists())
        {
            folderFile.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String fileSuffix = StringTools.getSuffix(fileName);
        String realFileName = StringTools.getRandomString(30) + fileSuffix;
        String filePath = folder + "/" + realFileName;
        file.transferTo(new File(filePath));
        if(createThumbnail){
            // TODO
            fFmpegUtils.createImageThumbnail(filePath);
        }
        return ResponseVO.getSuccessResponseVO(Constants.COVER_FOLDER + month + "/" + realFileName);
    }

    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse response, @NotNull String sourceName) throws IOException {

        if (!StringTools.pathIsOk(sourceName)) {
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        String suffix = StringTools.getSuffix(sourceName);
        response.setContentType("image/" + suffix.replace(".", ""));
        response.setHeader("Cache-Control", "max-age=2592000");
        readFile(response, sourceName);
    }

    protected void readFile(HttpServletResponse response, String filePath) {
        File file = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + filePath);
        if (!file.exists()) {
            return;
        }
        try (OutputStream out = response.getOutputStream(); FileInputStream in = new FileInputStream(file)) {
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        }catch (Exception e) {
            log.error("error FILE IO", e);
        }

    }
}
