package com.web.webchat.inteface;

import com.web.webchat.dto.RequestDto;

public interface Command {
    boolean open(RequestDto request);
    boolean close(RequestDto request);
}
