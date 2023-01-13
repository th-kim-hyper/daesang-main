package com.daesang.rpa.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EncryptConfig {

	@Bean(name = "encryptorBean")
	public StringEncryptor encryptor() {

		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");

		config.setPassword("rpa!23");

		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setConfig(config);

		encryptor.setProvider(new BouncyCastleProvider());

		log.debug(encryptor.encrypt("Superadmin!23"));
		log.debug(encryptor.encrypt("rpa2022user"));

		return encryptor;
	}
}