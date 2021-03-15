package com.web.webchat.dto.baidutextreview;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaiduTextReviewResponseDto {
 // "log_id": 123456789,
//    "conclusion": "不合规",
//    "conclusionType": 2,
//    "data": [{
//        "type": 11,
//        "subType": 0,
//        "conclusion": "不合规",
//        "conclusionType": 2,
//        "msg": "存在百度官方默认违禁词库不合规",
//        "hits": [{
//            "datasetName": "百度默认黑词库",
//"probability": 1.0,
//            "words": ["免费翻墙"]
//        }]
//    }
    @JsonProperty("log_id")
    private String logId;

    private String conclusion;

    private Integer conclusionType;

    private List<Review> data;


   @Override
   public String toString() {
      return "BaiduTextReviewResponseDto{" +
              "logId='" + logId + '\'' +
              ", conclusion='" + conclusion + '\'' +
              ", conclusionType=" + conclusionType +
              ", data=" + data +
              '}';
   }
}
