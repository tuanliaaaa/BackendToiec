package com.toiec.toiec.exception.word;

import com.toiec.toiec.exception.base.NotFoundException;

public class WordNotFoundException extends NotFoundException {
    public WordNotFoundException() {
        setMessage("word not found");
    }
}
