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
package net.ymate.platform.mvc.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.ymate.platform.mvc.IErrorHandler;
import net.ymate.platform.mvc.IEventHandler;
import net.ymate.platform.mvc.IMvcConfig;
import net.ymate.platform.plugin.IPluginExtraParser;


/**
 * <p>
 * DefaultMvcConfig
 * </p>
 * <p>
 * MVC框架初始化配置接口默认实现类；
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
 *          <td>2012-12-7下午9:19:58</td>
 *          </tr>
 *          </table>
 */
public class DefaultMvcConfig implements IMvcConfig {

	private IEventHandler __eventHandler;

	private IErrorHandler __errorHandler;

	private Map<String, String> __extendParams = new HashMap<String, String>();

	private Locale __locale;

	private boolean __i18n;

	private String __charsetEncoding;

	private IPluginExtraParser __extraParser;

	private String __pluginHome;

	private String[] __controllerPackages;

	/**
	 * 构造器
	 * @param handler
	 * @param extraParser
	 * @param errorHandler
	 * @param locale
	 * @param i18n
	 * @param charsetEncoding
	 * @param pluginHome
	 * @param extendParams
	 * @param controllerPackages
	 */
	public DefaultMvcConfig(IEventHandler handler,
			IPluginExtraParser extraParser, IErrorHandler errorHandler,
			Locale locale, boolean i18n, String charsetEncoding, String pluginHome, Map<String, String> extendParams, String[] controllerPackages) {
		this.__eventHandler = handler;
		this.__extraParser = extraParser;
		this.__errorHandler = errorHandler;
		this.__locale = locale;
		this.__i18n = i18n;
		this.__charsetEncoding = charsetEncoding;
		if (extendParams != null && !extendParams.isEmpty()) {
			this.__extendParams.putAll(extendParams);
		}
		this.__pluginHome = pluginHome;
		this.__controllerPackages = controllerPackages;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IMvcConfig#getEventHandlerClassImpl()
	 */
	public IEventHandler getEventHandlerClassImpl() {
		return __eventHandler;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IMvcConfig#getErrorHandlerClassImpl()
	 */
	public IErrorHandler getErrorHandlerClassImpl() {
		return __errorHandler;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IMvcConfig#getExtendParams()
	 */
	public Map<String, String> getExtendParams() {
		return __extendParams;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IMvcConfig#getLocale()
	 */
	public Locale getLocale() {
		return __locale;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IMvcConfig#isI18n()
	 */
	public boolean isI18n() {
		return __i18n;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IMvcConfig#getCharsetEncoding()
	 */
	public String getCharsetEncoding() {
		return __charsetEncoding;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IMvcConfig#getPluginHome()
	 */
	public String getPluginHome() {
		return __pluginHome;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IMvcConfig#getPluginExtraParser()
	 */
	public IPluginExtraParser getPluginExtraParser() {
		return __extraParser;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.IMvcConfig#getControllerPackages()
	 */
	public String[] getControllerPackages() {
		return __controllerPackages;
	}

}
