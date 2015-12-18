package com.education.mock;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Moco;
import com.github.dreamhead.moco.MocoJsonRunner;
import com.github.dreamhead.moco.Runner;
import com.github.dreamhead.moco.resource.Content;
import org.junit.Test;
import org.junit.runner.Request;

/**
 * Created by yzzhao on 12/18/15.
 */
public class WeChatMockServer {

    @Test
    public void testWeChatMockServer(){
        HttpServer httpServer = MocoJsonRunner.jsonHttpServer(7777,
                Moco.file("src/test/resources/com/education/mock/wechatmock.json"));
        Runner runner = Runner.runner(httpServer);
        runner.start();
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
