package com.toiec.toiec.dto.request.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryWordRequest {
    private Integer id;
    private Integer score;
}
