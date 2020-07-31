/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.boot.dubbo.annotation;

import org.apache.dubbo.config.spring.context.annotation.DubboConfigBindingRegistrar;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfigBinding;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Dubbo Config Binding {@link BeanPostProcessor}
 *
 * @see EnableDubboConfigBinding
 * @see DubboConfigBindingRegistrar
 * @since 2.5.8
 */

public class DubboConfigBindingBeanPostProcessor extends org.apache.dubbo.config.spring.beans.factory.annotation.DubboConfigBindingBeanPostProcessor {
    public DubboConfigBindingBeanPostProcessor(String prefix, String beanName) {
        super(prefix, beanName);
    }
}
