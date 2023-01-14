/**
 * @projectName gh-elearning-gateway
 * @package com.gold.test
 * @className com.gold.test.ScheduledTask
 * @copyright Copyright 2023 Thunisoft, Inc All rights reserved.
 */
package com.web.webchat.config.cronTask;

import java.util.concurrent.ScheduledFuture;

/**
 * ScheduledTask
 *
 * @author Administrator
 * @version TODO
 * @description
 * @date 2023/1/5 16:49
 */
public class ScheduledTask {

    volatile ScheduledFuture<?> future;

    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}