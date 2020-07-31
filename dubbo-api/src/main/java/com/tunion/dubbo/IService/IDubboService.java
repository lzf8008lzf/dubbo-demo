package com.tunion.dubbo.IService;

import com.tunion.cores.result.Results;
import com.tunion.dubbo.WelcomeAd;

import java.util.Map;

/**
 * Created by Think on 2017/11/16.
 */
public interface IDubboService {

    String sayHello(String name);

    Results queryResults(Map<String, Object> searchParams);

    WelcomeAd welcomeAd();
}
