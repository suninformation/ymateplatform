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
package net.ymate.platform.mvc.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.commons.i18n.II18NEventHandler;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.configuration.Cfgs;
import net.ymate.platform.mvc.MVC;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.impl.WebRequestProcessor;
import net.ymate.platform.mvc.web.support.CookieHelper;
import net.ymate.platform.mvc.web.validation.RequriedValidator;
import net.ymate.platform.validation.Validates;

import org.apache.commons.lang.StringUtils;




/**
 * <p>
 * WebMVC
 * </p>
 * <p>
 * 基于Web应用的MVC框架核心管理器；
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
 *          <td>2012-12-7下午10:23:39</td>
 *          </tr>
 *          </table>
 */
public class WebMVC extends MVC {

//	private static final Log _LOG = LogFactory.getLog(WebMVC.class);

	static {
		Validates.registerValidatorClass(RequriedValidator.class);
	}

	/**
	 * 初始化WebMVC管理器
	 * 
	 * @param config
	 */
	public static void initialize(IWebMvcConfig config) {
		__doInitialize(config, new WebRequestProcessor());
		if (config.isI18n()) {
			final String _localKey = StringUtils.defaultIfEmpty(config.getExtendParams().get("i18n_language_key"), "lang");
			I18N.setEventHandler(new II18NEventHandler() {

				public Locale loadCurrentLocale() {
					// 先尝试取URL参数变量
					String _langStr = (String) WebContext.getContext().get(_localKey);
					if (_langStr == null) {
						// 再尝试从请求参数中获取
						_langStr = WebContext.getRequest().getParameter(_localKey);
						if (_langStr == null) {
							// 最后一次机会，尝试读取Cookies
							BlurObject _langCookie = CookieHelper.create().getCookie(_localKey);
							if (_langCookie != null) {
								_langStr = _langCookie.toStringValue();
							}
						}
					}
					return MVC.localeFromStr(_langStr, MVC.getConfig().getLocale());
				}

				public void onLocaleChanged(Locale locale) {
					CookieHelper.create().setCookie(_localKey, locale.toString());
				}

				public InputStream onLoadProperties(String resourceName) throws IOException {
					if (Cfgs.isInited()) {
						File _resourcefile = Cfgs.search("i18n/" + resourceName);
						if (_resourcefile != null) {
							return new FileInputStream(_resourcefile);
						}
					}
					return null;
				}

			});
		}
	}

	/**
	 * 销毁
	 */
	public static void destory() {
		__doDestroy();
	}

	/**
	 * @return 获取当前配置体系框架初始化配置对象
	 */
	public static IWebMvcConfig getConfig() {
		return (IWebMvcConfig) MVC.getConfig();
	}

}
