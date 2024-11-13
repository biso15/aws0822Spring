package com.myaws.myapp.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
// @Slf4j  // 롬복 라이브러리 추가해야 사용 가능
public class SampleAdvice {

	private static final Logger logger = LoggerFactory.getLogger(SampleAdvice.class);
	
	@Before("execution(* com.myaws.myapp.service.BoardService*.*(..))")
	public void startLog() {
		logger.info("----------------------------------");
		logger.info("aop 로그 테스트중입니다.");
		logger.info("----------------------------------");
		// System.out.println("테스트");
	}
}
