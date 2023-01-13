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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MapperScan(value = "com.daesang.rpa.repository.oracle", sqlSessionFactoryRef = "groupwareSqlSessionFactory")
@Configuration
public class GroupwareDataSourceConfig {

	@Bean
	@Qualifier("groupwareConfig")
	@ConfigurationProperties(prefix = "spring.datasource.groupware")
	public HikariConfig groupwareConfig() {
		return new HikariConfig();
	}

	@Bean
	@Qualifier("groupwareDataSource")
	public DataSource groupwareDataSource() throws Exception {
		return new HikariDataSource(groupwareConfig());
	}

	@Bean(name = "groupwareSqlSessionFactory")
	public SqlSessionFactory groupwareSqlSessionFactory(@Qualifier("groupwareDataSource") DataSource dataSource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
		sqlSessionFactoryBean
				.setMapperLocations(applicationContext.getResources("classpath:mapper/oracle/GroupwareMapper.xml"));

		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "groupwareSqlSessionTemplate")
	public SqlSessionTemplate groupwareSqlSessionTemplate(
			@Qualifier("groupwareSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
