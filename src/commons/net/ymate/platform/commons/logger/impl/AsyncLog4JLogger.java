/**
 * <p>文件名:	AsyncLog4JLogger.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Commons</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.logger.impl;

import java.io.File;
import java.util.Enumeration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.commons.logger.AbstractLogger;
import net.ymate.platform.commons.logger.ILogger;
import net.ymate.platform.commons.logger.Logs;
import net.ymate.platform.commons.util.DateTimeUtils;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


/**
 * <p>
 * AsyncLog4JLogger
 * </p>
 * <p>
 * 异常线程日志记录器（基于Log4J实现）；
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
 *          <td>2010-8-2上午10:09:29</td>
 *          </tr>
 *          </table>
 */
public class AsyncLog4JLogger extends AbstractLogger implements ILogger{

	/** 由 Apache Log4J 实现的日志输出 */
	private Logger logger;

	/** 日志队列，Map<level, Map<message, e>>，级别level不可能为空 */
	private LinkedBlockingQueue<PairObject<Integer, PairObject<Object, Throwable>>> logQueue;

	/** 日志输出线程 */
	private Thread logThread;

	private boolean isStoped = true;

	/**
	 * 构造器
	 */
	public AsyncLog4JLogger() {
		logQueue = new LinkedBlockingQueue<PairObject<Integer, PairObject<Object, Throwable>>>();
	}

	public boolean has(String loggerName) {
		if (__IS_INITED) {
			Logger l = Logger.getLogger(loggerName);
			if (l != null) {
				Enumeration<?> e = l.getAllAppenders();
				if (e != null && e.hasMoreElements()) {
					return true;
				}
			}
		}
		return false;
	}

	public void trace(String info, Throwable e) {
		bindExpInfo(info, e, LogLevel.TRACE);
	}

	public void trace(String info) {
		bindExpInfo(info, null, LogLevel.TRACE);
	}

	public void debug(String info, Throwable e) {
		bindExpInfo(info, e, LogLevel.DEBUG);
	}

	public void debug(String info) {
		bindExpInfo(info, null, LogLevel.DEBUG);
	}

	public void fatal(String info, Throwable e) {
		bindExpInfo(info, e, LogLevel.FATAL);
	}

	public void fatal(String info) {
		bindExpInfo(info, null, LogLevel.FATAL);
	}

	public void error(String info, Throwable e) {
		bindExpInfo(info, e, LogLevel.ERROR);
	}

	public void error(String info) {
		bindExpInfo(info, null, LogLevel.ERROR);
	}

	public void info(String info, Throwable e) {
		bindExpInfo(info, e, LogLevel.INFO);
	}
	
	public void info(String info) {
		bindExpInfo(info, null, LogLevel.INFO);
	}

	public void warn(String info, Throwable e) {
		bindExpInfo(info, e, LogLevel.WARN);
	}

	public void warn(String info) {
		bindExpInfo(info, null, LogLevel.WARN);
	}

	public void log(String info, Throwable e, LogLevel level) {
		bindExpInfo(info, e, null);
	}

	public void log(String info, LogLevel level) {
		bindExpInfo(info, null, null);
	}

	private void bindExpInfo(String info, Throwable e, LogLevel logLevel) {
		if (logLevel == null) {
			logLevel = LogLevel.ALL;
		}
		if (level.getValue() > logLevel.getValue()) {
			return;
		}
		StringBuffer sb = new StringBuffer(DateTimeUtils.formatTime(DateTimeUtils.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));
		sb.append(logLevel.getDisplayName());
		if (enableCallerInfo) {
			sb.append('[').append(Thread.currentThread().getId()).append(':').append(makeCallerInfo(callerDeepth)).append(']');
		}
		sb.append(' ').append(info);
		logQueue.add(new PairObject<Integer, PairObject<Object, Throwable>>(logLevel.getValue(), new PairObject<Object, Throwable>(sb, e)));
	}

	public void initialize(LogLevel defaultLevel, String loggerName) {
		if (!__IS_INITED) {
			File cfgFile = new File(Logs.getConfig().getLogCfgFile());
			if (!cfgFile.exists()) {
				System.err.println("[错误: 无法找到日志配置文件：" + Logs.getConfig().getLogCfgFile() + "]");
				return;
			}
			System.out.println("[信息: 加载Log4J配置文件: " + cfgFile.getPath() + "]");
			// 设置Property属性，方使在log4j.xml文件中直接引用${LOGS_DIR}属性值
			System.setProperty("LOGS_DIR", Logs.getConfig().getLogOutputDir());
			DOMConfigurator.configure(cfgFile.getPath());
			__IS_INITED = true;
		}
		level = defaultLevel;
		this.loggerName = loggerName;
		isPrintConsole = Logs.getConfig().allowPrintConsole();
		System.out.println("[信息: 初始化Log4J日志记录器 \"" + loggerName + "\"" + (!isPrintConsole ? ", 已将命令行输出关闭，请从日志文件查看日志记录信息]" : "]"));
		logger = Logger.getLogger(loggerName);
		logThread = new Thread("Log4J异步日志[" + loggerName + "]输出线程") {
			@Override
			public void run() {
				try {
					PairObject<Integer, PairObject<Object, Throwable>> logPObject;
					Object msg;
					Throwable e;
					while (!isStoped) {
						// 获取需要记录的日志内容，得到了就不为空
						logPObject = logQueue.poll(30, TimeUnit.SECONDS);
						if (logPObject == null) {
							continue;
						}
						msg = logPObject.getValue().getKey();
						e = logPObject.getValue().getValue();
						if (e == null) {
							if (logPObject.getKey() == LogLevel.ERROR.getValue()) {
								logger.error(msg);
							} else if (logPObject.getKey() == LogLevel.WARN.getValue()) {
								logger.warn(msg);
							} else if (logPObject.getKey() == LogLevel.INFO.getValue()) {
								logger.info(msg);
							} else if (logPObject.getKey() == LogLevel.DEBUG.getValue()) {
								logger.debug(msg);
							} else if (logPObject.getKey() == LogLevel.TRACE.getValue()) {
								logger.trace(msg);
							} else if (logPObject.getKey() == LogLevel.FATAL.getValue()) {
								logger.fatal(msg);
							} else {
								logger.debug(msg);
							}
							// 判断是否输出到控制台
							if (isPrintConsole) {
								System.out.println(msg);
							}
						} else {
							if (logPObject.getKey() == LogLevel.ERROR.getValue()) {
								logger.error(msg);
								logger.error(toStacksString(e));
							} else if (logPObject.getKey() == LogLevel.WARN.getValue()) {
								logger.warn(msg);
								logger.warn(toStacksString(e));
							} else if (logPObject.getKey() == LogLevel.INFO.getValue()) {
								logger.info(msg);
								logger.info(toStacksString(e));
							} else if (logPObject.getKey() == LogLevel.DEBUG.getValue()) {
								logger.debug(msg);
								logger.debug(toStacksString(e));
							} else if (logPObject.getKey() == LogLevel.TRACE.getValue()) {
								logger.trace(msg);
								logger.trace(toStacksString(e));
							} else if (logPObject.getKey() == LogLevel.FATAL.getValue()) {
								logger.fatal(msg);
								logger.fatal(toStacksString(e));
							} else {
								logger.debug(msg);
								logger.debug(toStacksString(e));
							}
							// 判断是否输出到控制台
							if (isPrintConsole) {
								System.out.println(msg + "\r\n" + toStacksString(e));
							}
						}
					}
				} catch (Exception e) {
					if (!isStoped) {
						System.err.println(DateTimeUtils.formatTime(DateTimeUtils.currentTimeMillis(), null) + "[严重故障：异常导致日志线程被中断]");
						e.printStackTrace();
					}
				}
			}
		};
		logThread.setDaemon(true);
		isStoped = false;
		logThread.start();

		logger.info("\r\n");
		logger.info("-------------------- 在 " + DateTimeUtils.formatTime(DateTimeUtils.currentTimeMillis(), null) + " 重新开启日志系统 --------------------");
	}

	public void destroy() {
		if (logThread != null) {
			isStoped = true;
			try {
				logThread.join();
			} catch (InterruptedException e) {
                // 忽略...
            }
		}
	}
}