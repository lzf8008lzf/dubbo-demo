package com.tunion.dubbo.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.tunion.cores.result.Results;
import com.tunion.dubbo.IService.IDubboService;
import com.tunion.dubbo.pojo.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by Think on 2017/11/16.
 */
@Service(version = "${demo.service.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}")
public class DubboServiceImpl  implements IDubboService {

    private static Logger logger = LoggerFactory.getLogger(DubboServiceImpl.class);

    @Override
    public String sayHello(String name) {
        logger.info(" Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());

        return "Hello " + name + ", response form provider: " + RpcContext.getContext().getLocalAddress();
    }

    @Override
    public Results queryResults(Map<String, Object> map) {

        map.forEach((key,val)->{
            logger.info("key:{},value:{}",key,val);
        });

        Screen screen = new Screen();
        screen.setScreenCode("0001");
        screen.setScreenName("3DMAX厅");
        screen.setSeatNum(99);
        screen.setType("1");

        Results results = new Results("0","sucess",screen);

        return results;
    }
}
