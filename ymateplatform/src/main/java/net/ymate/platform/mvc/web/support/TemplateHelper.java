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
package net.ymate.platform.mvc.web.support;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.mvc.web.WebMVC;

import org.apache.commons.lang.StringUtils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * <p>
 * TemplateHelper
 * </p>
 * <p>
 * 模板操作助手类;
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
 *          <td>2013-7-9下午10:27:36</td>
 *          </tr>
 *          </table>
 */
public class TemplateHelper {

	private static Configuration __FREEMARKER_CONFIG;

	private static String __ROOT_VIEW_PATH;

	private static String __PLUGIN_VIEW_PATH;

	private static Object __LOCKER = new Object();

	/**
	 * @return 模板基准路径并以'/WEB-INF'开始，以'/'结束
	 */
	public static String getRootViewPath() {
		if (__ROOT_VIEW_PATH == null) {
			synchronized (__LOCKER) {
				String _viewBasePath = StringUtils.trimToNull(WebMVC.getConfig().getViewPath());
				if (_viewBasePath == null || !(_viewBasePath = _viewBasePath.replaceAll("\\\\", "/")).startsWith("/WEB-INF/")) {
					_viewBasePath = "/WEB-INF/templates/";
				} else if (!_viewBasePath.endsWith("/")) {
					_viewBasePath += "/";
				}
				__ROOT_VIEW_PATH = _viewBasePath;
			}
		}
		return __ROOT_VIEW_PATH;
	}

	/**
	 * @return 插件模板基准路径，以'/WEB-INF'开始，以'/'结束
	 */
	public static String getPluginViewPath() {
		if (__PLUGIN_VIEW_PATH == null) {
			synchronized (__LOCKER) {
				String _pHome = WebMVC.getConfig().getPluginHome();
				if (StringUtils.isNotBlank(_pHome) && (_pHome = _pHome.replaceAll("\\\\", "/")).contains("/WEB-INF/")) {
                    __PLUGIN_VIEW_PATH = StringUtils.substring(_pHome, _pHome.indexOf("/WEB-INF/"));
                    if (!__PLUGIN_VIEW_PATH.endsWith("/")) {
                        __PLUGIN_VIEW_PATH += "/";
                    }
				} else {
					__PLUGIN_VIEW_PATH = "/WEB-INF/plugins/"; // 为了适应Web环境JSP文件的特殊性(即不能引用工程路径外的JSP文件), 建议采用默认"/WEB-INF/plugins/
				}
			}
		}
		return __PLUGIN_VIEW_PATH;
	}

	/**
	 * @return 获取Freemarker模板引擎配置对象, 若不存在则创建(全局唯一)
	 */
	public static Configuration getFreemarkerConfiguration() {
		if (__FREEMARKER_CONFIG == null) {
			synchronized (__LOCKER) {
				__FREEMARKER_CONFIG = new Configuration();
				__FREEMARKER_CONFIG.setDefaultEncoding("UTF-8");
				__FREEMARKER_CONFIG.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		        //
				List<TemplateLoader> _tmpLoaders = new ArrayList<TemplateLoader>();
				try {
					_tmpLoaders.add(new FileTemplateLoader(new File(RuntimeUtils.getRootPath(), StringUtils.substringAfter(getRootViewPath(), "/WEB-INF/"))));
					//
					_tmpLoaders.add(new FileTemplateLoader(new File(RuntimeUtils.getRootPath(), StringUtils.substringAfter(getPluginViewPath(), "/WEB-INF/"))));
                    //
                    _tmpLoaders.add(new FileTemplateLoader((new File(WebMVC.getConfig().getPluginHome()))));
				} catch (IOException e) {
					throw new Error(RuntimeUtils.unwrapThrow(e));
				}
				__FREEMARKER_CONFIG.setTemplateLoader(new MultiTemplateLoader(_tmpLoaders.toArray(new TemplateLoader[0])));
			}
		}
		return __FREEMARKER_CONFIG;
	}

}
