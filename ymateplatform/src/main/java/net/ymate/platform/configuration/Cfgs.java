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
package net.ymate.platform.configuration;

import java.io.File;
import java.net.URL;
import java.security.AccessControlException;

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.ResourceUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.commons.util.SystemEnvUtils;
import net.ymate.platform.configuration.annotation.Configuration;
import net.ymate.platform.configuration.provider.IConfigurationProvider;

import org.apache.commons.lang.StringUtils;


/**
 * <p>
 * Cfgs
 * </p>
 * <p>
 * 配置管理类，用于管理已成功加载的IConfig配置实例对象；
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
 *          <td>2011-8-27上午02:17:46</td>
 *          </tr>
 *          </table>
 */
public class Cfgs {

	/**
	 * 当前配置体系框架初始化配置对象
	 */
	private static ICfgConfig __CFG_CONFIG;

	private static boolean __IS_INITED;

	private static String __CONFIG_HOME;
	private static String __PROJECT_HOME;
	private static String __MODULE_HOME;

	private static String __USER_HOME;
	private static String __USER_DIR;

	/**
	 * 初始化配置体系管理器
	 * 
	 * @param config
	 * @throws ConfigurationInitializeException
	 */
	public static void initialize(ICfgConfig config) throws ConfigurationInitializeException {
		System.out.println(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.configuration.module_init"));
		if (config == null) {
			throw new ConfigurationInitializeException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.configuration.module_init_parameter_null"));
		}
		__CFG_CONFIG = config;
		__CONFIG_HOME = config.getConfigHome();
		if (StringUtils.isBlank(config.getConfigHome())) {
			// 尝试通过运行时变量或系统变量获取CONFIG_HOME参数
			__CONFIG_HOME = System.getenv("CONFIG_HOME");
			if (StringUtils.isBlank(__CONFIG_HOME)) {
				__CONFIG_HOME = SystemEnvUtils.getSystemEnv("CONFIG_HOME");
			}
		}
		//
		if (StringUtils.isNotBlank(__CONFIG_HOME)) {
			File _configHomeFile = new File(__CONFIG_HOME);
			if (!_configHomeFile.exists() || !_configHomeFile.isDirectory()) {
				throw new ConfigurationInitializeException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.configuration.config_home_invalid"));
			}
			__CONFIG_HOME = FileUtils.fixSeparator(_configHomeFile.getPath());
			System.setProperty("user.dir", __CONFIG_HOME);
			// 在配置体系主目录（configHome）存在的情况下，处理项目主目录
			if (StringUtils.isNotBlank(config.getProjectName())) {
				__PROJECT_HOME = FileUtils.fixSeparator(new File(__CONFIG_HOME + ICfgConfig.PROJECTS_FORLDER_NAME + File.separator + config.getProjectName()).getPath());
				System.setProperty("user.dir", __PROJECT_HOME);
				// 在项目主目录（projectHome）存在的情况下，处理模块主目录
				if (StringUtils.isNotBlank(config.getModuleName())) {
					__MODULE_HOME = FileUtils.fixSeparator(new File(__PROJECT_HOME + ICfgConfig.MODULES_FORLDER_NAME + File.separator + config.getModuleName()).getPath());
					System.setProperty("user.dir", __MODULE_HOME);
				}
			}
			//
			StringBuilder _conf = new StringBuilder("{\n  ");
			_conf.append("CONFIG_HOME").append("=").append(__CONFIG_HOME);
			//
			__USER_HOME = System.getProperty("user.home", "");
			if (StringUtils.isNotBlank(__USER_HOME)) {
				__USER_HOME = FileUtils.fixSeparator(__USER_HOME);
			}
			//
			_conf.append(", \n  ").append("USER_HOME").append("=").append(__USER_HOME);
			//
			__USER_DIR = System.getProperty("user.dir", "");
			if (StringUtils.isNotBlank(__USER_DIR)) {
				__USER_DIR = FileUtils.fixSeparator(__USER_DIR);
			}
			__IS_INITED = true;
			//
			_conf.append(", \n  ").append("USER_DIR").append("=").append(__USER_DIR).append("\n}");
			//
			System.out.println(_conf.toString());
			System.out.println(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.configuration.module_init_final"));
		} else {
			throw new ConfigurationInitializeException(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.configuration.config_home_invalid"));
		}
	}

	/**
	 * @return 获取当前配置体系框架初始化配置对象
	 */
	public static ICfgConfig getConfig() {
		return __CFG_CONFIG;
	}

	/**
	 * @return 判断是否已初始化完成
	 */
	public static boolean isInited() {
		return __IS_INITED;
	}

	/**
	 * 智能搜索配置文件真实资源路径，先在配置体系中查找，再到项目 CLASSPATH 路径中查找，若 cfgFile 以 "jar:" 开头则直接返回
	 * 
	 * @param cfgFile 配置文件相对路径及名称
	 * @return 配置文件真实路径
	 */
	public static String smartSearch(String cfgFile) {
		if (StringUtils.isBlank(cfgFile)) {
			return null;
		}
		File _targetFile = search(cfgFile);
		if (_targetFile == null) {
			if (cfgFile.startsWith("jar:")) {
	    		return cfgFile;
			}
			URL _targetFileURL = ResourceUtils.getResource(cfgFile, Cfgs.class);
	    	if (_targetFileURL != null) {
	    		_targetFile = FileUtils.toFile(_targetFileURL);
	    		return _targetFile == null ? _targetFileURL.toString() : _targetFile.getPath();
	    	}
		} else {
			return _targetFile.getPath();
		}
		return null;
	}

	/**
	 * 按照模块路径->项目路径->主路径(CONFIG_HOME)->用户路径(user.dir)->系统用户路径(user.home)的顺序寻找指定文件
	 *
	 * @param cfgFile 配置文件路径及名称
	 * @return 找到的文件File对象，只要找到存在的File，立即停止寻找并返回当前File实例
	 */
	public static File search(String cfgFile) {
		if (StringUtils.isBlank(cfgFile)) {
			return null;
		}
		// 若指定的 cfgFile 为文件绝对路径名，则直接返回
		File _result = new File(cfgFile);
		if (_result.isAbsolute()) {
			return _result;
		}
		// 到 moduleHome(模块路径)路径中去寻找 cfgFile 指定的文件
		if (StringUtils.isNotBlank(__MODULE_HOME)) {
			_result = new File(__MODULE_HOME + cfgFile);
			if (_result.canRead() && _result.isAbsolute() && _result.exists()) {
				return _result;
			}
		}
		// 到 projectHome(项目路径)路径中去寻找 cfgFile 指定的文件
		if (StringUtils.isNotBlank(__PROJECT_HOME)) {
			_result = new File(__PROJECT_HOME + cfgFile);
			if (_result.canRead() && _result.isAbsolute() && _result.exists()) {
				return _result;
			}
		}
		// 到 configHome(主路径)路径中去寻找 cfgFile 指定的文件
		if (StringUtils.isNotBlank(__CONFIG_HOME)) {
			_result = new File(__CONFIG_HOME + cfgFile);
			if (_result.canRead() && _result.isAbsolute() && _result.exists()) {
				return _result;
			}
		}
		try {
			// 到 userDir(用户路径)路径中去寻找 cfgFile 指定的文件
			if (StringUtils.isNotBlank(__USER_DIR)) {
				_result = new File(__USER_DIR + cfgFile);
				if (_result.canRead() && _result.isAbsolute() && _result.exists()) {
					return _result;
				}
			}
			// 到 osUserHome(系统用户路径)路径中去寻找 cfgFile 指定的文件
			if (StringUtils.isNotBlank(__USER_HOME)) {
				_result = new File(__USER_HOME + cfgFile);
				if (_result.canRead() && _result.isAbsolute() && _result.exists()) {
					return _result;
				}
			}
		} catch (AccessControlException e) {
			RuntimeUtils.unwrapThrow(e).printStackTrace(System.err);
		}
		return null;
	}

	public static String getModuleHome() {
		return __MODULE_HOME;
	}

	public static String getUserHome() {
		return __USER_HOME;
	}

	public static String getProjectHome() {
		return __PROJECT_HOME;
	}

	public static String getUserDir() {
		return __USER_DIR;
	}

	public static String getConfigHome() {
		return __CONFIG_HOME;
	}

	/**
	 * 填充配置对象
	 *
	 * @param config 配置对象，不可为空
	 * @param cfgFileName 配置所需要的装载参数
	 * @param needSearch 是否采用智能搜索
	 * @return 是否成功装载配置
	 */
	public static synchronized boolean fillCfg(IConfiguration config, String cfgFileName, boolean needSearch) {
		if (__IS_INITED) {
			if (config == null) {
				return false;
			}
			try {
				IConfigurationProvider _provider = __CFG_CONFIG.getConfigurationProviderClassImpl();
				if (needSearch) {
					_provider.load(smartSearch(cfgFileName));
				} else {
					_provider.load(cfgFileName);
				}
				config.initialize(_provider);
				return true;
			} catch (ConfigurationLoadException e) {
				RuntimeUtils.unwrapThrow(e).printStackTrace(System.err);
				return false;
			}
		} else {
			System.err.println(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.configuration.module_not_init"));
		}
		return false;
	}

	public static boolean fillCfg(IConfiguration config, String cfgFileName) {
		return fillCfg(config, cfgFileName, true);
	}

	/**
	 * 装载配置，根据Configuration注解指定的配置文件进行加载，否则默认使用当前配置类对象的SimpleName作为配置文件名，即：SimpleName.CfgTagName.xml
	 *
	 * @param config 配置对象，不可为空
	 * @param needSearch 是否采用智能搜索
	 * @return 是否成功装载配置
	 */
	public static boolean fillCfg(IConfiguration config, boolean needSearch) {
		if (config != null) {
			String _cfgFileName = null;
			Configuration _configuration = config.getClass().getAnnotation(Configuration.class);
			if (_configuration != null) {
				_cfgFileName = _configuration.value();
			}
			if (StringUtils.isBlank(_cfgFileName)) {
				_cfgFileName = "cfgs/" + config.getClass().getSimpleName().toLowerCase() + config.getCfgTagName() + ".xml";
			}
			return fillCfg(config, _cfgFileName, needSearch);
		}
		return false;
	}

	public static boolean fillCfg(IConfiguration config) {
		return fillCfg(config, true);
	}

}
