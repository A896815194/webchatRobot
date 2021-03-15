package com.web.webchat.vo;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

	private String code;

	private T results ;


}
