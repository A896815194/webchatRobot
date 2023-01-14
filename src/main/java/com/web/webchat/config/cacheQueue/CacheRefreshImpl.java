//package com.web.webchat.config.cacheQueue;
//
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import com.thunisoft.common.utils.FastJsonUtils;
//import com.thunisoft.pzgl.config.Receiver;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.*;
//
///**
// * <p>Title:CacheRefreshImpl.java </p>
// * <p>Description: 在mq组件删除后，模拟mq消费逻辑创建一个阻塞队列将原发送的消息通过该队列进行消费 </p>
// * <p>Company: </p>
// *
// * @author jiangshuai
// * @date 2022年06月23日
// */
//public class CacheRefreshImpl implements CacheRefreshService {
//
//    @Autowired
//    private Receiver receiver;
//
//    //日志输出
//    private final static Logger logger = LoggerFactory.getLogger(CacheRefreshImpl.class);
//    // 消费缓存事件的队列
//    private BlockingDeque<TSysCacheEvent> sysCacheEventsQueue;
//    // 防止消息重复的集合
//    private Set<TSysCacheEvent> cacheEventsMessageSet;
//    // 获取消息事件的事件map
//    private Map<TSysCacheEvent, Long> messageReceiveTimeMillisMap;
//
//    private static final int MAX_HANDLER = 4;
//
//    private static final int MAX_CHECK_TIMES = 5;
//
//
//    public void init() {
//        logger.info("缓存队列bean 加载完毕，初始化线程池");
//        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
//                .setNameFormat(this.getClass()
//                        .getSimpleName() + "-").build();
//
//        cacheEventsMessageSet = Collections.synchronizedSet(new HashSet<>());
//        messageReceiveTimeMillisMap = new ConcurrentHashMap<>();
//        sysCacheEventsQueue = new LinkedBlockingDeque<>();
//        //Common Thread Pool
//        ExecutorService executor = new ThreadPoolExecutor(MAX_HANDLER, MAX_HANDLER,
//                0L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//        for (int i = 0; i < MAX_HANDLER; i++) {
//            executor.submit(new CacheMessageHandler());
//        }
//    }
//
//    // 阻塞获取消息
//    private TSysCacheEvent blokingTakeMessage() throws InterruptedException {
//        return sysCacheEventsQueue.take();
//    }
//
//    public boolean addMessage(final TSysCacheEvent message) {
//        //DJSTODO 这种判断并不能真正避免重复生成，1来可能做集群，二来可能在处理中的时候，这个queue里的内容就移除了
//        if (!sysCacheEventsQueue.contains(message) && !cacheEventsMessageSet.contains(message)) {
//            logger.info(message + "加入刷新缓存入列");
//            sysCacheEventsQueue.add(message);
//            messageReceiveTimeMillisMap.put(message, System.currentTimeMillis());
//            return true;
//        }
//        return false;
//    }
//
//
//    // 缓存消息处理线程
//    private class CacheMessageHandler implements Runnable {
//
//        @Override
//        public void run() {
//            while (true) {
//                TSysCacheEvent message = null;
//                try {
//                    message = blokingTakeMessage();
//                    cacheEventsMessageSet.add(message);
//                    if (null != message) {
//                        logger.info(Thread.currentThread().getName() + " 接收到来自sysCacheEventsQueue队列的消息："
//                                + FastJsonUtils.toJSONString(message, false));
//                        int checkTimes = 0;
//                        boolean isNeedReCheck = true;
//                        while (isNeedReCheck && checkTimes < MAX_CHECK_TIMES) {
//                            isNeedReCheck = receiver.checkAllCache();
//                            Long receiveTimeMillis = messageReceiveTimeMillisMap.remove(message);
//                            if (null != receiveTimeMillis) {
//                                logger.info("缓存队列消费-【" + message + "】" + "从接收消息到处理完毕，间隔【"
//                                        + (System.currentTimeMillis() - receiveTimeMillis) + "】ms");
//                            }
//                            checkTimes += 1;
//                        }
//                    }
//                } catch (Throwable t) {
//                    logger.error("缓存队列消费异常", t);
//                } finally {
//                    try {
//                        cacheEventsMessageSet.remove(message);
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        logger.warn("缓存队列消费出现警告", e);
//                    }
//                }
//            }
//        }
//
//    }
//}
