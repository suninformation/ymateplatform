/*
 * Copyright 2007-2107 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.platform.configuration.provider.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.configuration.ConfigurationLoadException;
import net.ymate.platform.configuration.IConfiguration;
import net.ymate.platform.configuration.provider.IConfigurationProvider;

import org.apache.commons.lang.StringUtils;
import org.jconfig.Configuration;
import org.jconfig.ConfigurationManager;
import org.jconfig.ConfigurationManagerException;
import org.jconfig.handler.XMLFileHandler;



/**
 * <p>
 * JConfigProvider
 * </p>
 * <p>
 * 基于JConfig开源配置框架实现的配置提供者对象；
 * </p>
 * 
 * @author 刘镇(suninformation@163.com)
 * @version 0.0.0
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th><th width="100px">动作</th><th
 *          width="100px">修改人</th><th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>刘镇</td>
 *          <td>2010-4-17下午02:34:44</td>
 *          </tr>
 *          </table>
 */
public class JConfigProvider implements IConfigurationProvider {

	/**
	 * 配置对象缓存，对于重复的文件加载会使用缓存，减少文件读写频率
	 */
	private static final Map<String, Configuration> __CONFIG_MAPS = new HashMap<String, Configuration>();

	/**
	 * 基于JConfig的配置对象
	 */
	private Configuration config;

	/**
	 * 装载配置文件参数
	 */
	private String cfgFileName;

	/**
	 * 构造器
	 */
	public JConfigProvider() {
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfigurationProvider#getString(java.lang.String)
	 */
	public String getString(String key) {
		return getString(key, "");
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfigurationProvider#getString(java.lang.String, java.lang.String)
	 */
	public String getString(String key, String defaultValue) {
		String[] keys = StringUtils.split(key, IConfiguration.CFG_KEY_SEPERATE);
		int keysSize = keys.length;
		if (keysSize == 1) {
			return config.getProperty(keys[0], defaultValue);
		} else if (keysSize == 2) {
			return config.getProperty(keys[1], defaultValue, keys[0]);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfigurationProvider#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfigurationProvider#getBoolean(java.lang.String, boolean)
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		String[] keys = StringUtils.split(key, IConfiguration.CFG_KEY_SEPERATE);
		int keysSize = keys.length;
		if (keysSize == 1) {
			return config.getBooleanProperty(keys[0], defaultValue);
		} else if (keysSize == 2) {
			return config.getBooleanProperty(keys[1], defaultValue, keys[0]);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.configuration.IConfigurationProvider#getCfgsMap()
	 */
	public Map<String, String> getCfgsMap() {
		Map<String, String> cfgsMap = new HashMap<String, String>();
		String[] categoryNames = config.getCategoryNames();
		if (categoryNames != null && categoryNames.length > 0) {
			for (String categoryName : categoryNames) {
				String[] propertyNames = config.getPropertyNames(categoryName);
				if (propertyNames != null && propertyNames.length > 0) {
					for (String propertyName : propertyNames) {
						cfgsMap.put(categoryName + IConfiguration.CFG_KEY_SEPERATE + propertyName, config.getProperty(propertyName, "", categoryName));
					}
				}
			}
		}
		return cfgsMap;
	}
	
	public double getDouble(String key) {
		return getDouble(key, Double.MIN_VALUE);
	}

	public double getDouble(String key, double defaultValue) {
		String[] keys = StringUtils.split(key, IConfiguration.CFG_KEY_SEPERATE);
		int keysSize = keys.length;
		if (keysSize == 1) {
			return config.getDoubleProperty(keys[0], defaultValue);
		} else if (keysSize == 2) {
			return config.getDoubleProperty(keys[1], defaultValue, keys[0]);
		}
		return defaultValue;
	}

	public float getFloat(String key) {
		return getFloat(key, Float.MIN_VALUE);
	}
	
	public float getFloat(String key, float defaultValue) {
		return new Double(getDouble(key, defaultValue)).floatValue();
	}

	public int getInt(String key) {
		return getInt(key, Integer.MIN_VALUE);
	}
	
	public int getInt(String key, int defaultValue) {
		String[] keys = StringUtils.split(key, IConfiguration.CFG_KEY_SEPERATE);
		int keysSize = keys.length;
		if (keysSize == 1) {
			return config.getIntProperty(keys[0], defaultValue);
		} else if (keysSize == 2) {
			return config.getIntProperty(keys[1], defaultValue, keys[0]);
		}
		return defaultValue;
	}

	public List<String> getList(String key) {
		List<String> valueList = new ArrayList<String>();
		String[] keys = StringUtils.split(key, IConfiguration.CFG_KEY_SEPERATE);
		int keysSize = keys.length;
		Properties properties = null;
		if (keysSize == 1) {
			properties = config.getProperties();
		} else if (keysSize == 2) {
			properties = config.getProperties(keys[0]);
		}
		if (properties != null && !properties.isEmpty()) {
			for (Object name : properties.keySet()) {
				if (name != null && name.toString().startsWith(keysSize == 1 ? keys[0] : keys[1])) { // 以给定key开头的键值对
					Object value = properties.get(name);
					valueList.add(value == null ? "" : value.toString());
				}
			}
		}
		return valueList;
	}

	public Map<String, String> getMap(String keyHead) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(keyHead)) {
			return map;
		}
		String[] keys = StringUtils.split(keyHead, IConfiguration.CFG_KEY_SEPERATE);
		int keysSize = keys.length;
		Properties properties = null;
		// 当使用了"|"分级，那么就使用索引为0的键进行获取所有的配置项
		if (keysSize == 1) {
			properties = config.getProperties();
		} else if (keysSize == 2) {
			properties = config.getProperties(keys[0]);
		}
		int headLength = keysSize == 1 ? keys[0].length() : keys[1].length();
		if (properties != null && !properties.isEmpty()) {
			for (Object name : properties.keySet()) {
				if (name != null && name.toString().startsWith(keysSize == 1 ? keys[0] : keys[1])) {
					String key = name.toString();
					Object value = properties.get(name);
					map.put(key.substring(headLength), value.toString());
				}
			}
		}
		return map;
	}

	public String getCfgFileName() {
		return cfgFileName;
	}

	public long getLong(String key) {
		return getLong(key, Long.MIN_VALUE);
	}
	
	public long getLong(String key, long defaultValue) {
		String[] keys = StringUtils.split(key, IConfiguration.CFG_KEY_SEPERATE);
		int keysSize = keys.length;
		if (keysSize == 1) {
			return config.getLongProperty(keys[0], defaultValue);
		} else if (keysSize == 2) {
			return config.getLongProperty(keys[1], defaultValue, keys[0]);
		}
		return defaultValue;
	}

	public boolean contains(String key) {
		String[] keys = StringUtils.split(key, IConfiguration.CFG_KEY_SEPERATE);
		int keysSize = keys.length;
		if (keysSize == 1) {
			return config.getProperty(keys[0], null) != null;
		} else if (keysSize == 2) {
			return config.getProperty(keys[1], null, keys[0]) != null;
		}
		return false;
	}

	public void load(String cfgFileName) throws ConfigurationLoadException {
		if (StringUtils.isBlank(cfgFileName)) {
			throw new ConfigurationLoadException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.configuration.load_config_error", ""));
		}
		config = __CONFIG_MAPS.get(cfgFileName);
		if (config == null) {
            this.cfgFileName = cfgFileName;
			// 注：基于JConfig的配置文件只能是存放在可以通过物理路径获取到的文件，（即非jar包中资源）；
			URL _url = FileUtils.toURL(cfgFileName);
			File file = FileUtils.toFile(_url);
			if (file == null) {
				throw new ConfigurationLoadException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.configuration.load_config_error", cfgFileName));
			}
			// 配置文件也无需实时加载，停掉JConfig的文件检查，节省线程开销；
			System.setProperty("jconfig.filewatcher", "false");
			try {
				ConfigurationManager.getInstance().load(new XMLFileHandler(file.getAbsolutePath()), IConfiguration.DEFAULT_CFG_CATEGORY_NAME);
				config = ConfigurationManager.getConfiguration(IConfiguration.DEFAULT_CFG_CATEGORY_NAME);
				// INFO: "平台已成功加载配置文件"
				__CONFIG_MAPS.put(cfgFileName, config);
			} catch (ConfigurationManagerException e) {
				throw new ConfigurationLoadException(RuntimeUtils.unwrapThrow(e));
			}
		}
	}

	public void reload() throws ConfigurationLoadException {
		if (StringUtils.isNotBlank(this.cfgFileName)) {
			// 移除缓存项
			__CONFIG_MAPS.remove(this.cfgFileName);
			// 加载配置
			load(this.cfgFileName);
		}
	}
	
	public String[] getArray(String key) {
		return getArray(key, true);
	}
	
	public String[] getArray(String key, boolean zeroSize) {
		String[] keys = StringUtils.split(key, IConfiguration.CFG_KEY_SEPERATE);
		int keysSize = keys.length;
		if (keysSize == 1) {
			return config.getArray(keys[0], zeroSize ? new String[] {} : null);
		} else if (keysSize == 2) {
			return config.getArray(keys[1], zeroSize ? new String[] {} : null, keys[0]);
		}
		return zeroSize ? new String[] {} : null;
	}

}
