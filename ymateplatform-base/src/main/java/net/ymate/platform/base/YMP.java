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
package net.ymate.platform.base;

import java.io.InputStream;
import java.util.Properties;

import net.ymate.platform.base.impl.DefaultModuleLoader;
import net.ymate.platform.commons.lang.BlurObject;

import org.apache.commons.lang.time.StopWatch;

/**
 * <p>
 * YMP
 * </p>
 * <p>
 * 框架核心模块管理器；
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
 *          <td>2012-12-23下午5:52:44</td>
 *          </tr>
 *          </table>
 */
public class YMP {

	public final static String VERSION = YMP.class.getPackage().getSpecificationVersion();

	public final static String BUILD_DATE = YMP.class.getPackage().getImplementationVersion();

	public static boolean IS_DEV_MODEL;

	public static boolean IS_INITED;

	private static IModuleLoader __MODULE_LOADER;

	/**
	 * 启动框架初始化
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		initialize();
	}

	/**
	 * 初始化框架模块
	 */
	public static void initialize()  {
		if (!IS_INITED) {
			System.out.println("[信息：初始化框架(v" + VERSION + "-" + BUILD_DATE + ")]");
			Properties _configs = new Properties();
			InputStream _in = YMP.class.getClassLoader().getResourceAsStream("ymp-conf.properties");
			if (_in != null) {
				try {
					_configs.load(_in);
					IS_DEV_MODEL = new BlurObject(_configs.getProperty("ymp.dev_model")).toBooleanValue();
					__MODULE_LOADER = (IModuleLoader) Class.forName(_configs.getProperty("ymp.module_loader_impl_class")).newInstance();
				} catch (Exception e) {
					__MODULE_LOADER = new DefaultModuleLoader();
				}
			} else {
				System.err.println("[警告：框架配置加载失败，请确认ymp-conf.properties文件是否存在]");
			}
			StopWatch _stopWatch = new StopWatch();
			_stopWatch.start();
			try {
				__MODULE_LOADER.initialize(_configs);
				IS_INITED = true;
			} catch (Exception e) {
				System.err.println("[警告：" + e.getMessage() + "]");
				e.printStackTrace();
			} finally {
				_stopWatch.stop();
				System.out.println("[信息：模块初始化操作" + (IS_INITED ? "完毕" : "失败") + "，耗时" + _stopWatch.getTime() + "ms]");
			}
		}
	}

	/**
	 * 销毁
	 */
	public static void destroy() {
		if (IS_INITED && __MODULE_LOADER != null) {
			try {
				__MODULE_LOADER.destroy();
			} catch (Exception e) {
				//~~~
			}
			__MODULE_LOADER = null;
			IS_INITED = false;
		}
	}

}
