package com.toiec.toiec.exception.base;



public class NotFoundException extends BaseException {
    public NotFoundException(String id, String objectName) {
        setCode("com.g11.FresherManage.exception.base.NotFoundException");
        setStatus(404);
        addParam("id", id);
        addParam("objectName", objectName);
    }

    public NotFoundException() {
        setCode("com.g11.FresherManage.exception.base.NotFoundException");
        setStatus(404);
    }
}