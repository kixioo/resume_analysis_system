package com.baker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInformation {
    private long  jid;
    private String name;
    private List<String> responsibilities;
    private List<String> require;
}
