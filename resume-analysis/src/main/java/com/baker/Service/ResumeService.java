package com.baker.Service;

import com.baker.common.ResponseResult;
import com.baker.pojo.ResumeInformation;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ResumeService {
    ResponseResult<String> uploadFile(byte[] data, String originalFilename, long job);

    ResponseResult<String> uploadCompressFile(byte[] data, String originalFilename, long job) throws IOException;

    ResponseResult<List<ResumeInformation>> getResumeByUid();

    ResponseResult<List<ResumeInformation>> getResumeByTime(String date, int category) throws ParseException;
}
