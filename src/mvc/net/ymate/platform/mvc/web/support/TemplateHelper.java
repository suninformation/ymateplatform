/**
 * <p>文件名:	TemplateHelper.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.support;

import java.io.File;
import java.io.IOException;

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
				if (_viewBasePath == null || !_viewBasePath.startsWith("/WEB-INF/")) {
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
	 * @return 插件模板基准路径
	 */
	public static String getPluginViewPath() {
		if (__PLUGIN_VIEW_PATH == null) {
			synchronized (__LOCKER) {
				__PLUGIN_VIEW_PATH = "/WEB-INF/plugins/"; // 因为适应Web环境JSP文件的特殊性，默认写死
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
				TemplateLoader[] _tmpLoaders;
				try {
					_tmpLoaders = new TemplateLoader[] {
							new FileTemplateLoader(new File(RuntimeUtils.getRootPath(), "templates")),
							new FileTemplateLoader(new File(RuntimeUtils.getRootPath(), "plugins"))
						};
				} catch (IOException e) {
					throw new Error(RuntimeUtils.unwrapThrow(e));
				}
				__FREEMARKER_CONFIG.setTemplateLoader(new MultiTemplateLoader(_tmpLoaders));
			}
		}
		return __FREEMARKER_CONFIG;
	}

}
