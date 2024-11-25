package com.toiec.toiec.dto.response.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    private  Integer idResource;
    private  String resourceContent;
    private  String resourceType;
}
