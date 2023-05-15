package com.baker.Service;

import com.baker.common.ResponseResult;
import com.baker.pojo.JobInformation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface JobInformationService {
    ResponseResult<String> addJob(@RequestBody JobInformation jobInformation);

    ResponseResult<String> deleteJob(@RequestBody JobInformation jobInformation);

    ResponseResult<String> putJob(@RequestBody JobInformation jobInformation);

    ResponseResult<List<JobInformation>> getJob();
}
