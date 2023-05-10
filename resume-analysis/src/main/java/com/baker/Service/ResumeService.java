package com.baker.Service;

import com.baker.common.ResponseResult;
import com.baker.pojo.ResumeInformation;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ResumeService {
    ResponseResult<String> uploadFile(int type, byte[] data, String originalFilename);

    public ResponseResult<String> uploadCompressFile(MultipartFile resume) throws IOException;

    ResponseResult<List<ResumeInformation>> getResumeByUid();

    ResponseResult<List<ResumeInformation>> getResumeByTime(String date, int category) throws ParseException;
}
