package com.web.webchat.function.gzh;

import com.web.webchat.entity.gzh.SingDailyGzhEntity;
import com.web.webchat.enums.gzh.WeChatConstat;
import com.web.webchat.repository.gzh.SingDailyGzhRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component("singDailyGzh")
public class SingDailyGzh {

    private static final Logger logger = LogManager.getLogger(SingDailyGzh.class.getName());

    @Autowired
    private SingDailyGzhRepository singDailyGzhRepository;

    // 每日歌单
    public String saveSongDaily(String content) {
        logger.info("每日歌单:content{}", content);
        if (StringUtils.isBlank(content)) {
            return WeChatConstat.MSG_ERROR_BHF;
        }
        String songName = content;
        String timeType = calTimeType(new Date());
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();
        SingDailyGzhEntity singDailyGzhEntity = new SingDailyGzhEntity();
        singDailyGzhEntity.setYear(year);
        singDailyGzhEntity.setMonth(month);
        singDailyGzhEntity.setDay(day);
        singDailyGzhEntity.setSongName(songName);
        singDailyGzhEntity.setTimeDay(year + "-" + month + "-" + day);
        singDailyGzhEntity.setTimeType(timeType);
        singDailyGzhEntity.setCreateTime(new Date());
        singDailyGzhEntity.setUpdateTime(new Date());
        singDailyGzhRepository.save(singDailyGzhEntity);
        List<SingDailyGzhEntity> daily = singDailyGzhRepository.findAllByYearAndMonthAndDayOrderByCreateTimeAsc(year, month, day);
        return convertSingDailyMsg(daily, null, false);

    }

    private String convertSingDailyMsg(List<SingDailyGzhEntity> daily, String day, boolean range) {
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isEmpty(daily)) {
            if (range) {
                sb.append("最近3天没有唱歌哦");
            } else if (StringUtils.isBlank(day)) {
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(currentDate);
                sb.append(String.format(WeChatConstat.MSG_SING_DAILY_TOTAL_MODE, formattedDate, 0));
            } else {
                sb.append(String.format(WeChatConstat.MSG_SING_DAILY_TOTAL_MODE, day, 0));
            }
            return sb.toString();
        }
        Map<String, List<SingDailyGzhEntity>> dailyByDay = daily.stream().sorted(Comparator.comparing(SingDailyGzhEntity::getCreateTime))
                .collect(Collectors.groupingBy(SingDailyGzhEntity::getTimeDay, LinkedHashMap::new, Collectors.toList()));
        dailyByDay.forEach((days, singday) -> {
            sb.append(String.format(WeChatConstat.MSG_SING_DAILY_TOTAL_MODE, singday.get(0).getYear() + "-" + singday.get(0).getMonth() + "-" + singday.get(0).getDay(), singday.size()));
            singday.forEach(sing -> {
                sb.append(String.format(WeChatConstat.MSG_SING_DAILY_MODE, sing.getTimeType(), sing.getSongName()));
            });
        });


        return sb.toString();
    }

    private String calTimeType(Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 3 && hour < 13) {
            return "早间";
        } else if (hour >= 13 && hour < 20) {
            return "下午";
        } else {
            return "晚间";
        }
    }


    //查询某天歌单
    public String searchSongDaily(String content) {
        logger.info("查询歌单:content{}", content);
        if (StringUtils.isBlank(content) || !content.contains("年") || !content.contains("月") || !content.contains("日")) {
            return WeChatConstat.MSG_ERROR_BHF;
        }
        String yearS = content.split("年")[0];
        String monthS = content.split("年")[1].split("月")[0];
        String dayS = content.split("月")[1].split("日")[0];
        List<SingDailyGzhEntity> daily = singDailyGzhRepository.findAllByYearAndMonthAndDayOrderByCreateTimeAsc(Integer.valueOf(yearS), Integer.valueOf(monthS), Integer.valueOf(dayS));
        return convertSingDailyMsg(daily, yearS + "-" + monthS + "-" + dayS, false);
    }


    //3天歌单
    public String searchSongDailyRecent3(String content) {
        logger.info("查询最近3天歌单:content{}", content);
        // 获取当前时间
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 获取当前天最大的时间
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date maxTimeOfDay = calendar.getTime();

        // 获取当前时间3天前0时时间
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date threeDaysAgoMidnight = calendar.getTime();
        List<SingDailyGzhEntity> daily = singDailyGzhRepository.findAllByCreateTimeBetweenOrderByCreateTimeAsc(threeDaysAgoMidnight, maxTimeOfDay);
        return convertSingDailyMsg(daily, null, true);

    }
}
