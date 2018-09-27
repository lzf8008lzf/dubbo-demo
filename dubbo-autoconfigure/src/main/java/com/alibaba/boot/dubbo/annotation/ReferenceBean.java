package com.alibaba.boot.dubbo.annotation;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.spring.extension.SpringExtensionFactory;
import com.alibaba.dubbo.config.support.Parameter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class ReferenceBean<T> extends com.alibaba.dubbo.config.spring.ReferenceBean<T> implements Cloneable{

    private static Logger logger = LoggerFactory.getLogger(ReferenceBean.class);

    private static final long serialVersionUID = 213195494150089726L;

    private transient T proxy;//代理

    private transient IVersionDesider versionDesider;

    private transient ReferenceBean self_=this;

    private transient ConcurrentHashMap<String,Object> versionMap=new ConcurrentHashMap<String,Object>();

    private transient final String DEFAULT_VERSION="1.0.0";

    private transient ApplicationContext applicationContext;

    public ReferenceBean() {
        super();
    }

    public ReferenceBean(Reference reference) {
        super(reference);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        SpringExtensionFactory.addApplicationContext(applicationContext);
    }

    @Override
    public Class<?> getObjectType() {
        return getInterfaceClass();
    }

    @Override
    @Parameter(excluded = true)
    public boolean isSingleton() {
        return true;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        try {
            versionDesider = applicationContext.getBean(IVersionDesider.class);
        }catch (Exception e)
        {
            versionDesider = null;
        }

        proxy = getVersionDecideProxy();//创建代理对象
    }

    @SuppressWarnings({"all"})
    private T getVersionDecideProxy() throws ClassNotFoundException,InstantiationException,IllegalAccessException{
        ProxyFactory proxyFactory = new ProxyFactory();
        Class[] interfaces={Class.forName(this.getInterface())};
        proxyFactory.setInterfaces(interfaces);

        //创建代理类型的Class
        Class<ProxyObject> proxyObjectClass = proxyFactory.createClass();
        T proxy = (T) proxyObjectClass.newInstance();
        ((ProxyObject)proxy).setHandler(new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                T target = null;
                String version=DEFAULT_VERSION;
                if(versionDesider!=null) {
                    version = versionDesider.desideVersion(self_);
                }
                {
                    logger.error("versionDesider is null!");
                }

                if(version==null) version = DEFAULT_VERSION;

                String keyVersion=self_.getId()+version;
                ReferenceConfig config=null;

                if(versionMap.get(keyVersion)!=null)
                {
                    config=(ReferenceConfig)versionMap.get(keyVersion);
                    target = (T)config.get();
                    T retObj = (T)thisMethod.invoke(target,args);

                    return retObj;
                }

                synchronized (self_){
                    if(config==null)
                    {
                        config=(ReferenceConfig)self_.clone();
                        config.setVersion(version);
                        config.setCheck(false);

                        versionMap.put(keyVersion,config);
                    }
                }

                target = (T)config.get();
                T retObj = (T)thisMethod.invoke(target,args);

                return retObj;
            }
        });

        return proxy;
    }

    @Override
    public Object getObject() throws Exception {
        return proxy;
    }

}