package com.web.webchat.util.baiducommon;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsrResponse {

    private String corpus_no;

    private String err_msg;

    private String err_no;

    private List<String> result;

    private String sn;
}
