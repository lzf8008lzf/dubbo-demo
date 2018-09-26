package com.alibaba.boot.dubbo.annotation;

import com.alibaba.dubbo.config.ReferenceConfig;

public interface IVersionDesider {
    public String desideVersion(ReferenceConfig referenceConfig);
}
