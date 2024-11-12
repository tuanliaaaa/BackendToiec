package com.toiec.toiec.exception.question;

import com.toiec.toiec.exception.base.BadRequestException;

public class NumberOfQuestionsIsInsufficient extends BadRequestException {
    public NumberOfQuestionsIsInsufficient() {
        super("The number of questions is insufficient");
    }
}
