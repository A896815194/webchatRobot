package com.web.webchat.function.video;

import cn.hutool.json.JSONUtil;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.entity.video.DanmuEntity;
import com.web.webchat.repository.gzh.DanmuRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("videoFunction")
public class VideoFunction {

    private static final Logger logger = LogManager.getLogger(VideoFunction.class.getName());

    @Autowired
    private PropertiesEntity propertiesEntity;

    @Autowired
    private DanmuRepository danmuRepository;


    // 20241027@mid
    public String initVideoDanmu(String content) {
        logger.info("初始化视频弹幕:content{}", content);
        String directiory = propertiesEntity.getVideoUrl();
        String path = content.split("\\+")[0];
        String uid = content.split("\\+")[1];
        logger.info("读取地址:content{}", directiory + "/" + path);
        File directory = new File(directiory + "/" + path);
        File[] files = directory.listFiles();
        Long Index = 1L;
        danmuRepository.deleteAllById(uid);
        List<DanmuEntity> entities = new ArrayList<>();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                entities.addAll(initVideoMsg(file, file.getName(), uid, Index));
            }
        }
        if (!CollectionUtils.isEmpty(entities)) {
            entities.forEach(item->{
                danmuRepository.save(item);
            });

        }
        return "操作成功";
    }


    public List<DanmuEntity> initVideoMsg(File file, String fileName, String uid, Long index) {

        List<DanmuEntity> danmuEntities = new ArrayList<>();
        fileName = fileName.split("\\.")[0];
        Long videoStartTime = convertTimeStringToSeconds(fileName);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); // 去掉收尾空格
                index++;
                DanmuEntity danmu = fillDanmuString(uid, line, videoStartTime, index);
                if (danmu != null) {
                    danmuEntities.add(danmu);
                }
            }
        } catch (Exception e) {
            logger.error("读取文件失败", e);
        }
        return danmuEntities;
    }

    public Long convertTimeStringToSeconds(String timeString) {
        long time = 0l;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date date = sdf.parse(timeString);
            time = date.getTime();
        } catch (Exception e) {
            logger.error("转换错误", e);
        }
        return time / 1000;
    }

    private DanmuEntity fillDanmuString(String mid, String msg, Long startTime, Long index) {
        DanmuEntity danmuEntity = new DanmuEntity();
        if (msg.startsWith("【礼物msg】")) {
            danmuEntity.setId(mid);
            danmuEntity.setType("right");
            danmuEntity.setCid(index);
            danmuEntity.setText(msg.split("】")[2]);
            danmuEntity.setColor("rgb(255, 0, 255)");
            danmuEntity.setSize("27.5px");
            String curTimeStr = msg.split("】")[1].split("【")[1];
            BigDecimal curTimeB = new BigDecimal(curTimeStr);
            curTimeB.setScale(6, RoundingMode.HALF_UP);
            BigDecimal startB = new BigDecimal(startTime);
            startB.setScale(6, RoundingMode.HALF_UP);
            BigDecimal result = curTimeB.subtract(startB);
            result.setScale(3, RoundingMode.HALF_UP);
            danmuEntity.setVideotime(result.floatValue());
            danmuEntity.setIp("127.0.0.1");
            danmuEntity.setTime(Integer.valueOf(startB.toString()));
            return danmuEntity;
        }
        if (msg.startsWith("【聊天msg】")) {
            danmuEntity.setId(mid);
            danmuEntity.setCid(index);
            danmuEntity.setType("right");
            danmuEntity.setText(msg.split("】")[3]);
            danmuEntity.setColor("rgb(128, 138, 135)");
            danmuEntity.setSize("27.5px");
            String curTimeStr = msg.split("】")[1].split("【")[1];
            float num = Float.parseFloat(curTimeStr);
            double roundedNum = Math.round(num * 1000.0) / 1000.0;
            danmuEntity.setVideotime((float) (roundedNum - startTime));
            danmuEntity.setIp("127.0.0.1");
            danmuEntity.setTime((int) roundedNum);
            return danmuEntity;
        }
        return null;
    }
}
