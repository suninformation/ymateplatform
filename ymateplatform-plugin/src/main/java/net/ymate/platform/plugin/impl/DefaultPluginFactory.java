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
package net.ymate.platform.plugin.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.ymate.platform.commons.logger.Logs;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.configuration.Cfgs;
import net.ymate.platform.configuration.IConfigurable;
import net.ymate.platform.plugin.IPlugin;
import net.ymate.platform.plugin.IPluginConfig;
import net.ymate.platform.plugin.IPluginFactory;
import net.ymate.platform.plugin.IPluginParser;
import net.ymate.platform.plugin.PluginClassLoader;
import net.ymate.platform.plugin.PluginContext;
import net.ymate.platform.plugin.PluginException;
import net.ymate.platform.plugin.PluginInstanceException;
import net.ymate.platform.plugin.PluginMeta;
import net.ymate.platform.plugin.PluginNotFoundException;
import net.ymate.platform.plugin.util.PluginUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>
 * DefaultPluginFactory
 * </p>
 * <p>
 * 默认插件工厂接口定义实现类；
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
 *          <td>2012-12-2下午3:22:17</td>
 *          </tr>
 *          </table>
 */
public class DefaultPluginFactory implements IPluginFactory {

	private static final Log _LOG = LogFactory.getLog(DefaultPluginFactory.class);

	/**
	 * 排除的接口集合，被包含的接口将不被插件工厂管理
	 */
	protected static Set<String> __EXCLUDED_CLASS_NAME_SET;

	static {
		__EXCLUDED_CLASS_NAME_SET = new HashSet<String>();
		__EXCLUDED_CLASS_NAME_SET.add(IPlugin.class.getName());
	}

	/**
	 * 添加被排除的接口
	 * 
	 * @param interfaceClass 目标接口类对象
	 */
	public static void addExcludedInterfaceClass(Class<?> interfaceClass) {
		if (interfaceClass.isInterface()) {
			__EXCLUDED_CLASS_NAME_SET.add(interfaceClass.getName());
		}
	}

	protected Map<String, PluginMeta> __PLUGINMETA_MAPS = new ConcurrentHashMap <String, PluginMeta>();

	protected Map<String, IPlugin> __PLUGIN_MAPS = new ConcurrentHashMap <String, IPlugin>();

	protected Map<String, String> __PLUGIN_INTERFACE_WITH_PID = new ConcurrentHashMap<String, String>();

	protected IPluginConfig __PLUGIN_CONFIG;

	protected ClassLoader __PLUGIN_CLASSLOADER;

	protected boolean __IS_INITED;

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginFactory#getPluginMeta(java.lang.String)
	 */
	public PluginMeta getPluginMeta(String pluginId) {
		return __PLUGINMETA_MAPS.get(pluginId);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginFactory#getPluginMetas()
	 */
	public Collection<PluginMeta> getPluginMetas() {
		return Collections.unmodifiableCollection(__PLUGINMETA_MAPS.values());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginFactory#getPlugin(java.lang.String)
	 */
	public synchronized IPlugin getPlugin(String pluginId) throws PluginNotFoundException, PluginInstanceException {
		if (__PLUGIN_MAPS.containsKey(pluginId)) {
			return __PLUGIN_MAPS.get(pluginId);
		}
		try {
			PluginMeta _pluginMeta = __PLUGINMETA_MAPS.get(pluginId);
			if (_pluginMeta == null || StringUtils.isBlank(_pluginMeta.getInitClass())) {
				throw new PluginInstanceException("插件 (Id = " + pluginId + ") 配置中未设置初始化启动类参数.");
			} else {
				_LOG.info("创建插件[" + pluginId + "]实例对象...");
				IPlugin _pluginObj = (IPlugin) _pluginMeta.getClassLoader().loadClass(_pluginMeta.getInitClass()).newInstance();
				if (_pluginObj != null) {
					// 判断当前组件类是否实现了配置接口
					if (Cfgs.isInited() && _pluginObj instanceof IConfigurable) {
						// 获取当前组件的配置对象并尝试直接加载组件配置
						PluginUtils.fillCfg(((IConfigurable) _pluginObj).getConfig(), _pluginObj);
					}
					_LOG.info("初始化插件[" + pluginId + "]...");
					_pluginObj.doInit(new PluginContext(_pluginMeta));
					_LOG.info("启动插件[" + pluginId + "]...");
					_pluginObj.doStart();
					__PLUGIN_MAPS.put(_pluginMeta.getId(), _pluginObj);
					//
					String[] _interfaceNames = ClassUtils.getInterfaceNames(_pluginObj.getClass());
					for (String _interfaceName : _interfaceNames) {
						if (__EXCLUDED_CLASS_NAME_SET.contains(_interfaceName)) {
							continue;
						}
						__PLUGIN_INTERFACE_WITH_PID.put(_interfaceName, _pluginMeta.getId());
					}
				}
				_LOG.info("插件[" + pluginId + "]实例对象创建完毕");
				return _pluginObj;
			}
		} catch (ClassNotFoundException e) {
			throw new PluginNotFoundException(RuntimeUtils.unwrapThrow(e));
		} catch (InstantiationException e) {
			throw new PluginInstanceException(RuntimeUtils.unwrapThrow(e));
		} catch (IllegalAccessException e) {
			throw new PluginInstanceException(RuntimeUtils.unwrapThrow(e));
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginFactory#getPluginMeta(java.lang.Class)
	 */
	public PluginMeta getPluginMeta(Class<?> clazz) {
		String _pid = __PLUGIN_INTERFACE_WITH_PID.get(clazz.getName());
		return this.getPluginMeta(_pid);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginFactory#getPlugin(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T getPlugin(Class<T> clazz) throws PluginNotFoundException, PluginInstanceException {
		String _pid = __PLUGIN_INTERFACE_WITH_PID.get(clazz.getName());
		return StringUtils.isBlank(_pid) ? null : (T) this.getPlugin(_pid);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginFactory#getPluginClassLoader()
	 */
	public synchronized ClassLoader getPluginClassLoader() {
		if (__PLUGIN_CLASSLOADER == null) {
			if (StringUtils.isNotBlank(__PLUGIN_CONFIG.getPluginHomePath())) {
				_LOG.info("创建插件工厂类加载器对象...");
				ArrayList<URL> _commonLibs = new ArrayList<URL>();
				// 扫描并分析插件通用类路径
				File _pluginCommonFile = new File(__PLUGIN_CONFIG.getPluginHomePath(), ".plugin");
				if (_pluginCommonFile.exists() && _pluginCommonFile.isDirectory()) {
					try {
						// 设置通用JAR包路径
						File _tempFile = new File(_pluginCommonFile, "lib");
						if (_tempFile.exists() && _tempFile.isDirectory()) {
                            File[] _libFiles = _tempFile.listFiles();
							for (File _libFile : _libFiles != null ? _libFiles : new File[0]) {
								if (_libFile.getPath().endsWith("jar")) {
									_LOG.info("插件工厂类加载器加载Jar文件 [" + _libFile.getPath() + "]");
									_commonLibs.add(_libFile.toURI().toURL());
								}
							}
						}
						// 设置通用类文件路径
						_tempFile = new File(_pluginCommonFile, "classes");
						if (_tempFile.exists() && _tempFile.isDirectory()) {
							_LOG.info("插件工厂类加载器加载类路径[" + _tempFile.getPath() + "]");
							_commonLibs.add(_tempFile.toURI().toURL());
						}
					} catch (MalformedURLException e) {
						// 出错就忽略...
					}
				}
				if (!_commonLibs.isEmpty()) {
					URL[] urls = _commonLibs.toArray(new URL[_commonLibs.size()]);
					__PLUGIN_CLASSLOADER = new PluginClassLoader(urls, PluginMeta.class.getClassLoader());
				}
				_LOG.info("插件工厂类加载器对象创建完毕");
			}
			// 若未找到插件通用类路径导致ClassLoader为空，则返回系统默认类加载器
			if (__PLUGIN_CLASSLOADER == null) {
				__PLUGIN_CLASSLOADER = PluginMeta.class.getClassLoader();
			}
		}
		return __PLUGIN_CLASSLOADER;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginFactory#initialize(net.ymate.platform.plugin.IPluginConfig)
	 */
	public void initialize(IPluginConfig config) throws PluginException {
		__PLUGIN_CONFIG = config;
		_LOG.info("初始化默认插件工厂...");
		IPluginParser _parser = config.getPluginParserClassImpl();
		if (_parser != null) {
			_LOG.info("初始化插件配置文件分析器[" + _parser.getClass().getName() + "]");
			_parser.setPluginFactory(this);
			 Map<String, PluginMeta> _metas = _parser.doParser();
			 if (_metas != null && !_metas.isEmpty()) {
				__PLUGINMETA_MAPS.putAll(_metas);
			 }
		} else {
			throw new PluginException("插件主配置文件分析器参数未设置.");
		}
		__IS_INITED = true;
		_LOG.info("默认插件工厂初始化完毕");
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginFactory#getPluginConfig()
	 */
	public IPluginConfig getPluginConfig() {
		return __PLUGIN_CONFIG;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginFactory#isInited()
	 */
	public boolean isInited() {
		return __IS_INITED;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginFactory#destroy()
	 */
	public void destroy() {
		__IS_INITED = false;
		//
		for (String pluginId : __PLUGIN_MAPS.keySet()) {
			IPlugin _p = null;
			try {
				_p = this.__PLUGIN_MAPS.get(pluginId);
				_LOG.info("销毁插件[" + pluginId + "]...");
				_p.destroy();
			} catch (Exception e) {
				Logs.warn("销毁插件 (Id = " + pluginId + ") 时发生异常", RuntimeUtils.unwrapThrow(e));
			} finally {
				_p = null;
			}
		}
		//
		__PLUGIN_MAPS.clear();
		__PLUGINMETA_MAPS.clear();
		__PLUGIN_INTERFACE_WITH_PID.clear();
		__PLUGIN_CONFIG = null;
		__PLUGIN_CLASSLOADER = null;
	}

}