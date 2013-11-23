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

import net.ymate.platform.plugin.IPluginConfig;
import net.ymate.platform.plugin.IPluginExtraParser;
import net.ymate.platform.plugin.IPluginFactory;
import net.ymate.platform.plugin.IPluginParser;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * DefaultPluginConfig
 * </p>
 * <p>
 * 默认插件初始化配置接口实现类；
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
 *          <td>2012-12-2下午4:33:37</td>
 *          </tr>
 *          </table>
 */
public final class DefaultPluginConfig implements IPluginConfig {

	private static final String PLUGIN_MAINIFEST_FILE = "ymate_plugin.xml";

	private IPluginFactory __pluginFactoryImpl;

	private IPluginParser __pluginParserImpl;

	private IPluginExtraParser __extraParserImpl;

	private boolean __includeClassPath;

	private String __pluginHome;

	private String __manifestFile;

	/**
	 * 构造器
	 * @param includeClassPath
	 */
	public DefaultPluginConfig(boolean includeClassPath) {
		this(new DefaultPluginFactory(), new DefaultPluginParser(), null, null, null, includeClassPath);
	}

	/**
	 * 构造器
	 * @param extraParser
	 * @param pluginHome
	 * @param includeClassPath
	 */
	public DefaultPluginConfig(IPluginExtraParser extraParser, String pluginHome, boolean includeClassPath) {
		this(new DefaultPluginFactory(), new DefaultPluginParser(), extraParser, pluginHome, null, includeClassPath);
	}

	/**
	 * 构造器
	 * @param extraParser
	 * @param pluginHome
	 * @param manifestFile
	 * @param includeClassPath
	 */
	public DefaultPluginConfig(IPluginExtraParser extraParser, String pluginHome, String manifestFile, boolean includeClassPath) {
		this(new DefaultPluginFactory(), new DefaultPluginParser(), extraParser, pluginHome, manifestFile, includeClassPath);
	}

	/**
	 * 构造器
	 * @param pluginHome
	 * @param includeClassPath
	 */
	public DefaultPluginConfig(String pluginHome, boolean includeClassPath) {
		this(null, pluginHome, null, includeClassPath);
	}

	/**
	 * 构造器
	 * @param pluginHome
	 * @param manifestFile
	 * @param includeClassPath
	 */
	public DefaultPluginConfig(String pluginHome, String manifestFile, boolean includeClassPath) {
		this(null, pluginHome, manifestFile, includeClassPath);
	}

	/**
	 * 构造器
	 * @param factory
	 * @param parser
	 * @param extraParser
	 * @param pluginHome
	 * @param manifestFile
	 * @param includeClassPath
	 */
	public DefaultPluginConfig(IPluginFactory factory, IPluginParser parser, IPluginExtraParser extraParser, String pluginHome, String manifestFile, boolean includeClassPath) {
		__pluginFactoryImpl = factory;
		__pluginParserImpl = parser;
		__extraParserImpl = extraParser;
		__includeClassPath = includeClassPath;
		__pluginHome = pluginHome;
		__manifestFile = StringUtils.defaultIfBlank(manifestFile, PLUGIN_MAINIFEST_FILE);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginConfig#getPluginFactoryClassImpl()
	 */
	public IPluginFactory getPluginFactoryClassImpl() {
		return __pluginFactoryImpl;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginConfig#getPluginParserClassImpl()
	 */
	public IPluginParser getPluginParserClassImpl() {
		return __pluginParserImpl;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginConfig#getPluginExtraParserClassImpl()
	 */
	public IPluginExtraParser getPluginExtraParserClassImpl() {
		return __extraParserImpl;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginConfig#isIncludeClassPath()
	 */
	public boolean isIncludeClassPath() {
		return __includeClassPath;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginConfig#getPluginHomePath()
	 */
	public String getPluginHomePath() {
		return __pluginHome;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginConfig#getPluginManifestFile()
	 */
	public String getPluginManifestFile() {
		return __manifestFile;
	}

}
