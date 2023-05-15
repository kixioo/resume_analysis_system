package com.baker.Controller;

import com.baker.Service.JobInformationService;
import com.baker.common.ResponseResult;
import com.baker.pojo.JobInformation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/job")
public class JobInformationController {

    @Resource
    JobInformationService jobInformationService;

    @PostMapping
    public ResponseResult<String> addJob(@RequestBody JobInformation jobInformation){
        return jobInformationService.addJob(jobInformation);
    }

    @DeleteMapping
    public ResponseResult<String> deleteJob(@RequestBody JobInformation jobInformation){
        return jobInformationService.deleteJob(jobInformation);
    }

    @PutMapping
    public ResponseResult<String> putJob(@RequestBody JobInformation jobInformation){
        return jobInformationService.putJob(jobInformation);
    }
    @GetMapping
    public ResponseResult<List<JobInformation>> getJob(){
        return jobInformationService.getJob();
    }

}
