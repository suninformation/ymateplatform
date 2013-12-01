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

import net.ymate.platform.base.YMP;
import net.ymate.platform.commons.i18n.I18N;



/**
 * <p>
 * AbstractLogger
 * </p>
 * <p>
 * 
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
 *          <td>2012-12-21下午12:27:37</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractLogger implements ILogger {

	protected static boolean __IS_INITED;

	/** 当前输出日志过滤级别 */
	protected LogLevel level;

    /** 是否输出到windows的命令行 */
	protected boolean isPrintConsole = false;

	/** 是否启用调用者信息输出 */
	protected boolean enableCallerInfo = true;

	/** 调用者深度 */
	protected int callerDeepth;

	protected String loggerName;

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.logger.ILogger#getLevel()
	 */
	public LogLevel getLevel() {
		return level;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.logger.ILogger#setLevel(net.ymate.platform.commons.logger.ILogger.LogLevel)
	 */
	public void setLevel(LogLevel level) {
		if (level != null) {
			this.level = level;
		} else {
			this.level = LogLevel.ALL;
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.logger.ILogger#switchPrintConsole(boolean)
	 */
	public void switchPrintConsole(boolean isPrint) {
		isPrintConsole = isPrint;
		if (!isPrintConsole) {
			System.err.println(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.commons.logger_switch_without_console", loggerName));
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.logger.ILogger#switchLogCallerInfo(boolean)
	 */
	public void switchLogCallerInfo(boolean enable) {
		enableCallerInfo = enable;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.commons.logger.ILogger#setLogCallerDeepth(int)
	 */
	public void setLogCallerDeepth(int deepth) {
		if (deepth >= 0) {
			callerDeepth = deepth;
		}
	}

	/**
	 * 获取调用者信息
	 *
	 * @param stackDeepth 堆栈深度，向上寻找堆栈长度
	 * @return 找到的堆栈信息，格式为：className.methodName:lineNumber，如果找不到则返回NOT_MATCH:-1
	 */
	protected String makeCallerInfo(int stackDeepth) {
		StackTraceElement stack[] = new Throwable().getStackTrace();
		// 追溯到对应的调用行，如果对应行不存在，则不给出无法确定行号的输出
		if (stackDeepth >= 0 && stack.length > 1 + stackDeepth) {
			StackTraceElement traceElement = stack[1 + stackDeepth];
			String className = traceElement.getClassName();
			return className.substring(className.lastIndexOf(".") + 1) + "." + traceElement.getMethodName() + ":" + traceElement.getLineNumber();
		}
		return "NOT_MATCH:-1";
	}

	/**
	 * 将异常转换为堆栈输出串
	 *
	 * @param e 需要输出的异常对象
	 * @return 转换出的字符串，不为空
	 */
	protected String toStacksString(Throwable e) {
		if (e == null) {
			return "";
		}
		StringBuffer eStrBuf = new StringBuffer(I18N.formatMessage(YMP.__LSTRING_FILE, null, null, "ymp.commons.logger_content", e.getClass().getName(), e.getMessage()));
		StackTraceElement[] traces = e.getStackTrace();
        for (StackTraceElement trace : traces) {
            eStrBuf.append("\tat "); // 在堆栈行开始增加空格
            eStrBuf.append(trace.toString());
            eStrBuf.append("\r\n");
        }
		ex(eStrBuf, e.getCause());
		return eStrBuf.toString();
	}

	/**
	 * 将异常输出到字符缓冲中
	 *
	 * @param sb 需要输出到的目标字符串缓冲，不可为空
	 * @param t 需要输出的异常
	 * @return 如果还有引起异常的源，那么返回true
	 */
	protected boolean ex(StringBuffer sb, Throwable t) {
		if (t != null) {
			sb.append("Caused by: ");
			sb.append(t.getClass().getName());
			sb.append(": ");
			sb.append(t.getMessage());
			sb.append("\r\n");
			StackTraceElement[] traces = t.getStackTrace();
			int tracesSize = traces.length;
			for (int i = 0; i < tracesSize; i++) {
				if (i < ILogConfig.PRINT_STACK_COUNT) {
					sb.append("\tat "); // 在堆栈行开始增加空格
					StackTraceElement trace = traces[i];
					sb.append(trace.toString()).append("\r\n");
				} else {
					sb.append("\t... ");
					sb.append(tracesSize - ILogConfig.PRINT_STACK_COUNT);
					sb.append(" more\r\n");
					break;
				}
			}
			if (ex(sb, t.getCause())) {
				return true;
			}
		}
		return false;
	}

}
