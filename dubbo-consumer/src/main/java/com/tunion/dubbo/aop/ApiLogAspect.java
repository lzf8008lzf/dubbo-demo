package com.tunion.dubbo.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


/**
 * ApiController，切面处理类
 *
 * @author zcc
 */
@Aspect
//@Component
@Slf4j
public class ApiLogAspect {

    // 设置切点
    @Pointcut("execution(* com.tunion.dubbo.controller..*Controller.*(..))")
    public void apiLogPointCut() {

    }

    @Around("apiLogPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //执行具体方法
        Object result = null;
		StopWatch clock = new StopWatch();
		clock.start(); //计时开始
		try {
			result = point.proceed();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}finally {
			clock.stop(); //计时结束
			if (log.isInfoEnabled()) {
				log.info("调用方法:[{}],执行时间:[{}ms]",point.getSignature().getName(),clock.getTotalTimeMillis());
			}
		}

        return result;
    }

}
