package com.baker.Service.Impl;

import com.baker.Service.ResumeService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final static String analysisDocxApi = "/getInformation/file/docx/";
    private final static String analysisImgApi = "/getInformation/file/img/";
    private final static RestTemplate restTemplate = new RestTemplate();
    private static final String fileName = "fileName";
    private static final String fileData = "data";
    private static final String fileTime = "createDate";
    private static final String fileCreateUid = "createUid";
    private static final String fileType = "type";
    private static final int DOCX = 1;

    private static final int IMG = 2;


    public static String resumeUrl = "";
    @Resource
    private MongoTemplate mongoTemplate;

    private final CallableList<Map<String ,Object>> analysisList;

    private final CallableList<Map<String ,Object>> zipAnalysisList;

    public ResumeServiceImpl(@Value("${resume.analysis.host}") String resumeUrl) {

        Assert.hasText(resumeUrl, "resume_analysis host cannot empty");

        ResumeServiceImpl.resumeUrl = resumeUrl;

        //加载普通文件处理流程
        analysisList = new CallableList<>(i->{
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
                mongoTemplate.insert(information);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        zipAnalysisList = new CallableList<>(i-> unzip((byte[]) i.get(fileData), (String) i.get(fileCreateUid), (Date) i.get(fileTime)));
    }

    @Override
    public ResponseResult<String> uploadFile(int type, byte[] data, String originalFilename) {
        Map<String,Object> map = new HashMap<>();
        map.put(fileData,data);
        map.put(fileName,originalFilename);
        map.put(fileType,type);
        String uid = ((LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getPhoneNumber();
        map.put(fileCreateUid,uid);
        map.put(fileTime,new Date(System.currentTimeMillis()));
        analysisList.add(map);
        return ResponseResult.ok("success");
    }

    @Override
    public ResponseResult<String> uploadCompressFile(MultipartFile resume) throws IOException {
        Map<String,Object> map = new HashMap<>();
        String uid = ((LoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getPhoneNumber();
        map.put(fileData,resume.getBytes());
        map.put(fileCreateUid,uid);
        map.put(fileTime,new Date(System.currentTimeMillis()));
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




    public void unzip(byte[] zipData,String fileCreateUid,Date fileTime) {
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
                ResponseEntity<String> r;
                if(fileName.endsWith("docx")){
                    r = restTemplate.postForEntity(resumeUrl + analysisDocxApi + fileName
                            , new HttpEntity<>(fileData, null), String.class);
                }else if(fileName.endsWith("png")||fileName.endsWith("jpg")||fileName.endsWith("jpeg")){
                    r = restTemplate.postForEntity(resumeUrl + analysisImgApi + fileName
                            , new HttpEntity<>(fileData, null), String.class);
                }else {
                    byteArrayOutputStream.close();
                    zis.closeEntry();
                    continue;
                }
                ObjectMapper mapper = new ObjectMapper();
                try {
                    var information = mapper.readValue(r.getBody(),ResumeInformation.class);
                    information.setCreateUid(fileCreateUid);
                    information.setCreateDate(fileTime);
                    mongoTemplate.insert(information);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                //处理完成
                byteArrayOutputStream.close();
                zis.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
