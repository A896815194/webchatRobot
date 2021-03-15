package com.web.webchat.dto.tulingrobot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RobotResult {
    @JsonProperty("groupType")
    private int groupType;
    @JsonProperty("resultType")
    private String resultType;
    @JsonProperty("values")
    private Map<String,String> values;

}
