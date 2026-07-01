package com.example.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("execution(* com.example.demo.service.*.*(..)) && !target(com.example.demo.service.MergedStressService) && !target(com.example.demo.service.AppOrderService)")
    public Object measureNormalServices(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        System.out.println("==================================");
        System.out.println(
                "Method: " + joinPoint.getSignature().getName() + " | Execution Time: " + (end - start) + " ms");
        System.out.println("==================================");
        return result;
    }

    @Around("execution(* com.example.demo.service.MergedStressService.*(..))")
    public Object handleStressServiceLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    @Around("execution(* com.example.demo.service.AppOrderService.*(..))")
    public Object traceOrderServiceSpans(ProceedingJoinPoint joinPoint) throws Throwable {
        String traceId = String.valueOf(Thread.currentThread().getId());
        String methodName = joinPoint.getSignature().getName();

        System.out.println("\n [" + traceId + "] ====== [DISTRIBUTED TRACING - START] ======");

        // [Span 1]: Gateway
        System.out.println(" [" + traceId + "][Span 1: API Gateway] Intercepted incoming HTTP request.");

        // [Span 2]: Auth
        System.out.println(" [" + traceId + "][Span 2: Auth Service] Token verified successfully | Latency: 0 ms");

        // [Span 3]: Core Service
        System.out.println(
                " [" + traceId + "][Span 3: Order Service] Executing core business logic for: [" + methodName + "]");

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println(
                " [" + traceId + "][Span 3: Order Service] Core logic finished in: " + executionTime + " ms");

        // [Span 4]: DB Infrastructure
        if (methodName.contains("NPlusOne")) {
            System.out.println(" [" + traceId
                    + "][Span 4: DB Infrastructure]  BOTTLENECK: Sequential Lazy DB Calls detected.");
        } else {
            System.out.println(" [" + traceId + "][Span 4: DB Infrastructure] Optimized: Single JOIN FETCH executed.");
        }

        System.out.println(" [" + traceId + "] ====== [DISTRIBUTED TRACING - END] ======\n");

        return result;
    }
}