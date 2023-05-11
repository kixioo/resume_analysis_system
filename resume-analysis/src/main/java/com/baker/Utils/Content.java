package com.baker.Utils;

import java.util.HashMap;
import java.util.Map;

public class Content {

    public final static String analysisDocxApi = "/getInformation/file/docx/";
    public final static String analysisImgApi = "/getInformation/file/img/";
    public static final String fileName = "fileName";
    public static final String fileData = "data";
    public static final String fileTime = "createDate";
    public static final String fileCreateUid = "createUid";
    public static final String fileType = "type";

    public static final int DOCX = 1;

    private static final int IMG = 2;
    public static Map<String ,Integer> typeMap;


    static {
        typeMap = new HashMap<>();
        typeMap.put("docx",DOCX);
        typeMap.put("jpeg",IMG);
        typeMap.put("jpg",IMG);
        typeMap.put("png",IMG);
    }
}
