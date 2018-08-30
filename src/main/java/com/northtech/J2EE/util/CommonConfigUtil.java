package com.northtech.J2EE.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


public class CommonConfigUtil {
	private static final Logger logger = LoggerFactory.getLogger(CommonConfigUtil.class);
	private static Configuration configs;
	public  static String  YUNPIAN_SECRET;// 云片的应用密钥



	public static synchronized void init(String filePath) {
		if (configs != null) {
			return;
		}
		try {
			configs = new PropertiesConfiguration(filePath);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

		if (configs == null) {
			throw new IllegalStateException("can`t find file by path:"
					+ filePath);
		}

		YUNPIAN_SECRET = configs.getString("YUNPIAN_SECRET");

	}

}