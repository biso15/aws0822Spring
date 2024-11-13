package com.myaws.myapp.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class BoardTimeCheck {

	private static final Logger logger = LoggerFactory.getLogger(SampleAdvice.class);

	@Around("execution(* com.myaws.myapp.service.BoardService*.*(..))")  // Around : 시작과 끝에 모두 실행
	public Object timelog(ProceedingJoinPoint pjp) throws Throwable {
		
		Object result = null;
		
		logger.info("시작하는 aop");
		logger.info(Arrays.toString(pjp.getArgs()));
		long startTime = System.currentTimeMillis();  // 시스템 날짜
		
		result = pjp.proceed();
		
		long endTime = System.currentTimeMillis();
		logger.info("끝나는 aop");
		
		long durTime = endTime - startTime;
		logger.info(pjp.getSignature().getName() + " 걸린시간 : " + durTime);
		
		return result;
	}
}
