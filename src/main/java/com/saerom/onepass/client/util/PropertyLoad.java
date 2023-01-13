package com.saerom.onepass.client.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertyLoad {

	public static File getFile() {

		String fileNm = "config.properties";

		File file = new File(fileNm);
		if (!file.isFile()) {

			try {

				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				if (cl != null) {

					String path = ClassLoader.getSystemClassLoader().getResource(fileNm).getPath().replaceAll("%20",
							" ");

					file = new File(path);
				}

			} catch (Exception e) {

				e.printStackTrace();

				throw e;
			}
		}

		log.warn("config.properties path :" + file.getAbsolutePath());

		return file;
	}

	public static String getProperty(String keyName) throws Exception {

		try (FileInputStream fis = new FileInputStream(PropertyLoad.getFile());
				BufferedInputStream bis = new BufferedInputStream(fis)) {

			Properties props = new Properties();
			props.load(bis);

			return props.getProperty(keyName).trim();

		} catch (IOException e) {

			e.printStackTrace();

			throw e;
		}
	}
}
