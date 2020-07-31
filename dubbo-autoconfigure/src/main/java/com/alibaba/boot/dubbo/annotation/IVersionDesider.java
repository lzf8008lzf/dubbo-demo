package org.apache.boot.dubbo.annotation;

import org.apache.dubbo.config.ReferenceConfig;

public interface IVersionDesider {
    public String desideVersion(ReferenceConfig referenceConfig);
}
