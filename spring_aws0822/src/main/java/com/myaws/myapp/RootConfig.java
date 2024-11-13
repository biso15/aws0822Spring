//package com.myin.noxml;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.stereotype.Controller;
//
//import com.myin.noxml.controller.MemberController;
//
//// 1. DB연결
//// 2. 마이바티스 연동
//// 3. 각 빈들을 찾을 검색범위를 지정하겠다. Servlet용도는 제외한다
//
//// RootConfig를 환경설정용도로 객체생성요청  rootConfig라는 id로 사용. 클래스와 id가 같으면 생략가능
//@Configuration
//@ComponentScan(basePackages = { "com.myin.noxml" }, 
//excludeFilters = { @ComponentScan.Filter (type = FilterType.ANNOTATION, value = Controller.class)})
//public class RootConfig {
//	
//	private String url = "jdbc:log4jdbc:oracle:thin://@127.0.0.1:1521:XE";
//	private String username = "sys as sysdba";
//	private String password = "1111";
//
//	@Bean
//	public DriverManagerDataSource db() {
//		DriverManagerDataSource dmb = new DriverManagerDataSource(url, username, password);
//		dmb.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
//		return dmb;
//	}
//
//	@Bean
//	public DataSourceTransactionManager transactionManager() {
//		DataSourceTransactionManager dt = new DataSourceTransactionManager();
//		dt.setDataSource(db());
//		return dt;
//	}
//
//	@Bean	
//	public  SqlSessionFactory  sqlSessionFactory() throws Exception {
//	SqlSessionFactoryBean sfb = new SqlSessionFactoryBean();
//	sfb.setDataSource(db());
//	sfb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("/mappers/*.xml"));
//	sfb.setConfigLocation(new ClassPathResource("/config/mybatis_config.xml"));	
//
//	return (SqlSessionFactory)sfb.getObject();
//	}
//	
//	@Bean
//	public SqlSessionTemplate sqlSession() throws Exception {
//		SqlSessionTemplate st = new SqlSessionTemplate(sqlSessionFactory());	
//	return st;
//	}
//	
//}
