package com.toiec.toiec.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.toiec.toiec.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponseError <T>{
    private int status;
    private String message;
    private T error;
    private String code;
    private String timestamp;
    public ResponseError(T responseGeneral, T httpStatus) {
    }
    public static <T> ResponseError<T> of(int status, String message, T error,String code) {
        return of(status, message, error,code, DateUtils.getCurrentDateString());
    }
}