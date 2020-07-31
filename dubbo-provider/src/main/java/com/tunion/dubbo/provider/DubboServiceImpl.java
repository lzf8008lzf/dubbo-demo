package com.tunion.dubbo.provider;

import com.tunion.cores.result.Results;
import com.tunion.dubbo.IService.IDubboService;
import com.tunion.dubbo.WelcomeAd;
import com.tunion.dubbo.pojo.Screen;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.util.Map;

/**
 * Created by Think on 2017/11/16.
 */
@DubboService
@Slf4j
public class DubboServiceImpl  implements IDubboService {

    public DubboServiceImpl()
    {
        System.err.println("---------------------------");
    }

    @Override
    public String sayHello(String name) {
        log.info(" Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());

        return "Hello " + name + ", response form provider: " + RpcContext.getContext().getLocalAddress();
    }

    @Override
    public Results queryResults(Map<String, Object> map) {

        map.forEach((key,val)->{
            log.info("key:{},value:{}",key,val);
        });

        Screen screen = new Screen();
        screen.setScreenCode("0001");
        screen.setScreenName("3DMAX厅");
        screen.setSeatNum(99);
        screen.setType("1");

        Results results = new Results("0","sucess",screen);

        return results;
    }

    @Override
    public WelcomeAd welcomeAd() {
        WelcomeAd welcomeAd =new WelcomeAd();

        welcomeAd.setType("img");
        welcomeAd.setTitle("欧派全屋定制");
        welcomeAd.setImg("https://yuexiang-video.oss-cn-beijing.aliyuncs.com/2020/06/29/16-57-2507251153116897");
        welcomeAd.setContent("https://www.oppein.cn/");
        welcomeAd.setShowUrl("url");
        welcomeAd.setDuration(3000);

        return welcomeAd;
    }
}
