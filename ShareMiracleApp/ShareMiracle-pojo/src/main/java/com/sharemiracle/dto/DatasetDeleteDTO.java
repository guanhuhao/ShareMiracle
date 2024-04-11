package com.sharemiracle.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class DatasetDeleteDTO implements Serializable {

    private Long id;

    private List<Long> ids = new ArrayList<>();

}
