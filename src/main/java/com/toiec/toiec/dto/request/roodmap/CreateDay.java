package com.toiec.toiec.dto.request.roodmap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDay {
    private String nameDay;
    private List<Integer>  lsIdLearningpath;
}
