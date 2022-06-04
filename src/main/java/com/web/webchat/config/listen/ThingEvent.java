package com.web.webchat.config.listen;

import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.ThingEntity;
import org.springframework.context.ApplicationEvent;

public class ThingEvent extends ApplicationEvent {

    private RequestDto requestDto;

    private ThingEntity thing;

    public RequestDto getRequestDto() {
        return requestDto;
    }

    public ThingEntity getThing() {
        return thing;
    }

    public void setThing(ThingEntity thing) {
        this.thing = thing;
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

    public ThingEvent(Object source, RequestDto request,ThingEntity thing) {
        super(source);
        this.requestDto = request;
        this.thing = thing;
    }

    public ThingEvent(Object source, RequestDto request) {
        super(source);
        this.requestDto = request;
    }
}
