package com.baker.Service.Impl;

import com.baker.Service.JobInformationService;
import com.baker.Utils.SnowflakeIdGenerator;
import com.baker.common.ResponseResult;
import com.baker.pojo.JobInformation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JobInformationServiceImpl implements JobInformationService {

    @Resource
    MongoTemplate mongoTemplate;

    SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(5);
    @Override
    public ResponseResult<String> addJob(JobInformation jobInformation) {
        jobInformation.setJid(idGenerator.nextId());
        mongoTemplate.insert(jobInformation);
        return ResponseResult.ok("success");
    }

    @Override
    public ResponseResult<String> deleteJob(JobInformation jobInformation) {
        Query query = new Query(Criteria.where("jid").is(jobInformation.getJid()));
        mongoTemplate.remove(query);
        return ResponseResult.ok("success");
    }

    @Override
    public ResponseResult<String> putJob(JobInformation jobInformation) {
        mongoTemplate.findAndReplace(new Query(Criteria.where("jid").is(jobInformation)),jobInformation);
        return ResponseResult.ok("success");
    }

    @Override
    public ResponseResult<List<JobInformation>> getJob() {
        return ResponseResult.ok(mongoTemplate.findAll(JobInformation.class));
    }
}
