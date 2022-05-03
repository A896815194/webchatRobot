package com.web.webchat.config.listen;

import com.web.webchat.dto.RequestDto;
import org.springframework.context.ApplicationEvent;

public class ThingEvent extends ApplicationEvent {

    private RequestDto requestDto;

    public RequestDto getRequestDto() {
        return requestDto;
    }

    public void setRequestDto(RequestDto requestDto) {
        this.requestDto = requestDto;
    }

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ThingEvent(Object source) {
        super(source);
    }

    public ThingEvent(Object source, RequestDto request) {
        super(source);
        this.requestDto = request;
    }
}
