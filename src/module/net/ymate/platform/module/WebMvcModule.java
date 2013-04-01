/**
 * <p>文件名:	WebMvcModule.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.module;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.ResourceUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.module.base.IModule;
import net.ymate.platform.mvc.MVC;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.web.IWebErrorHandler;
import net.ymate.platform.mvc.web.IWebEventHandler;
import net.ymate.platform.mvc.web.IWebMultipartHandler;
import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.impl.WebMvcConfig;
import net.ymate.platform.plugin.IPluginExtraParser;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * WebMvcModule
 * </p>
 * <p>
 * WebMvc模块加载器接口实现类；
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
 *          <td>2012-12-23下午7:05:33</td>
 *          </tr>
 *          </table>
 */
public class WebMvcModule implements IModule {

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.IModule#initialize(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void initialize(Map<String, String> moduleCfgs) throws Exception {
		IWebEventHandler _eventHandler = ClassUtils.impl(moduleCfgs.get("base.event_handler_class"), IWebEventHandler.class, WebMvcModule.class);
		IPluginExtraParser _extraParser = ClassUtils.impl(moduleCfgs.get("base.plugin_extra_parser_class"), IPluginExtraParser.class, WebMvcModule.class);
		IWebErrorHandler _errorHandler = ClassUtils.impl(moduleCfgs.get("base.error_handler_class"), IWebErrorHandler.class, WebMvcModule.class);
		IWebMultipartHandler _multipartHandler = ClassUtils.impl(moduleCfgs.get("base.multipart_handler_class"), IWebMultipartHandler.class, WebMvcModule.class);
		//
		Locale _locale = MVC.localeFromStr(moduleCfgs.get("base.locale"), null);
		String _charsetEncoding = StringUtils.defaultIfEmpty(moduleCfgs.get("base.charset_encoding"), "UTF-8");
		//
		List<Class<IFilter>> _extraFilters = new ArrayList<Class<IFilter>>();
		for (String _extraFilter : StringUtils.split(StringUtils.trimToEmpty(moduleCfgs.get("base.extra_filters")), "|")) {
			Class<?> _filterClass = ResourceUtils.loadClass(_extraFilter, WebMvcModule.class);
			if (_filterClass != null && ClassUtils.isInterfaceOf(_filterClass, IFilter.class)) {
				_extraFilters.add((Class<IFilter>) _filterClass);
			}
		}
		//
		Map<String, String> _extendParams = new HashMap<String, String>();
		for (String _cfgKey : moduleCfgs.keySet()) {
			if (_cfgKey.startsWith("params")) {
				_extendParams.put(StringUtils.substring(_cfgKey, 7), moduleCfgs.get(_cfgKey));
			}
		}
		//
		String _pluginHome = moduleCfgs.get("base.plugin_home");
		if (StringUtils.isNotBlank(_pluginHome) && _pluginHome.startsWith("/WEB-INF/")) {
			File _pluginHomeFile = new File(RuntimeUtils.getRootPath(), StringUtils.substringAfter(_pluginHome, "/WEB-INF/"));
			if (_pluginHomeFile.exists() && _pluginHomeFile.isDirectory()) {
				_pluginHome = _pluginHomeFile.getPath();
			}
//			以下代码在百度的BAE中执行会抛出权限访问异常，反正也没必要就注掉吧
//			if (_pluginHomeFile.exists() && _pluginHomeFile.isDirectory() && _pluginHomeFile.canRead()) {
//				if (_pluginHomeFile.canWrite()) {
//					FileUtils.fixAndMkDir(_pluginHomeFile.getPath() + "/.plugin");
//				}
//				_pluginHome = _pluginHomeFile.getPath();
//			}
		}
		//
		WebMvcConfig _config = new WebMvcConfig(_eventHandler, _extraParser, _errorHandler, _locale, _charsetEncoding, _pluginHome,  _extendParams, StringUtils.split(moduleCfgs.get("base.controller_packages"), '|'));
		//
		_config.setMultipartHandlerClassImpl(_multipartHandler);
		_config.setRestfulModel(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("base.restful_model"), "false")).toBooleanValue());
		_config.setConventionModel(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("base.convention_model"), "true")).toBooleanValue());
		_config.setUrlSuffix(StringUtils.defaultIfEmpty(moduleCfgs.get("base.url_suffix"), ""));
		_config.setViewPath(StringUtils.defaultIfEmpty(moduleCfgs.get("base.view_path"), ""));
		_config.setExtraFilters(_extraFilters);
		_config.setUploadTempDir(StringUtils.defaultIfEmpty(moduleCfgs.get("upload.temp_dir"), System.getProperty("java.io.tmpdir")));
		_config.setUploadFileSizeMax(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("upload.file_size_max"), "-1")).toIntValue());
		_config.setUploadTotalSizeMax(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("upload.total_size_max"), "-1")).toIntValue());
		_config.setUploadSizeThreshold(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("upload.size_threshold"), "10240")).toIntValue());
		//
		_config.setCookiePrefix(StringUtils.defaultIfEmpty(moduleCfgs.get("cookie.prefix"), ""));
		_config.setCookieDomain(StringUtils.defaultIfEmpty(moduleCfgs.get("cookie.domain"), ""));
		_config.setCookiePath(StringUtils.defaultIfEmpty(moduleCfgs.get("cookie.path"), "/"));
		_config.setCookieAuthKey(StringUtils.defaultIfEmpty(moduleCfgs.get("cookie.auth_key"), ""));
		//
		WebMVC.initialize(_config);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.IModule#destroy()
	 */
	public void destroy() throws Exception {
		WebMVC.destory();
	}

}
