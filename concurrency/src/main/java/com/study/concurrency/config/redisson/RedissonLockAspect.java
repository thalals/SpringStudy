package com.study.concurrency.config.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Order(1)   //@Transactional 보다 먼저 하기 위해
@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.study.concurrency.config.redisson.RedissonLock)")
    public Object redissonLock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock annotation = method.getAnnotation(RedissonLock.class);

        String lockKey = method.getName() + getDynamicValue(signature.getParameterNames(), joinPoint.getArgs());

        //key 로 Lock 객체 가져옴
        RLock lock = redissonClient.getLock(lockKey);

        try {
            //획득시도 시간, 락 점유 시간
            boolean lockable = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), TimeUnit.MILLISECONDS);
            if (!lockable) {
                log.error("Lock 획득 실패={}", lockKey);
                return false;
            }
            log.info("Redisson Lock 획득");

            return aopForTransaction.proceed(joinPoint);

        } catch (InterruptedException e) {
            log.error("Redisson 락 점유 에러");
            throw e;
        } finally {
            log.info("락 해제");
            lock.unlock();
        }
    }

    private String getDynamicValue(String[] parameterNames, Object[] args) {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < parameterNames.length; i++) {
            stringBuilder.append("-").append(parameterNames[i]).append(":").append(args[i]);
        }

        return stringBuilder.toString();
    }
}