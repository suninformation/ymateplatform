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
package net.ymate.platform.plugin.util;

import java.io.File;
import java.net.URL;

import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.ResourceUtils;
import net.ymate.platform.configuration.Cfgs;
import net.ymate.platform.configuration.IConfiguration;
import net.ymate.platform.configuration.annotation.Configuration;
import net.ymate.platform.plugin.IPlugin;

import org.apache.commons.lang.StringUtils;


/**
 * <p>
 * PluginUtils
 * </p>
 * <p>
 * 插件资源、配置文件加载工具类；
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
 *          <td>2012-5-23下午4:12:19</td>
 *          </tr>
 *          </table>
 */
public class PluginUtils {

	/**
	 * @param cfgFile
	 * @param plugin
	 * @return 智能搜索插件所在类路径配置文件真实资源路径
	 */
	public static String getResourcePath(String cfgFile, IPlugin plugin) {
		if (plugin != null) {
			// 先在插件所在目录查找
			if (StringUtils.isNotBlank(plugin.getPluginMeta().getPath())) {
				File _result = new File(plugin.getPluginMeta().getPath(), cfgFile);
				if (_result.isAbsolute() && _result.exists() && _result.canRead()) {
					return _result.getPath();
				}
			}
			// 然后在插件所在类路径中查找
			URL _targetFileURL = ResourceUtils.getResource(cfgFile, plugin.getClass());
			if (_targetFileURL != null) {
	    		return _targetFileURL.toString();
	    	}
		}
		return null;
	}

	/**
	 * @param cfgFile
	 * @param plugin
	 * @return 智能搜索插件所在类路径配置资源文件对象
	 */
	public static File getResourceFile(String cfgFile, IPlugin plugin) {
		if (plugin != null) {
			// 先在插件所在目录查找
			if (StringUtils.isNotBlank(plugin.getPluginMeta().getPath())) {
				File _result = new File(plugin.getPluginMeta().getPath(), cfgFile);
				if (_result.isAbsolute() && _result.exists() && _result.canRead()) {
					return _result;
				}
			}
			// 然后在插件所在类路径中查找
			URL _targetFileURL = ResourceUtils.getResource(cfgFile, plugin.getClass());
			if (_targetFileURL != null) {
	    		return FileUtils.toFile(_targetFileURL);
	    	}
		}
		return null;
	}

	/**
	 * 装载配置，使用指定配置文件名，优先处理插件
	 *
	 * @param config 配置对象，不可为空
	 * @param cfgFileName  配置所需要的装载参数，即配置文件名称，为相对路径
	 * @param plugin 插件接口对象，若提供则优先处理
	 * @return
	 */
	public static boolean fillCfg(IConfiguration config, String cfgFileName, IPlugin plugin) {
		String _cfgFileName = getResourcePath(cfgFileName, plugin);
		return Cfgs.fillCfg(config, _cfgFileName, _cfgFileName == null);
	}

	/**
	 * 装载配置，根据 Configuration 注解指定的配置文件进行加载，否则默认使用当前配置类对象的SimpleName作为配置文件名，即：SimpleName.CfgTagName.xml
	 *
	 * @param config 配置对象，不可为空
	 * @param plugin 插件接口对象，若提供则优先处理
	 * @return 是否成功装载配置
	 */
	public static boolean fillCfg(IConfiguration config, IPlugin plugin) {
		if (config != null) {
			String _cfgFileName = null;
			Configuration _configuration = config.getClass().getAnnotation(Configuration.class);
			if (_configuration != null) {
				_cfgFileName = config.getClass().getAnnotation(Configuration.class).value();
			}
			if (StringUtils.isBlank(_cfgFileName)) {
				_cfgFileName = "cfgs/" + config.getClass().getSimpleName().toLowerCase() + config.getCfgTagName() + ".xml";
			}
			_cfgFileName = getResourcePath(_cfgFileName, plugin);
			return Cfgs.fillCfg(config, _cfgFileName, _cfgFileName == null);
		}
		return false;
	}

}
