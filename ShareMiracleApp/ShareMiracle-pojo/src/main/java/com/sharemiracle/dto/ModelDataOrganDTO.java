package com.sharemiracle.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ModelDataOrganDTO implements Serializable {

    private Long ModelDataId;

    private List<Long> ids = new ArrayList<>();

}
