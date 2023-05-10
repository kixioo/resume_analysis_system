package com.baker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TwoParamObject<K,T> {
    K first;
    T second;

}
