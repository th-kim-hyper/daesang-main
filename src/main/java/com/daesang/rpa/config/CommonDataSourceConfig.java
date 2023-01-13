package com.daesang.rpa.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MapperScan(value = "com.daesang.rpa.repository.mariadb", sqlSessionFactoryRef = "commonSqlSessionFactory")
@Configuration
public class CommonDataSourceConfig {

	@Primary
	@Bean
	@Qualifier("commonConfig")
	@ConfigurationProperties(prefix = "spring.datasource.common")
	public HikariConfig commonConfig() {
		return new HikariConfig();
	}

	@Primary
	@Bean
	@Qualifier("commonDataSource")
	public DataSource commonDataSource() throws Exception {
		return new HikariDataSource(commonConfig());
	}

	@Primary
	@Bean(name = "commonSqlSessionFactory")
	public SqlSessionFactory commonSqlSessionFactory(@Qualifier("commonDataSource") DataSource dataSource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/mariadb/CommonMapper.xml"));

		return sqlSessionFactoryBean.getObject();
	}

	@Primary
	@Bean(name = "commonSqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(
			@Qualifier("commonSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
