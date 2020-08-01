package com.tunion.dubbo.consumer;


import com.tunion.cores.result.Results;
import com.tunion.cores.utils.JacksonUtil;
import com.tunion.dubbo.IService.IDubboService;
import com.tunion.dubbo.WelcomeAd;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class DubboConsumer {

    private static Logger logger = LoggerFactory.getLogger(DubboConsumer.class);

    @DubboReference(filter={"dubboTraceIdFilter"})
    private IDubboService consumerService;

    public void sayHello() {
        String retStr = consumerService.sayHello("JeffLee");
        logger.info(retStr);
    }

    public void queryResults(){
        Map<String, Object> searchParams = new LinkedHashMap();
        Results results = consumerService.queryResults(searchParams);

        String retStr= JacksonUtil.toJson(results);
        logger.info(retStr);
    }

    public WelcomeAd welcomeAd()
    {
        return consumerService.welcomeAd();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(new String[] {"dubbo-consumer.xml"});

        context.start();

        logger.error("------------------------------------------------------------------------------------------------");
        IDubboService dubboService=(IDubboService) context.getBean("consumerService");

        String test=dubboService.sayHello("ttttttttttt");

        logger.error(test);

    }
}
