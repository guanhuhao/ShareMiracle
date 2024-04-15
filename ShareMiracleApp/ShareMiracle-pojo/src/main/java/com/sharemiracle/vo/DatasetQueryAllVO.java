package com.sharemiracle.vo;


import com.sharemiracle.entity.Dataset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasetQueryAllVO implements Serializable {

    private List<Long> ids = new ArrayList<>();

}
