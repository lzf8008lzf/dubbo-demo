package com.tunion.dubbo;

import com.tunion.dubbo.consumer.DubboConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConsumerStartApplication.class})
@AutoConfigureMockMvc
public class DoubboConsumerTest {

    private static Logger logger = LoggerFactory.getLogger(DubboConsumer.class);

    @Autowired
    private DubboConsumer doubboConsumer;

    @Test
    public void sayHello()
    {
        doubboConsumer.sayHello();
    }

    @Test
    public void queryResults()
    {
        doubboConsumer.queryResults();
    }
}
