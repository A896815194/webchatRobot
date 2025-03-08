package com.web.webchat.api;

import com.web.webchat.entity.ShopEntity;
import com.web.webchat.entity.birth.BirthCardEntity;
import com.web.webchat.repository.ShopRepository;
import com.web.webchat.repository.birth.BirthCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1")
@Slf4j
public class BirthDayController {

    @Autowired
    private BirthCardRepository birthCardRepository;


    @GetMapping("birth/chouka")
    public Object chouka() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        List<ShopEntity> shops = shopRepository.findAllByThingId("999");
        if (CollectionUtils.isEmpty(shops)) {
            result.put("msg", "抽奖次数还没有到来");
            return result;
        }
        Integer count = shops.get(0).getThingCount();
        if (count < 1) {
            result.put("msg", "没有抽奖次数了");
            return result;
        }
        BirthCardEntity card = birthCardRepository.findTopBySendAndCardType("0", "1");
        if (card == null) {
            result.put("msg", "没有卡片了");
            return result;
        }
        card.setSend("1");
        birthCardRepository.save(card);
        count--;
        shops.get(0).setThingCount(count);
        shopRepository.save(shops.get(0));
        result.put("success", true);
        result.put("data", card);
        return result;
    }

    @Autowired
    private ShopRepository shopRepository;

    @GetMapping("birth/count")
    public Object choukaCount() {
        List<ShopEntity> shops = shopRepository.findAllByThingId("999");
        if (CollectionUtils.isEmpty(shops)) {
            return 0;
        }
        return shops.get(0).getThingCount();
    }


    @GetMapping("birth/chouka/bag")
    public Object bag() {
        Map<String, Object> result = new HashMap<>();
        List<BirthCardEntity> card = birthCardRepository.findAllBySendAndCardType("1", "1");
        if (CollectionUtils.isEmpty(card)) {
            result.put("success", false);
            return result;
        }
        result.put("success", true);
        result.put("data", card);
        return result;
    }

    @GetMapping("birth/zflist")
    public Object zflist() {
        Map<String, Object> result = new HashMap<>();
        List<BirthCardEntity> card = birthCardRepository.findAllByCardType("2");
        if (CollectionUtils.isEmpty(card)) {
            result.put("success", false);
            return result;
        }
        result.put("success", true);
        result.put("data", card);
        List<String> names = card.stream().map(BirthCardEntity::getName).collect(Collectors.toList());
        result.put("nameList", names);
        return result;
    }

    @PostMapping("birth/zf")
    public Object saveZf(@RequestBody Map<String, Object> params) {
        String name = (String) params.get("name");
        String content = (String) params.get("content");
        String sfsl = (String) params.get("sfsl");
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isBlank(name) || StringUtils.isBlank(content) || StringUtils.isBlank(sfsl)) {
            result.put("success", false);
            result.put("msg", "填写异常");
            return result;
        }
        BirthCardEntity cardEntiy = new BirthCardEntity();
        cardEntiy= cardEntiy.builder()
                .name(name)
                .content(content)
                .sfsl(sfsl)
                .cardType("2")
                .build();
        birthCardRepository.save(cardEntiy);
        result.put("success", true);
        return result;
    }

}
