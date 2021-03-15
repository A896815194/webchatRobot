package com.web.webchat.strategy;

import com.web.webchat.dto.RequestDto;
import com.web.webchat.inteface.Command;
import com.web.webchat.inteface.FunctionInt;

public class SwitchControl implements FunctionInt {

    private Command command;

    public void setFunctionSwitch(Command command){
        this.command= command;
    }

    @Override
    public boolean openFunction(RequestDto request) {
        request.setMsg("开启"+request.getMsg());
        return command.open(request);
    }

    @Override
    public boolean closeFunction(RequestDto request) {
        request.setMsg("关闭"+request.getMsg());
        return command.close(request);
    }

}
