package com.web.webchat.inteface;

import com.web.webchat.dto.RequestDto;

public abstract class Verification {

    public void setVerificationHandler(Verification verificationHandler) {
        this.verificationHandler = verificationHandler;
    }

    protected Verification verificationHandler;

    public abstract boolean hasOpen(RequestDto request,String functionType,int open);
}
