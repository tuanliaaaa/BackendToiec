package com.toiec.toiec.exception.user;


import com.toiec.toiec.exception.base.NotFoundException;

public class UsernameNotFoundException extends NotFoundException {
    public UsernameNotFoundException(){
        setMessage("User not found");
        setCode("com.g11.FresherManage.exception.account.UsernameNotFoundException");
    }
}