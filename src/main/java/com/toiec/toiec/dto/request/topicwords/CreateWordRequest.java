package com.toiec.toiec.dto.request.topicwords;

import lombok.Data;

@Data
public class CreateWordRequest {
    private String word;
    private String mean;
    private String type;

}
