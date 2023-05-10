package com.baker.Controller;

import com.baker.Service.ResumeService;
import com.baker.common.ResponseResult;
import com.baker.pojo.ResumeInformation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Resource
    ResumeService resumeService;

    @PostMapping("/compressUploadFile")
    public ResponseResult<String> uploadCompressFile(@RequestPart("resume") MultipartFile resume) throws IOException {
        return resumeService.uploadCompressFile(resume);
    }

    @PostMapping("/uploadFile/{type}")
    public ResponseResult<String> uploadFile(@PathVariable int type,
                                             @RequestPart("resume") MultipartFile resume) throws IOException {
        return resumeService.uploadFile(type,resume.getBytes(),resume.getOriginalFilename());
    }


    @GetMapping("/get/uid")
    public ResponseResult<List<ResumeInformation>> getResumeByUid(){
        return resumeService.getResumeByUid();
    }

    @GetMapping("/get/date/{category}/{date}")
    public ResponseResult<List<ResumeInformation>> getResumeByDay(@PathVariable String date, @PathVariable int category) throws ParseException {
        return resumeService.getResumeByTime(date,category);
    }

}
