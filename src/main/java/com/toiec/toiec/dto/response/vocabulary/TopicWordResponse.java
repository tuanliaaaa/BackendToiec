package com.toiec.toiec.dto.response.vocabulary;

import com.toiec.toiec.entity.Lesson;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopicWordResponse {
    private int id;
    private String name;
    public TopicWordResponse(Lesson toiec) {
        this.id = toiec.getIdLesson();
        this.name = toiec.getNameLesson();
    }
}
