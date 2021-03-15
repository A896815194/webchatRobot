package com.web.webchat.util;

import java.io.File;

public class FfmpegUtil {
    /**
     * 8000K采样频率
     */
    public static final int HZ_8K = 8000;
    /**
     * 16000K采样频率
     */
    public static final int HZ_16K = 16000;
    /**
     * 音频后缀
     */
    public static final String AUDIO_SUFFIX_WEBM = "webm";
    public static final String AUDIO_SUFFIX_WAV = "wav";
    public static final String AUDIO_SUFFIX_PCM = "pcm";

    public static void changeAmrToMp3(String sourcePath, String targetPath) throws Exception {

        String webroot = "d:\\ffmpeg\\bin";

        Runtime run = null;

        try {

            run = Runtime.getRuntime();

            long start=System.currentTimeMillis();

            System.out.println(new File(webroot).getAbsolutePath());

            //执行ffmpeg.exe,前面是ffmpeg.exe的地址，中间是需要转换的文件地址，后面是转换后的文件地址。-i是转换方式，意思是可编码解码，mp3编码方式采用的是libmp3lame

            //wav转pcm

            //Process p=run.exec(new File(webroot).getAbsolutePath()+"/ffmpeg -y -i "+sourcePath+" -acodec pcm_s16le -f s16le -ac 1 -ar 16000 "+targetPath);

            //mp3转pcm

            Process p=run.exec(new File(webroot).getAbsolutePath()+"/ffmpeg -y -i "+sourcePath+" -ar 44100 -ac 2 -acodec mp3 "+targetPath);

            //释放进程

            p.getOutputStream().close();

            p.getInputStream().close();

            p.getErrorStream().close();

            p.waitFor();

            long end=System.currentTimeMillis();

            System.out.println(sourcePath+" convert success, costs:"+(end-start)+"ms");

        } catch (Exception e) {

            e.printStackTrace();

        }finally{

            //run调用lame解码器最后释放内存

            run.freeMemory();

        }

    }

}
