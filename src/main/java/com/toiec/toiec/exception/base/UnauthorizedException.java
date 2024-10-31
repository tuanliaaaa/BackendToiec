package com.toiec.toiec.exception.base;

public class UnauthorizedException extends BaseException{
    public UnauthorizedException() {
        setCode("com.11.FresherManage.exception.base.UnauthorizedException");
        setStatus(403);
        setMessage("UNAUTHORIZED");
    }
}