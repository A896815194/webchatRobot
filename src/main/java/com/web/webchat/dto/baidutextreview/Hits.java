package com.web.webchat.dto.baidutextreview;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hits {
//            "datasetName": "百度默认黑词库",
//              "probability": 1.0,
//            "words": ["免费翻墙"]
    private String datasetName;

    private Integer probability;

    private List<String> words;

    @Override
    public String toString() {
        return "Hits{" +
                "datasetName='" + datasetName + '\'' +
                ", probability=" + probability +
                ", words=" + words +
                '}';
    }
}
