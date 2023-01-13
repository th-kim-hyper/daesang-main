package com.saerom.onepass.cipher.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertyLoad {

	private File file;

	public PropertyLoad() {

		this.file = getFile("eMateOnepassCrypto.properties");
	}

	public PropertyLoad(String fileNm) {

		this.file = getFile(fileNm);
	}

	private File getFile(String fileNm) {

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

		log.warn(fileNm + " path :" + file.getAbsolutePath());

		return file;
	}

	public String getProperty(String keyName) throws Exception {

		try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis)) {

			Properties props = new Properties();
			props.load(bis);

			return props.getProperty(keyName).trim();

		} catch (IOException e) {

			e.printStackTrace();

			throw e;
		}
	}
}
