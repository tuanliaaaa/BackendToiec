package com.toiec.toiec.exception.type;

import com.toiec.toiec.exception.base.NotFoundException;

public class TypeNotFoundException extends NotFoundException {
    public TypeNotFoundException(){
        this.setMessage("Type not found");
    }
}
