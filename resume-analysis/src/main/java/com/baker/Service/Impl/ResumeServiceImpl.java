package com.baker.Service.Impl;

import com.baker.Service.ResumeService;
import com.baker.Utils.Content;
import com.baker.Utils.SnowflakeIdGenerator;
import com.baker.common.ResponseResult;
import com.baker.domain.LoginUser;
import com.baker.pojo.CallableList;
import com.baker.pojo.ResumeInformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.baker.Utils.Content.*;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final static RestTemplate restTemplate = new RestTemplate();
    private static final int DOCX = 1;

    private static final int IMG = 2;


    public static String resumeUrl = "";
    @Resource
    private MongoTemplate mongoTemplate;

    SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(5);

    private final CallableList<Map<String ,Object>> analysisList;

    private final CallableList<Map<String ,Object>> zipAnalysisList;


    public ResumeServiceImpl(@Value("${resume.analysis.host}") String resumeUrl) {

        Assert.hasText(resumeUrl, "resume_analysis host cannot empty");

        ResumeServiceImpl.resumeUrl = resumeUrl;

        //加载普通文件处理流程
        analysisList = new CallableList<>(this::ResumeInformationHandler);
        zipAnalysisList = new CallableList<>(this::unzip);
    }

    @Override
    public ResponseResult<String> uploadFile(byte[] data, String originalFilename, String job) {
        String uid = ((LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getPhoneNumber();
        Integer type = typeMap.get(originalFilename.substring(originalFilename.lastIndexOf(".")+1));
        if(type==null) return ResponseResult.fail("fail");
        var map = getParameterMap(uid);
        map.put(fileName,originalFilename);
        map.put(fileType, type);
        map.put(fileData,data);
        map.put(fileJid,job);
        analysisList.add(map);
        return ResponseResult.ok("success");
    }

    @Override
    public ResponseResult<String> uploadCompressFile(byte[] data, String originalFilename, String job) {
        String uid = ((LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getPhoneNumber();
        var map = getParameterMap(uid);
        map.put(fileData,data);
        map.put(fileJid,job);
        zipAnalysisList.add(map);
        return ResponseResult.ok("success");
    }

    @Override
    public ResponseResult<List<ResumeInformation>> getResumeByUid() {
        String uid = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getPhoneNumber();
        Query query = new Query(Criteria.where(fileCreateUid).is(uid));
        var s = mongoTemplate.find(query, ResumeInformation.class);
        return ResponseResult.ok(s);
    }

    @Override
    public ResponseResult<List<ResumeInformation>> getResumeByTime(String  date, int category) throws ParseException {

        String uid = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getPhoneNumber();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        var t = format.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(t);
        var d = calendar.getTime();
        if(category==1) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }else if(category==2) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        }else {
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        }

        Query query = new Query(Criteria.where(fileCreateUid).is(uid).and(fileTime).gt(d).lte(calendar.getTime()));
        var s = mongoTemplate.find(query, ResumeInformation.class);
        return ResponseResult.ok(s);
    }

    public Map<String,Object> getParameterMap(String uid){
        Map<String,Object> map = new HashMap<>();
        map.put(fileCreateUid,uid);
        map.put(fileTime,new Date(System.currentTimeMillis()));
        return map;
    }



    public void unzip(Map<String,Object> map) {
        byte[] zipData = (byte[]) map.get(fileData);
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipData);
             ZipInputStream zis = new ZipInputStream(byteArrayInputStream)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = zis.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                byte[] fileData = byteArrayOutputStream.toByteArray();

                // 在这里处理文件数据
                var type = typeMap.get(fileName.substring(fileName.lastIndexOf(".")+1));
                if(type==null){
                    byteArrayOutputStream.close();
                    zis.closeEntry();
                    continue;
                }
                map.put(fileType,type);
                map.put(Content.fileName,fileName);
                map.put(Content.fileData,fileData);
                ResumeInformationHandler(map);
                //处理完成
                byteArrayOutputStream.close();
                zis.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ResumeInformationHandler(Map<String,Object> i){
        ResponseEntity<String> r;
        if((int)i.get(fileType)==DOCX) {
            r = restTemplate.postForEntity(resumeUrl + analysisDocxApi + i.get(fileName)
                    , new HttpEntity<>(i.get(fileData), null), String.class);
        }else if((int)i.get(fileType)==IMG){
            r = restTemplate.postForEntity(resumeUrl + analysisImgApi + i.get(fileName)
                    , new HttpEntity<>(i.get(fileData), null), String.class);
        }else {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            var information = mapper.readValue(r.getBody(),ResumeInformation.class);
            information.setCreateUid((String) i.get(fileCreateUid));
            information.setCreateDate((Date) i.get(fileTime));
            information.setRid(idGenerator.nextId());
            information.setJid((Long) i.get(fileJid));
            mongoTemplate.insert(information);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
