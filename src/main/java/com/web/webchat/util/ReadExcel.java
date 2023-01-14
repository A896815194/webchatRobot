package com.web.webchat.util;

import com.web.webchat.dto.excel.XqExcelDto;
import com.web.webchat.enums.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ReadExcel {

    public final static String FGF = "#fgf#";
    public final static String HHF = "#hhf";
    public final static String DYH = "#dyh";
    private final static List<String> header = new ArrayList<>();
    private final static List<String> colmuns = new ArrayList<>();

    static {
        colmuns.add("name");
        colmuns.add("address");
        colmuns.add("age");
        colmuns.add("desc");
        colmuns.add("want");
    }

    static {
        header.add("联系方式");
        header.add("位置");
        header.add("年龄\n" +
                "选填");
        header.add("自我介绍");
    }

    public static List<String> readFile(String filePath) {
        List<String> result = new ArrayList<>();
        try {
            Files.lines(Paths.get(filePath), StandardCharsets.UTF_8).forEach(item -> {
                if (StringUtils.isNotBlank(item)) {
                    result.add(item);
                }
            });
        } catch (Exception e) {
            log.error("读取失败", e);
        }
        return result;
    }

    public static void outFile(String filePath, String content) {
        File f = new File(filePath);
        File pafile = f.getParentFile();
        //不存在父路径就创建
        if (!pafile.exists()) {
            pafile.mkdirs();
        }

        FileOutputStream fos1 = null;
        try {
            fos1 = new FileOutputStream(f);
            OutputStreamWriter dos1 = new OutputStreamWriter(fos1);
            dos1.write(content);
            dos1.close();
        } catch (Exception e) {
            log.error("输出异常", e);
        }

    }

    public static void main(String[] args) {
        String filePath = "G:\\wechatFile\\WeChat Files\\tiaotiaoxiaoshuai\\FileStorage\\File\\2023-01\\勇敢一点就有故事.xlsx";
        List<List<Object>> list = readExcelData(filePath, colmuns, XqExcelDto.class);
//        StringBuilder out = new StringBuilder();
//        for (List<Object> data : list) {
//            XqExcelDto dto = (XqExcelDto) data.get(0);
//            System.out.println(dto.getName());
//            out.append(dto.getName() + "\r");
//        }
        //outFile("G:\\test\\推送.txt", out.toString());
        String pzPath = "G:\\test\\情缘推.txt";
        List<String> result = readFile(pzPath);
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

    }

    private static String createPzFileContent(List<String> notMan, List<String> notWoMan, List<String> man, List<String> woman, String manContent, String womanContent) {
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

    private static String createPushContent(XqExcelDto pushManDto, String sex, String template) {
        if (pushManDto == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("【联系方式】" + pushManDto.getName());
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

    private static XqExcelDto getPushDto(List<Object> data, List<String> notExist, List<String> push) {
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

    private static List<XqExcelDto> convertExcel(List<Object> data) {
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

    public static List<String> getValuesByKey(List<String> result, String key) {
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        String content = result.stream().filter(data -> {
            return data.startsWith(key + "=");
        }).collect(Collectors.joining());
        List<String> values = new ArrayList<>();
        if (StringUtils.isBlank(content) || !content.contains("=")) {
            return new ArrayList<>();
        }
        if (content.split("=").length < 2) {
            return new ArrayList<>();
        } else {
            String value = content.split("=")[1];
            if (StringUtils.isBlank(value)) {
                return new ArrayList<>();
            }
            if (value.contains(FGF)) {
                String[] valuenum = value.split(FGF);
                values.addAll(Arrays.asList(valuenum));
            } else {
                values.add(value);
            }
        }
        return values;
    }

    public static List<List<Object>> readExcelData(String filePath, List<String> colmuns, Class classzz
    ) {
        List<List<Object>> list = new ArrayList<>();
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        wb = readExcel(filePath);
        if (wb != null) {
            try {
                System.err.println("页签数量：" + wb.getNumberOfSheets());
                // 循环页签
                for (int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); sheetNum++) {
                    // 指定页签的值
                    sheet = wb.getSheetAt(sheetNum);
                    // 定义存放一个页签中所有数据的List
                    List<Object> sheetList = new ArrayList<>();

                    System.err.println("行总数：" + sheet.getLastRowNum());
                    // 循环行
                    for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        // 指定行的值
                        if (rowNum == 0) {
                            continue;
                        }
                        row = sheet.getRow(rowNum);
                        // 定义存放一行数据的List
                        //List<Object> rowList = new ArrayList<>();
                        // 循环列
                        //System.err.println("列总数：" + row.getLastCellNum());
//                        for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
//                            Cell cell = sheet.getRow(rowNum).getCell(cellNum);
//                            String cellValue = getStringCellValue(cell);
//                            if (cellNum == 0 && StringUtils.isBlank(cellValue)) {
//                                break;
//                            }
//                            rowList.add(getStringCellValue(cell));
//                        }
                        Object dto = classzz.newInstance();
                        boolean isJump = false;
                        for (int cellNum = 0; cellNum < colmuns.size(); cellNum++) {
                            Cell cell = sheet.getRow(rowNum).getCell(cellNum);
                            String cellValue = StringUtils.trim(getStringCellValue(cell, true));
                            // 去乱七八糟的符号
                            cellValue = cellValue.replaceAll("\\s*", "");
                            if (cellNum == 0 && StringUtils.isBlank(cellValue)) {
                                isJump = true;
                                break;
                            }
                            Field field = classzz.getDeclaredField(colmuns.get(cellNum));
                            field.setAccessible(true);
                            field.set(dto, getStringCellValue(cell, true));
                        }
                        if (!isJump) {
                            sheetList.add(dto);
                        }
                    }
                    list.add(sheetList);
                }
                System.err.println(list.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    //判断文件格式
    private static Workbook readExcel(String filePath) {
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));

        try {
            @SuppressWarnings("resource")
            InputStream is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static String getStringCellValue(Cell cell, boolean isBlzs) {
        String cellvalue = "";
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellvalue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = cell.getDateCellValue();
                    cellvalue = sdf.format(date);
                } else {
                    if (isBlzs) {
                        double count = cell.getNumericCellValue();
                        DecimalFormat df = new DecimalFormat("0");
                        return df.format(count);
                    }
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellvalue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK:
                cellvalue = "";
                break;
            default:
                cellvalue = "";
                break;
        }
        if (cellvalue == "") {
            return "";
        }
        return cellvalue;
    }


    public static boolean isCellDateFormatted(Cell cell) {
        if (cell == null) return false;
        boolean bDate = false;

        double d = cell.getNumericCellValue();
        if (isValidExcelDate(d)) {
            CellStyle style = cell.getCellStyle();
            if (style == null) return false;
            int i = style.getDataFormat();
            String f = style.getDataFormatString();
            bDate = isADateFormat(i, f);
        }
        return bDate;
    }

    public static boolean isADateFormat(int formatIndex, String formatString) {
        if (isInternalDateFormat(formatIndex)) {
            return true;
        }

        if ((formatString == null) || (formatString.length() == 0)) {
            return false;
        }

        String fs = formatString;
        //下面这一行是自己手动添加的 以支持汉字格式wingzing
        fs = fs.replaceAll("[\"|\']", "").replaceAll("[年|月|日|时|分|秒|毫秒|微秒]", "");

        fs = fs.replaceAll("\\\\-", "-");

        fs = fs.replaceAll("\\\\,", ",");

        fs = fs.replaceAll("\\\\.", ".");

        fs = fs.replaceAll("\\\\ ", " ");

        fs = fs.replaceAll(";@", "");

        fs = fs.replaceAll("^\\[\\$\\-.*?\\]", "");

        fs = fs.replaceAll("^\\[[a-zA-Z]+\\]", "");

        return (fs.matches("^[yYmMdDhHsS\\-/,. :]+[ampAMP/]*$"));
    }

    public static boolean isInternalDateFormat(int format) {
        switch (format) {
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 45:
            case 46:
            case 47:
                return true;
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
        }
        return false;
    }

    public static boolean isValidExcelDate(double value) {
        return (value > -4.940656458412465E-324D);

    }
}