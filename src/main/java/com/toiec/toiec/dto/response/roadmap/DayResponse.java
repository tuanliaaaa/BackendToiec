package com.toiec.toiec.dto.response.roadmap;

import com.toiec.toiec.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayResponse {
    private Integer id;
    private String nameDay;
    public DayResponse(Lesson lesson) {
        this.id = lesson.getIdLesson();
        this.nameDay = lesson.getNameLesson();
    }
}
