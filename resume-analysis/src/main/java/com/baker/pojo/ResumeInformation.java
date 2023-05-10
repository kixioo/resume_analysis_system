package com.baker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeInformation {
    public String fileName;
    public String name;
    public String degree;
    public String age;
    public String phoneNumber;
    public String school;
    public String timeOfGraduation;
    public String jobObjective;
    public String workExperience;
    public String createUid;
    public Date createDate;
}
