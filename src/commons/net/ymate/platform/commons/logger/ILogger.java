/**
 * <p>文件名:	ILogger.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Commons</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.logger;

import org.apache.log4j.Level;




/**
 * <p>
 * ILogger
 * </p>
 * <p>
 * 日志记录器接口定义类；
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
 *          <td>2011-8-27下午03:58:07</td>
 *          </tr>
 *          </table>
 */
public interface ILogger {

	/**
	 * 使用ILogConfig配置文件加载日志记录器，并提供默认记录级别
	 *
	 * @param defaultLevel 默认输出级别
	 * @param loggerName 启动的日志记录名称
	 */
	void initialize(LogLevel defaultLevel, String loggerName);

	/**
	 * 销毁(停止)当前的日志记录器，需要清除所占用的资源，而且日志记录器一旦被停止，将无法恢复使用
	 */
	void destroy();

	/**
	 * 设置是否打印日志输出到命令行
	 *
	 * @param isPrint
	 */
	void switchPrintConsole(boolean isPrint);

	/**
	 * 开关日志记录时输出的调用者信息，如类名、方法名、行号等信息
	 *
	 * @param enable 如果true则标识打开，false则为关闭
	 */
	public void switchLogCallerInfo(boolean enable);

	/**
	 * 设置调用者深度
	 *
	 * @param deepth 必须大于等于零
	 */
	public void setLogCallerDeepth(int deepth);

	/**
	 * 是否存在某个日志记录器
	 *
	 * @param loggerName 日志记录器名称
	 * @return 如果当前日志记录器存在那么返回true，如果不存在那么返回false
	 */
	boolean has(String loggerName);

	/**
	 * 设置日志记录级别
	 *
	 * @param level
	 */
	void setLevel(LogLevel level);

	/**
	 * 获取日志级别
	 *
	 * @return
	 */
	LogLevel getLevel();

	/**
	 * 不分级的记录器
	 *
	 * @param info
	 * @param level
	 */
	void log(String info, LogLevel level);

	/**
	 * 不分级的记录器
	 *
	 * @param info
	 * @param e
	 * @param level
	 */
	void log(String info, Throwable e, LogLevel level);

	/**
	 * 输出追溯信息
	 * 
	 * @param info
	 */
	void trace(String info);

	/**
	 * 输出追溯信息
	 * 
	 * @param info
	 * @param e
	 */
	void trace(String info, Throwable e);
	
	/**
	 * 输出调试信息
	 *
	 * @param info
	 */
	void debug(String info);

	/**
	 * 输出调试信息
	 *
	 * @param info
	 * @param e
	 */
	void debug(String info, Throwable e);

	/**
	 * 输出提示信息
	 *
	 * @param info
	 */
	void info(String info);

	/**
	 * 输出提示信息
	 *
	 * @param info
	 * @param e
	 */
	void info(String info, Throwable e);

	/**
	 * 输出警告信息
	 *
	 * @param info
	 */
	void warn(String info);

	/**
	 * 输出警告信息
	 *
	 * @param info
	 * @param e
	 */
	void warn(String info, Throwable e);

	/**
	 * 输出错误信息
	 *
	 * @param info
	 */
	void error(String info);

	/**
	 * 输出错误信息
	 *
	 * @param info
	 * @param e
	 */
	void error(String info, Throwable e);

	/**
	 * 输出致命错误信息
	 * 
	 * @param info
	 */
	void fatal(String info);

	/**
	 * 输出致命错误信息
	 * 
	 * @param info
	 * @param e
	 */
	void fatal(String info, Throwable e);

	/**
	 * <p>
	 * LogLevel
	 * </p>
	 * <p>
	 * 日志记录级别
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
	 *          <td>2010-8-2上午10:23:10</td>
	 *          </tr>
	 *          </table>
	 */
	public enum LogLevel {

		ALL(0, "all", "[全部]"),
		TRACE(1, "trace", "[追溯]"),
		DEBUG(2, "debug", "[调试]"),
		INFO(3, "info", "[提示]"),
		WARN(4, "warn", "[警告]"),
		ERROR(5, "error", "[错误]"),
		FATAL(6, "fatal", "[致命]"),
		OFF(7, "off", "[关闭]");

		/** 日志级别名称 */
		private String name;
		
		/** 日志级别显示名称 */
		private String displayName;

		/** 日志级别值 */
		private int value;

		public static LogLevel parse(String logLevelName) {
			for (LogLevel level : LogLevel.values()) {
				if (level.name.equalsIgnoreCase(logLevelName)) {
					return level;
				}
			}
			return ALL;
		}

		/**
		 * 分析日志级别
		 *
		 * @param logLevelValue
		 * @return
		 */
		public static LogLevel parse(int logLevelValue) {
			switch (logLevelValue) {
				case 0:
					return ALL;
				case 1:
					return TRACE;
				case 2:
					return DEBUG;
				case 3:
					return INFO;
				case 4:
					return WARN;
				case 5:
					return ERROR;
				case 6:
					return FATAL;
				case 7:
					return OFF;
				default:
					return ALL;
			}
		}

		public static LogLevel parse(Level level) {
			switch (level.toInt()) {
			case Level.ALL_INT:
				return ALL;
			case Level.TRACE_INT:
				return TRACE;
			case Level.DEBUG_INT:
				return DEBUG;
			case Level.INFO_INT:
				return INFO;
			case Level.WARN_INT:
				return WARN;
			case Level.ERROR_INT:
				return ERROR;
			case Level.FATAL_INT:
				return FATAL;
			case Level.OFF_INT:
				return OFF;
			default:
				return ALL;
		}
		}

		/**
		 * 构造器
		 *
		 * @param value 日志输出级别值
		 * @param name 日志输出级别名称
		 * @param displayName 日志输出显示名称
		 */
		private LogLevel(int value, String name, String displayName) {
			this.value = value;
			this.name = name;
			this.displayName = displayName;
		}

		/**
		 * @return 转换当前日志级别为Log4J所用Level对象
		 */
		public Level toLevel() {
			switch (this.value) {
			case 0:
				return Level.ALL;
			case 1:
				return Level.TRACE;
			case 2:
				return Level.DEBUG;
			case 3:
				return Level.INFO;
			case 4:
				return Level.WARN;
			case 5:
				return Level.ERROR;
			case 6:
				return Level.FATAL;
			case 7:
				return Level.OFF;
			default:
				return Level.ALL;
			}
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
	}

}
