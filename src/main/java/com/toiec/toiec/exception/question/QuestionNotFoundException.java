package com.toiec.toiec.exception.question;

import com.toiec.toiec.exception.base.NotFoundException;

public class QuestionNotFoundException extends NotFoundException {
    public QuestionNotFoundException() {
        setMessage("Question not found");
    }
}
