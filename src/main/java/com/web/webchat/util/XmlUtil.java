package com.web.webchat.util;


import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.web.webchat.dto.WxBaseDto.*;
import lombok.SneakyThrows;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {

    private final static Map<String, String> xmlMap = new HashMap<String, String>();

    private final static Class[] converClass = new Class[]{WxBaseResonseDto.class, HfContentResponseDto.class, HfImageResponseDto.class,HfMusicResponseDto.class, HfVoiceResponseDto.class,HfVideoResponseDto.class,HfArticleResponseDto.class};

    @SneakyThrows
    public static void main(String[] args) {
        String test = "<xml>\n" +
                "  <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "  <CreateTime>1348831860</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA[this is a test]]></Content>\n" +
                "  <MsgId>1234567890123456</MsgId>\n" +
                "  <MsgDataId>xxxx</MsgDataId>\n" +
                "  <Idx>xxxx</Idx>\n" +
                "</xml>";


        //System.out.println(xmlToMap(test));
        Map<String, String> testmap = xmlToMap(test);
        Gson gson = new Gson();
        WxRequestDto dtos = gson.fromJson(gson.toJson(testmap), WxRequestDto.class);
        System.out.println(dtos);
        HfArticleResponseDto dto = new HfArticleResponseDto();
        dto.setToUserName("test");
        dto.setFromUserName("name");
        dto.setMsgType("1");
        dto.setCreateTime("1232132");
        List<HfArticleResponseDto.Article> item = new ArrayList<>();
        HfArticleResponseDto.Article a = new HfArticleResponseDto.Article();
        a.setDescription("test");
        item.add(a);
        dto.setArticles(item);
        System.out.println(DtoToXmlString(dto));
    }

    public static String DtoToXmlString(WxBaseResonseDto dto) {
        XStream stream = new XStream();
        stream.processAnnotations(converClass);
        return stream.toXML(dto);
    }

    /**
     * xml字符串转换成Map 获取标签内属性值和text值
     *
     * @param xml
     * @return
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String xml) throws Exception {
        StringReader reader = new StringReader(xml);
        InputSource source = new InputSource(reader);
        SAXReader sax = new SAXReader(); // 创建一个SAXReader对象
        Document document = sax.read(source); // 获取document对象,如果文档无节点，则会抛出Exception提前结束
        Element root = document.getRootElement(); // 获取根节点
        Map<String, String> map = XmlUtil.getNodes(root); // 从根节点开始遍历所有节点
        return map;
    }

    /**
     * 从指定节点开始,递归遍历所有子节点
     *
     * @author chenleixing
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getNodes(Element node) {
        xmlMap.put(node.getName().toLowerCase(), node.getTextTrim());
        List<Attribute> listAttr = node.attributes(); // 当前节点的所有属性的list
        for (Attribute attr : listAttr) { // 遍历当前节点的所有属性
            String name = attr.getName(); // 属性名称
            String value = attr.getValue(); // 属性的值
            xmlMap.put(name, value.trim());
        }

// 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements(); // 所有一级子节点的list
        for (Element e : listElement) { // 遍历所有一级子节点
            XmlUtil.getNodes(e); // 递归
        }
        return xmlMap;

    }


}
