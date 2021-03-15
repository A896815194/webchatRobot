package com.web.webchat.dto.baidutextreview;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssToken {

    private String refresh_token;

    private String expires_in;

    private String session_key;

    private String access_token;

    private String session_secret;
}
