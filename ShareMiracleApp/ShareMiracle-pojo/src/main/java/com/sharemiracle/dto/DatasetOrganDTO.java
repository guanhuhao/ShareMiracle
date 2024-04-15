package com.sharemiracle.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DatasetOrganDTO implements Serializable {

    private Long datasetId;

    private List<Long> ids = new ArrayList<>();

}
