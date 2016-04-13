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
package net.ymate.platform.commons.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * <p>
 * RuntimeUtils
 * </p>
 * <p>
 * 运行时工具类，获取运行时相关信息
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
 *          <td>2010-8-2上午10:10:16</td>
 *          </tr>
 *          </table>
 */
public class RuntimeUtils {

	/**
	 * 当前操作系统是否为类Unix系统
	 * 
	 * @return
	 */
	public static boolean isUnixOrLinux() {
		return SystemUtils.IS_OS_UNIX;
	}

	/**
	 * 当前操作系统是否为Windows系统
	 * 
	 * @return
	 */
	public static boolean isWindows() {
		return SystemUtils.IS_OS_WINDOWS;
	}

	/**
	 * @return 获取应用根路径（若WEB工程则基于.../WEB-INF/返回，若普通工程则返回类所在路径）
	 */
	public static String getRootPath() {
		return getRootPath(true);
	}

    /**
     * @param safe 若WEB工程是否保留WEB-INF
     * @return 返回应用根路径
     */
    public static String getRootPath(boolean safe) {
        URL _rootURL = RuntimeUtils.class.getClassLoader().getResource("/");
        boolean _isWeb = false;
        if (_rootURL == null) {
            _rootURL = RuntimeUtils.class.getClassLoader().getResource("");
        } else {
            _isWeb = true;
        }
        String _rootPath = StringUtils.replace(_isWeb ? StringUtils.substringBefore(_rootURL.getPath(), safe ? "classes/" : "WEB-INF/") : _rootURL.getPath(), "%20", " ");
        if (isWindows()) {
            if (_rootPath.startsWith("/")) {
                _rootPath = _rootPath.substring(1);
            }
        }
        return _rootPath;
    }

	/**
	 * 根据格式化字符串，生成运行时异常
	 * 
	 * @param format 格式
	 * @param args 参数
	 * @return 运行时异常
	 */
	public static RuntimeException makeRuntimeThrow(String format, Object... args) {
		return new RuntimeException(String.format(format, args));
	}

	/**
	 * 将抛出对象包裹成运行时异常，并增加描述
	 * 
	 * @param e 抛出对象
	 * @param fmt 格式
	 * @param args 参数
	 * @return 运行时异常
	 */
	public static RuntimeException wrapRuntimeThrow(Throwable e, String fmt, Object... args) {
		return new RuntimeException(String.format(fmt, args), e);
	}

	/**
	 * 用运行时异常包裹抛出对象，如果抛出对象本身就是运行时异常，则直接返回
	 * <p>
	 * 若 e 对象是 InvocationTargetException，则将其剥离，仅包裹其 TargetException 对象
	 * </p>
	 * 
	 * @param e 抛出对象
	 * @return 运行时异常
	 */
	public static RuntimeException wrapRuntimeThrow(Throwable e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		if (e instanceof InvocationTargetException) {
			return wrapRuntimeThrow(((InvocationTargetException) e).getTargetException());
		}
		return new RuntimeException(e);
	}

	public static Throwable unwrapThrow(Throwable e) {
		if (e == null) {
			return null;
		}
		if (e instanceof InvocationTargetException) {
			InvocationTargetException itE = (InvocationTargetException) e;
			if (itE.getTargetException() != null) {
				return unwrapThrow(itE.getTargetException());
			}
		}
		if (e.getCause() != null) {
			return unwrapThrow(e.getCause());
		}
		return e;
	}

	/**
	 * 垃圾回收，返回回收的字节数
	 *
	 * @return 回收的字节数，如果为负数则表示当前内存使用情况很差，基本属于没有内存可用了
	 */
	public static final long gc() {
		Runtime rt = Runtime.getRuntime();
		long lastUsed = rt.totalMemory() - rt.freeMemory();
		rt.gc();
		return lastUsed - rt.totalMemory() + rt.freeMemory();
	}

}
