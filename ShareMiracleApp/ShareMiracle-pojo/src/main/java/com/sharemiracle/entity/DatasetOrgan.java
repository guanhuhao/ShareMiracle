package com.sharemiracle.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasetOrgan {

    Long id;

    Long datasetId;

    Long organId;

}
