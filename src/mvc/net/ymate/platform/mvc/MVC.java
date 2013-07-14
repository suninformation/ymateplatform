/**
 * <p>文件名:	MVC.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc;

import java.util.Locale;

import net.ymate.platform.commons.i18n.I18N;
import net.ymate.platform.mvc.context.IRequestContext;
import net.ymate.platform.mvc.impl.DefaultRequestProcessor;
import net.ymate.platform.mvc.support.RequestExecutor;
import net.ymate.platform.plugin.IPluginFactory;
import net.ymate.platform.plugin.Plugins;
import net.ymate.platform.plugin.impl.DefaultPluginConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * <p>
 * MVC
 * </p>
 * <p>
 * MVC框架核心管理器；
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
 *          <td>2012-12-7下午10:55:58</td>
 *          </tr>
 *          </table>
 */
public abstract class MVC {

	private static final Log _LOG = LogFactory.getLog(MVC.class);

	/**
	 * 当前MVC框架初始化配置对象
	 */
	private static IMvcConfig __MVC_CONFIG;

	private static IPluginFactory __PLUGIN_FACTORY;

	private static IRequestProcessor __META_PROCESSOR;

	private static boolean __IS_INITED;

	/**
	 * 初始化WebMVC管理器
	 * 
	 * @param config MVC框架初始化配置对象
	 * @param processor MVC控制器请求处理器对象
	 */
	protected static void __doInitialize(IMvcConfig config, IRequestProcessor processor) {
		if (!__IS_INITED) {
			_LOG.info("初始化MVC框架...");
			__MVC_CONFIG = config;
			__META_PROCESSOR = processor == null ? new DefaultRequestProcessor() : processor;
			__META_PROCESSOR.initialize();
			//
			__PLUGIN_FACTORY = Plugins.createPluginFactory(new DefaultPluginConfig(__MVC_CONFIG.getPluginExtraParser(), __MVC_CONFIG.getPluginHome()));
			__IS_INITED = true;
			if (__MVC_CONFIG.isI18n()) {
				// 初始化国际化资源管理器
				I18N.initialize(__MVC_CONFIG.getLocale());
			}
			//
			if (__MVC_CONFIG.getEventHandlerClassImpl() != null) {
				_LOG.info("回调MVC框架事件处理器接口, 触发onInitialized事件...");
				__MVC_CONFIG.getEventHandlerClassImpl().onInitialized();
			}
			_LOG.info("MVC框架初始化完毕");
		}
	}

	protected static void __doDestroy() {
		if (__IS_INITED) {
			__PLUGIN_FACTORY.destroy();
			__META_PROCESSOR.destroy();
			__IS_INITED = false;
			if (__MVC_CONFIG.isI18n()) {
				I18N.destroy();
			}
		}
	}

	/**
	 * @return 获取当前配置体系框架初始化配置对象
	 */
	public static IMvcConfig getConfig() {
		return __MVC_CONFIG;
	}

	/**
	 * @return 判断是否已初始化完成
	 */
	public static boolean isInited() {
		return __IS_INITED;
	}

	/**
	 * @return 返回当前插件工厂对象
	 */
	public static IPluginFactory getPluginFactory() {
		return __PLUGIN_FACTORY;
	}

	/**
	 * 注册控制器类
	 * 
	 * @param clazz 目标控制器类
	 */
	public static void registerController(Class<?> clazz) {
		__META_PROCESSOR.addController(clazz);
	}

	/**
	 * @param context 请求上下文对象
	 * @return 绑定请求执行器，返回对象可能为空
	 */
	public static RequestExecutor processRequestMapping(IRequestContext context) {
		return __META_PROCESSOR.bindRequestExecutor(context);
	}

	/**
	 * Builds a {@link java.util.Locale} from a String of the form en_US_foo into a Locale with language "en", country "US" and variant "foo". This will parse the output of {@link java.util.Locale#toString()}.
	 * 
	 * @param localeStr The locale String to parse.
	 * @param defaultLocale The locale to use if localeStr is <tt>null</tt>.
	 * @return requested Locale
	 */
	public static Locale localeFromStr(String localeStr, Locale defaultLocale) {
		if (StringUtils.isBlank(localeStr)) {
			return defaultLocale != null ? defaultLocale : Locale.getDefault();
		}
		int index = localeStr.indexOf('_');
		if (index < 0) {
			return new Locale(localeStr);
		}
		String language = localeStr.substring(0, index);
		if (index == localeStr.length()) {
			return new Locale(language);
		}
		localeStr = localeStr.substring(index + 1);
		index = localeStr.indexOf('_');
		if (index < 0) {
			return new Locale(language, localeStr);
		}
		String country = localeStr.substring(0, index);
		if (index == localeStr.length()) {
			return new Locale(language, country);
		}
		localeStr = localeStr.substring(index + 1);
		return new Locale(language, country, localeStr);
	}

}
