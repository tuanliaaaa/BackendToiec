package com.toiec.toiec.dto.response.topicwords;

import lombok.Data;

@Data
public class WordResponse {
    private int id;
    private String word;
    private String mean;
    private String type;
}