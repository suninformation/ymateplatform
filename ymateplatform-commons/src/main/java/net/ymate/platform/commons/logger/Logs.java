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
package net.ymate.platform.commons.logger;

import java.util.HashMap;
import java.util.Map;

import net.ymate.platform.commons.logger.ILogger.LogLevel;

import org.apache.commons.lang.StringUtils;


/**
 * <p>
 * Logs
 * </p>
 * <p>
 * 日志记录管理器；
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
 *          <td>2011-8-27下午03:56:24</td>
 *          </tr>
 *          </table>
 */
public class Logs {

	/**
	 * 当前加载的日志映射，可以通过名称进行检索获取，保证其中每一个logger都不为空
	 */
	private static Map<String, ILogger> __LOGGER_MAPS = new HashMap<String, ILogger>();

	/**
	 * 当前日志记录器框架初始化配置对象
	 */
	private static ILogConfig __LOG_CONFIG;

	/**
	 * 当前使用的日志记录器
	 */
	private static ILogger __CURRENT_LOGGER;

	/**
	 * 日志工具是否初始化成功
	 */
	private static boolean __IS_INITED;

	/**
	 * 初始化日志记录管理器
	 * 
	 * @param config
	 */
	public static void initialize(ILogConfig config) {
		if (config == null) {
			throw new Error("[错误: 日志记录器初始化失败，参数配置对象为NULL]");
		}
		__LOG_CONFIG = config;
		__CURRENT_LOGGER = create(__LOG_CONFIG.getLogLevel(), __LOG_CONFIG.getLoggerName());
		__CURRENT_LOGGER.setLogCallerDeepth(3);
		if (__CURRENT_LOGGER != null) {
			__IS_INITED = true;
		}
	}

	/**
	 * @return 返回当前日志记录器框架初始化配置对象
	 */
	public static ILogConfig getConfig() {
		return __LOG_CONFIG;
	}

	/**
	 * 判断日志是否初始化成功
	 *
	 * @return
	 */
	public static boolean isInited() {
		return __IS_INITED;
	}

	/**
	 * 释放日志记录器
	 *
	 * @param loggerName 需要释放的日志记录器名称标识，如果为空则不释放任何日志记录器
	 */
	public static void release(String loggerName) {
		if (StringUtils.isBlank(loggerName)) {
			return;
		}
		ILogger logger = __LOGGER_MAPS.remove(loggerName);
		if (logger != null) {
			logger.destroy();
		}
		if (logger == __CURRENT_LOGGER) {
			__CURRENT_LOGGER = null;
			__IS_INITED = false;
		}
	}

	/**
	 * 清理当前所有的日志，并停止，而且需要回收资源
	 */
	public static void clear() {
		for (String loggerName : __LOGGER_MAPS.keySet()) {
			release(loggerName);
		}
		__LOGGER_MAPS.clear();
		__CURRENT_LOGGER = null;
		__IS_INITED = false;
	}

	/**
	 * 设置当前日志记录级别
	 *
	 * @param level
	 */
	public static void setLevel(LogLevel level) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.setLevel(level);
		}
	}

	/**
	 * 开关日志记录时输出的调用者信息，如类名、方法名、行号等信息
	 *
	 * @param enable true-打开，false-关闭
	 */
	public static void switchLogCallerInfo(boolean enable) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.switchLogCallerInfo(enable);
		}
	}

	/**
	 * 开关是否将当前日志记录器打印到控制台
	 *
	 * @param isPrint true-打开，false-关闭
	 */
	public static void switchPrintConsole(boolean isPrint) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.switchPrintConsole(isPrint);
		}
	}

	/**
	 * @param defaultLevel
	 * @param loggerName
	 * @return 创建并返回指定名称的日志记录器，若已存在则变更其日志级别
	 */
	public static ILogger create(LogLevel defaultLevel, String loggerName) {
		ILogger logger = __LOGGER_MAPS.get(loggerName);
		if (logger == null) {
			logger = __LOG_CONFIG == null ? null : __LOG_CONFIG.getLoggerClassImpl();
			if (logger != null) {
				logger.initialize(defaultLevel, loggerName);
				logger.setLogCallerDeepth(2);
				__LOGGER_MAPS.put(loggerName, logger);
			} else {
				throw new Error("[错误: 创建名称为\"" + loggerName + "\"日志记录器失败，返回的ILogger接口实现类对象为NULL]");
			}
		} else if (loggerName.equals(ILogConfig.DEFAULT_LOGGER_NAME)) {
			throw new Error("[错误: 名称为\"" + loggerName + "\"日志记录器为默认对象，不能重复创建]");
		} else {
			logger.setLevel(defaultLevel);
		}
		return logger;
	}

	/**
	 * @param loggerName
	 * @return 从当前配置文件中的日志中获取指定名称的日志记录器
	 */
	public static ILogger get(String loggerName) {
		ILogger logger = null;
		if (StringUtils.isNotBlank(loggerName) && !loggerName.equals(ILogConfig.DEFAULT_LOGGER_NAME)) {
			logger = __LOGGER_MAPS.get(loggerName);
		}
		if (logger == null) {
			throw new Error("[错误: 名称为\"" + loggerName + "\"日志记录器不存在，请确认是否已经创建]");
		}
		return logger;
	}

	public static boolean isDebugEnabled() {
        return __CURRENT_LOGGER != null && __CURRENT_LOGGER.getLevel().getValue() <= LogLevel.DEBUG.getValue();
    }

	public static void trace(String info) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.trace(info, null);
		}
	}

	public static void trace(String info, Throwable e) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.trace(info, e);
		}
	}

	public static void fatal(String info) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.fatal(info, null);
		}
	}

	public static void fatal(String info, Throwable e) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.fatal(info, e);
		}
	}

	/**
	 * 记录调试信息
	 *
	 * @param info
	 */
	public static void debug(String info) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.debug(info, null);
		}
	}

	/**
	 * 记录调试信息和异常
	 *
	 * @param info
	 * @param e
	 */
	public static void debug(String info, Throwable e) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.debug(info, e);
		}
	}

	public static boolean isErrorEnabled() {
        return __CURRENT_LOGGER != null && __CURRENT_LOGGER.getLevel().getValue() <= LogLevel.ERROR.getValue();
    }

	/**
	 * 记录错误信息
	 *
	 * @param info
	 */
	public static void error(String info) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.error(info, null);
		}
	}

	/**
	 * 记录错误信息和异常
	 *
	 * @param info
	 * @param e
	 */
	public static void error(String info, Throwable e) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.error(info, e);
		}
	}

	public static boolean isInfoEnabled() {
        return __CURRENT_LOGGER != null && __CURRENT_LOGGER.getLevel().getValue() <= LogLevel.INFO.getValue();
    }

	/**
	 * 记录提示信息
	 *
	 * @param info
	 */
	public static void info(String info) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.info(info, null);
		}
	}

	/**
	 * 记录提示信息和异常
	 *
	 * @param info
	 * @param e
	 */
	public static void info(String info, Throwable e) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.info(info, e);
		}
	}

	/**
	 * 以指定级别记录信息
	 *
	 * @param info
	 * @param level
	 */
	public static void log(String info, LogLevel level) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.log(info, null, level);
		}
	}

	/**
	 * 以指定级别记录信息和异常
	 *
	 * @param info
	 * @param e
	 * @param level
	 */
	public static void log(String info, Throwable e, LogLevel level) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.log(info, e, level);
		}
	}

	public static boolean isWarnEnabled() {
        return __CURRENT_LOGGER != null && __CURRENT_LOGGER.getLevel().getValue() <= LogLevel.WARN.getValue();
    }

	/**
	 * 记录警告信息
	 *
	 * @param info
	 */
	public static void warn(String info) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.warn(info, null);
		}
	}

	/**
	 * 记录警告信息和异常
	 *
	 * @param info
	 * @param e
	 */
	public static void warn(String info, Throwable e) {
		if (__CURRENT_LOGGER != null) {
			__CURRENT_LOGGER.warn(info, e);
		}
	}

	/**
	 * 是否存在某个日志记录器
	 *
	 * @param loggerName 日志记录器名称
	 * @return 如果当前日志记录器存在那么返回true，如果不存在那么返回false
	 */
	public static boolean has(String loggerName) {
        return StringUtils.isNotBlank(loggerName) && __CURRENT_LOGGER != null && __CURRENT_LOGGER.has(loggerName);
    }

}
