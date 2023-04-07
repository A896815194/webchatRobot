package com.web.webchat.function.timer;

import com.google.gson.Gson;
import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.excel.XqExcelDto;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.enums.Message;
import com.web.webchat.enums.PushEvent;
import com.web.webchat.util.Calculate;
import com.web.webchat.util.ReadExcel;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component("BlindDate")
@Slf4j
public class BlindDate {

    @Autowired
    private PropertiesEntity propertiesEntity;

    private final static List<String> colmuns = new ArrayList<>();

    static {
        colmuns.add("name");
        colmuns.add("address");
        colmuns.add("age");
        colmuns.add("desc");
    }

    private String convertTxt(String msg) {
        return msg.replaceAll(ReadExcel.HHF, "\r").replaceAll(ReadExcel.DYH, "=");
    }

    private RequestDto createRequestDto(PropertiesEntity propertiesEntity) {
        RequestDto dto = new RequestDto();
        dto.setEvent(PushEvent.EventGroupMsg);
        dto.setRobot_wxid(propertiesEntity.getRobotId());
        dto.setFrom_wxid(propertiesEntity.getChatroomId());
        return dto;
    }

    public void pushMan(String request) {
        PropertiesEntity propertiesEntity = new Gson().fromJson(request, PropertiesEntity.class);
        RequestDto dto = createRequestDto(propertiesEntity);
        if (ChatBase.getVerificationByType(PushEvent.EventGroupMsg.name(), FunctionType.BlindDate.name(), dto, 1)) {
            log.info("该群开启了情缘推-男:chatroomId：{}", dto.getFrom_wxid());
            // 读取配置内容
            String pzPath = createPzPath(propertiesEntity);
            List<String> result = ReadExcel.readFile(pzPath);
            List<String> man = ReadExcel.getValuesByKey(result, "内容男");
            if (CollectionUtils.isEmpty(man)) {
                log.error("不发送消息");
                return;
            }
            RequestDto requestDto = new RequestDto();
            requestDto.setMsg(convertTxt(man.get(0)));
            requestDto.setRobot_wxid(propertiesEntity.getRobotId());
            requestDto.setFrom_wxid(propertiesEntity.getChatroomId());
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(requestDto, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            return;
        }
        log.info("该群没开启-男:chatroomId：{}", dto.getFrom_wxid());
    }

    public void pushWoman(String request) {
        PropertiesEntity propertiesEntity = new Gson().fromJson(request, PropertiesEntity.class);
        RequestDto dto = createRequestDto(propertiesEntity);
        if (ChatBase.getVerificationByType(PushEvent.EventGroupMsg.name(), FunctionType.BlindDate.name(), dto, 1)) {
            // 读取配置内容
            log.info("该群开启了情缘推-女:chatroomId：{}", dto.getFrom_wxid());
            String pzPath = createPzPath(propertiesEntity);
            List<String> result = ReadExcel.readFile(pzPath);
            List<String> woman = ReadExcel.getValuesByKey(result, "内容女");
            if (CollectionUtils.isEmpty(woman)) {
                log.error("不发送消息");
                return;
            }
            //
            List<String> womanName = ReadExcel.getValuesByKey(result, "女");
            if (!CollectionUtils.isEmpty(womanName) && Objects.equals("胜者为王", womanName.get(0))) {
                RequestDto requestDto = new RequestDto();
                requestDto.setMsg("C:/Users/Public/Documents/robot/BlindDate/pic/胜者为王.jpeg");
                requestDto.setRobot_wxid(propertiesEntity.getRobotId());
                requestDto.setFrom_wxid(propertiesEntity.getChatroomId());
                RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(requestDto, ApiType.SendImageMsg), propertiesEntity.getWechatUrl());
            }
            //
            RequestDto requestDto = new RequestDto();
            requestDto.setMsg(convertTxt(woman.get(0)));
            requestDto.setRobot_wxid(propertiesEntity.getRobotId());
            requestDto.setFrom_wxid(propertiesEntity.getChatroomId());
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(requestDto, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            return;
        }
        log.info("该群没开启-女:chatroomId：{}", dto.getFrom_wxid());
    }

    public void totalData(String request) {
        PropertiesEntity propertiesEntity = new Gson().fromJson(request, PropertiesEntity.class);
        RequestDto dto = createRequestDto(propertiesEntity);
        if (ChatBase.getVerificationByType(PushEvent.EventGroupMsg.name(), FunctionType.BlindDate.name(), dto, 1)) {
            log.info("该群开启了情缘推-统计:chatroomId：{}", dto.getFrom_wxid());
            // 获取excel 路径  路径/functionType/群号.xlsx
            String excelPath = createExcelPath(propertiesEntity);
            // 读取excel内容
            List<List<Object>> list = ReadExcel.readExcelData(excelPath, colmuns, XqExcelDto.class);
            // 读取配置内容
            String pzPath = createPzPath(propertiesEntity);
            List<String> result = ReadExcel.readFile(pzPath);
            List<String> notMan = ReadExcel.getValuesByKey(result, "不推男");
            List<String> notWoMan = ReadExcel.getValuesByKey(result, "不推女");
            List<String> man = ReadExcel.getValuesByKey(result, "男");
            List<String> woman = ReadExcel.getValuesByKey(result, "女");
            List<Object> manSource = list.get(0);
            List<Object> womanSource = list.get(1);
            XqExcelDto pushManDto = getPushDto(manSource, notMan, man);
            String manContent = createPushContent(pushManDto, "男", Message.QYT_MSG);
            XqExcelDto pushWomanDto = getPushDto(womanSource, notWoMan, woman);
            String womanContent = createPushContent(pushWomanDto, "女", Message.QYT_MSG);
            String content = createPzFileContent(notMan, notWoMan, man, woman, manContent, womanContent);
            ReadExcel.outFile(pzPath, content);
            return;
        }
        log.info("该群没开启-统计:chatroomId：{}", dto.getFrom_wxid());
    }

    private String createPzFileContent(List<String> notMan, List<String> notWoMan, List<String> man, List<String> woman, String manContent, String womanContent) {
        StringBuilder sb = new StringBuilder();
        sb.append("不推男=");
        if (!CollectionUtils.isEmpty(notMan)) {
            sb.append(String.join(ReadExcel.FGF, notMan));
        }
        sb.append("\r");
        sb.append("不推女=");
        if (!CollectionUtils.isEmpty(notWoMan)) {
            sb.append(String.join(ReadExcel.FGF, notWoMan));
        }
        sb.append("\r");
        sb.append("男=");
        if (!CollectionUtils.isEmpty(man)) {
            sb.append(String.join(ReadExcel.FGF, man));
        }
        sb.append("\r");
        sb.append("女=");
        if (!CollectionUtils.isEmpty(woman)) {
            sb.append(String.join(ReadExcel.FGF, woman));
        }
        sb.append("\r");
        sb.append("内容男=");
        sb.append(String.join(ReadExcel.FGF, manContent));
        sb.append("\r");
        sb.append("内容女=");
        sb.append(String.join(ReadExcel.FGF, womanContent));
        sb.append("\r");
        return sb.toString();
    }

    private String createPushContent(XqExcelDto pushManDto, String sex, String template) {
        if (pushManDto == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("【联系方式】" + pushManDto.getName() + ReadExcel.HHF);
        if (StringUtils.isBlank(pushManDto.getAddress())) {
            sb.append("【位置】待探索" + ReadExcel.HHF);
        } else {
            sb.append("【位置】" + pushManDto.getAddress() + ReadExcel.HHF);
        }
        sb.append("【性别】" + sex + ReadExcel.HHF);
        if (StringUtils.isBlank(pushManDto.getAge())) {
            sb.append("【年龄】待探索" + ReadExcel.HHF);
        } else {
            sb.append("【年龄】" + pushManDto.getAge() + ReadExcel.HHF);
        }
        if (StringUtils.isBlank(pushManDto.getDesc())) {
            sb.append("【自我介绍】待探索" + ReadExcel.HHF);
        } else {
            sb.append("【自我介绍】" + pushManDto.getDesc() + ReadExcel.HHF);
        }
        if (StringUtils.isBlank(pushManDto.getWant())) {
            sb.append("【要求】待探索" + ReadExcel.HHF);
        } else {
            sb.append("【要求】" + pushManDto.getWant() + ReadExcel.HHF);
        }
        return String.format(template, sb.toString());
    }

    private XqExcelDto getPushDto(List<Object> data, List<String> notExist, List<String> push) {
        List<XqExcelDto> excel = convertExcel(data);
        if (CollectionUtils.isEmpty(excel)) {
            return null;
        }
        excel.removeIf(item -> {
            return notExist.contains(item.getName());
        });
        if (!CollectionUtils.isEmpty(excel)) {
            XqExcelDto dto = excel.get(Calculate.randBewteewn(0, excel.size()));
            notExist.add(dto.getName());
            push.clear();
            push.add(dto.getName());
            return dto;
        } else {
            notExist.clear();
            List<XqExcelDto> newExcel = convertExcel(data);
            XqExcelDto dto = newExcel.get(Calculate.randBewteewn(0, excel.size()));
            notExist.add(dto.getName());
            push.clear();
            push.add(dto.getName());
            return dto;
        }
    }


    private XqExcelDto getPushDtoWithIndex(List<Object> dataSource, List<String> notPushList, List<String> currentPush, Integer index) {
        List<XqExcelDto> excel = convertExcel(dataSource);
        if (CollectionUtils.isEmpty(excel)) {
            return null;
        }
        if (!CollectionUtils.isEmpty(excel)) {
            XqExcelDto dto = excel.get(index);
            notPushList.add(dto.getName());
            log.info("拿到：{}数据",dto.getName());
            currentPush.clear();
            currentPush.add(dto.getName());
            return dto;
        } else {
            notPushList.clear();
            List<XqExcelDto> newExcel = convertExcel(dataSource);
            XqExcelDto dto = newExcel.get(index);
            notPushList.add(dto.getName());
            currentPush.clear();
            currentPush.add(dto.getName());
            return dto;
        }
    }

    private List<XqExcelDto> convertExcel(List<Object> data) {
        List<XqExcelDto> excel = new ArrayList<>();
        for (Object obj : data) {
            XqExcelDto dto = new XqExcelDto();
            BeanUtils.copyProperties(obj, dto);
            excel.add(dto);
        }
        if (CollectionUtils.isEmpty(excel)) {
            return null;
        }
        return excel;
    }


    public static String createExcelPath(PropertiesEntity propertiesEntity) {
        return propertiesEntity.getAppFilePath() + "\\" + FunctionType.BlindDate.name() + "\\" + propertiesEntity.getChatroomId() + ".xlsx";
    }

    public static String createPzPath(PropertiesEntity propertiesEntity) {
        return propertiesEntity.getAppFilePath() + "\\" + FunctionType.BlindDate.name() + "\\" + propertiesEntity.getChatroomId() + "@情缘推.txt";
    }

    public static String notSex(String sex) {
        if (Objects.equals("男", sex)) {
            return "女";
        }
        return "男";
    }

    public void createTotal(RequestDto request) {
        String msg = request.getMsg();
        log.info("汇情缘:{}", new Gson().toJson(request));
        if (!("tiaotiaoxiaoshuai".equals(request.getFrom_wxid()) || "tiaotiaoxiaoshuai".equals(request.getFinal_from_wxid()))) {
            return;
        }
        if (!msg.contains("#")) {
            log.error("汇总情缘推功能但是命令不对");
            return;
        }
        String chatroomId = msg.split("#")[1];
        String pushSex = msg.split("#")[2];
        String index = msg.split("#")[3];
        log.info("chatroomId:{}", chatroomId);
        log.info("pushSex:{}", msg);
        log.info("index:{}", index);
        // 汇情缘#rootId#男#111
        // 汇情缘#rootId#女#111
        PropertiesEntity newPro = new PropertiesEntity();
        BeanUtils.copyProperties(propertiesEntity, newPro);
        newPro.setChatroomId(chatroomId);
        // 等于 0 当前群聊的情缘推
        if (Objects.equals("0", chatroomId)) {
            newPro.setChatroomId(request.getFrom_wxid());
        }
        newPro.setRobotId(request.getRobot_wxid());
        RequestDto dto = createRequestDto(newPro);
        // 获取excel 路径  路径/functionType/群号.xlsx
        String excelPath = createExcelPath(newPro);
        // 读取excel内容
        List<List<Object>> list = ReadExcel.readExcelData(excelPath, colmuns, XqExcelDto.class);
        // 读取配置内容
        String pzPath = createPzPath(newPro);
        List<String> result = ReadExcel.readFile(pzPath);
        String notChangeSex = notSex(pushSex);
        // 读当前配置文档中的  不推男为谁
        List<String> pushNotRen = ReadExcel.getValuesByKey(result, "不推" + pushSex);
        // 去掉最后一个人
        if (!CollectionUtils.isEmpty(pushNotRen)) {
            pushNotRen.remove(pushNotRen.size() - 1);
        }
        // 读取  当前推谁
        List<String> pushCurrentPushRen = ReadExcel.getValuesByKey(result, pushSex);
        List<Object> pushSource = null;
        if (Objects.equals("男", pushSex)) {
            pushSource = list.get(0);
        } else {
            pushSource = list.get(1);
        }
        // 取当前选中index 的记录
        XqExcelDto pushManDto = getPushDtoWithIndex(pushSource, pushNotRen, pushCurrentPushRen, Integer.valueOf(index));
        String pushContent = createPushContent(pushManDto, pushSex, Message.QYT_MSG);
        List<String> notWoMan = ReadExcel.getValuesByKey(result, "不推" + notChangeSex);
        List<String> woman = ReadExcel.getValuesByKey(result, notChangeSex);
        String womanContent = ReadExcel.getValueStringByKey(result, "内容" + notChangeSex);
        String content = "";
        if (Objects.equals("男", pushSex)) {
            content = createPzFileContent(pushNotRen, notWoMan, pushCurrentPushRen, woman, pushContent, womanContent);
        } else {
            content = createPzFileContent(notWoMan, pushNotRen, woman, pushCurrentPushRen, womanContent, pushContent);
        }
        ReadExcel.outFile(pzPath, content);
        request.setMsg("汇情缘成功！");
        RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
        log.info("处理完成");
    }
}
