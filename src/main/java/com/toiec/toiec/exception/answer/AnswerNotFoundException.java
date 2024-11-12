package com.toiec.toiec.exception.answer;

import com.toiec.toiec.exception.base.NotFoundException;

public class AnswerNotFoundException extends NotFoundException {
    public AnswerNotFoundException(){
        setMessage("Answer not found");
        setCode("com.g11.FresherManage.exception.answer.AnswerNotFoundException");
    }
}
