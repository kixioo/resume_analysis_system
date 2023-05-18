package com.baker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeInformation {
    private long  jid;
    private long rid;
    private String fileName;
    private String name;
    private String degree;
    private String age;
    private String phoneNumber;
    private String school;
    private String timeOfGraduation;
    private String jobObjective;
    private String workExperience;
    private String createUid;
    private Date createDate;
}
