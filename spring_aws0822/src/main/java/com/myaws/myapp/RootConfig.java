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
//// 1. DB����
//// 2. ���̹�Ƽ�� ����
//// 3. �� ����� ã�� �˻������� �����ϰڴ�. Servlet�뵵�� �����Ѵ�
//
//// RootConfig�� ȯ�漳���뵵�� ��ü������û  rootConfig��� id�� ���. Ŭ������ id�� ������ ��������
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
