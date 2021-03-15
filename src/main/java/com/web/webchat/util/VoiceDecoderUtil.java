package com.web.webchat.util;
import org.nutz.lang.Encoding;
import org.nutz.lang.Lang;

import java.io.File;
import java.io.IOException;
public class VoiceDecoderUtil {

    private final static String SILK_V3_DECODER_PATH = "G://test/pcm/bin/silk_v3_decoder.exe";
    private final static String FFMPEG_PATH = "G://test/pcm/bin/ffmpeg.exe";
    private final static String SOURCE_VOICE_PATH ="G://test/pcm/bin/";
    private final static String TARGET_VOICE_PATH ="G://test/pcm/bin/";
    public static void main(String[] args){

        String skil = "G:\\test\\pcm\\bin\\test.silk";
        String pcm = "G:\\test\\pcm\\bin\\test1.pcm";
        String mp3 = "G:\\test\\pcm\\bin\\test2.mp3";

        //String pcmUrl = silkToPcm(SOURCE_VOICE_PATH+"test.silk",TARGET_VOICE_PATH+"/targetPath","666");
        //pcmToMp3(pcmUrl,TARGET_VOICE_PATH+"/mp3/test","mpe3");
//        boolean b = getPcm(skil,pcm);
//        System.out.println(b);
//        if (b)
//            getMp3(pcm,mp3);

    }
    /**
     * 解码为pcm格式
     * @param silk 源silk文件,需要绝对路径!! 例:F:\zhuanma\vg2ub41omgipvrmur1fnssd3tq.silk
     * @param pcm 目标pcm文件,需要绝对路径!! 例:F:\zhuanma\vg2ub41omgipvrmur1fnssd3tq.pcm
     * @return
     */
    public static String silkToPcm(String silk,String targetPath ,String pcm, String silkV3Path){
        String pcmUrl = targetPath+"/"+pcm+".pcm";
        File file = new File(targetPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String cmd="cmd /c "+silkV3Path+" "+silk+" "+pcmUrl+" -quiet";
        System.out.println("转码到pcm...");
        try
        {
            StringBuilder msg = Lang.execOutput(cmd, Encoding.CHARSET_GBK);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return pcmUrl;
    }

    /**
     * 转码为MP3格式
     * @param pcm 源pcm文件,需要绝对路径!!  例:F:\zhuanma\vg2ub41omgipvrmur1fnssd3tq.pcm
     * @param mp3 目标mp3文件,需要绝对路径!! 例:F:\zhuanma\vg2ub41omgipvrmur1fnssd3tq.mp3
     * @return
     */
    public static String pcmToMp3(String pcm,String targetPath ,String mp3,String ffmpegPath){
        String mp3Url = targetPath+"/"+mp3+".mp3";
        File file = new File(targetPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.println("转码到mp3...");
        try {
            StringBuilder sb = Lang.execOutput("cmd /c "+ ffmpegPath+" -y -f s16le -ar 24000 -ac 1 -i "+pcm+" "+mp3Url+"", Encoding.CHARSET_GBK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mp3Url;
    }
}
