package com.web.webchat.dto.baidutextreview;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
 //   {
//        "type": 11,
//        "subType": 0,
//        "conclusion": "不合规",
//        "conclusionType": 2,
//        "msg": "存在百度官方默认违禁词库不合规",
//        "hits": [{
//            "datasetName": "百度默认黑词库",
//"probability": 1.0,
//            "words": ["免费翻墙"]
//        }
//审核主类型，11：百度官方违禁词库、12：文本反作弊、13:自定义文本黑名单、14:自定义文本白名单
    private Integer type;
    //当type=11时subType取值含义：
    //0:百度官方默认违禁词库
    //当type=12时subType取值含义：
    //0:低质灌水、1:暴恐违禁、2:文本色情、3:政治敏感、4:恶意推广、5:低俗辱骂 6:恶意推广-联系方式、7:恶意推广-软文推广、8:广告法审核
    //当type=13时subType取值含义：
    //0:自定义文本黑名单
    //当type=14时subType取值含义：
    //0:自定义文本白名单
    private Integer subType;

    private String conclusion;

    private Integer conclusionType;

    private String msg;

    private List<Hits> hits;

   @Override
   public String toString() {
      return "Review{" +
              "type=" + type +
              ", subType=" + subType +
              ", conclusion='" + conclusion + '\'' +
              ", conclusionType=" + conclusionType +
              ", msg='" + msg + '\'' +
              ", hits=" + hits +
              '}';
   }
}
