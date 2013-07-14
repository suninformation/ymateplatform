/**
 * <p>文件名:	I18N.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import net.ymate.platform.commons.util.RuntimeUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * I18N
 * </p>
 * <p>
 * 国际化资源管理器；
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
 *          <td>2013-4-14下午1:36:11</td>
 *          </tr>
 *          </table>
 */
public class I18N {

	private static final Log _LOG = LogFactory.getLog(I18N.class);

	protected static Map<Locale, Map<String, Properties>> __RESOURCES_CAHCES = new ConcurrentHashMap<Locale, Map<String, Properties>>();

	protected static Locale __DEFAULT_LOCALE;

	protected static II18NEventHandler __EVENT_HANDLER;

	protected static boolean __IS_INITED;

	protected static ThreadLocal<Locale> __CURRENT_LOCALE = new ThreadLocal<Locale>();

	/**
	 * 初始化
	 * 
	 * @param defaultLocale 默认语言，若为空则采用JVM默认语言
	 */
	public static void initialize(Locale defaultLocale) {
		if (!__IS_INITED) {
			__DEFAULT_LOCALE = defaultLocale == null ? Locale.getDefault() : defaultLocale;
			__IS_INITED = true;
			_LOG.info("初始化国际化资源管理器I18N [" + __DEFAULT_LOCALE+ "]");
		}
	}

	/**
	 * @return 判断是否已初始化
	 */
	public static boolean isInited() {
		return __IS_INITED;
	}

	/**
	 * 重置(清空)缓存资源
	 */
	public static void reset() {
		if (__IS_INITED) {
			__RESOURCES_CAHCES.clear();
		}
	}

	/**
	 * @param eventHandler 设置事件监听处理器
	 */
	public static void setEventHandler(II18NEventHandler eventHandler) {
		__EVENT_HANDLER = eventHandler;
	}

	/**
	 * @return 获取当前本地线程语言，若为空则返回默认
	 */
	public static Locale current() {
		if (!__IS_INITED) {
			throw new UnsupportedOperationException("I18N服务尚未初始化");
		}
		Locale _locale = __CURRENT_LOCALE.get();
		if (_locale == null) {
			if (__EVENT_HANDLER != null) {
				_locale = __EVENT_HANDLER.loadCurrentLocale();
			}
		}
		return _locale == null ? __DEFAULT_LOCALE : _locale;
	}

	/**
	 * @param locale
	 * @return 修改当前线程语言设置，不触发事件，并返回修改结果
	 */
	public static boolean set(Locale locale) {
		if (locale != null && !current().equals(locale)) {
			__CURRENT_LOCALE.set(locale);
			return true;
		}
		return false;
	}

	/**
	 * 修改当前线程语言设置并触发onLocaleChanged事件
	 * 
	 * @param locale
	 */
	public static void change(Locale locale) {
		if (set(locale)) {
			if (__EVENT_HANDLER != null) {
				__EVENT_HANDLER.onLocaleChanged(locale);
			}
		}
	}

	/**
	 * @param resourceName 资源名称
	 * @param key 键值
	 * @return 加载资源并提取key指定的值
	 */
	public static String load(String resourceName, String key) {
		return load(resourceName, key, "");
	}

	/**
	 * @param resourceName 资源名称
	 * @param key 键值
	 * @param defaultValue 默认值
	 * @return 加载资源并提取key指定的值
	 */
	public static String load(String resourceName, String key, String defaultValue) {
		Map<String, Properties> _cache = __RESOURCES_CAHCES.get(current());
		Properties _prop = _cache != null? _cache.get(resourceName) : null;
		if (_prop == null) {
			if (__EVENT_HANDLER != null) {
				try {
					List<String> _localeResourceNames = resourceNames(current(), resourceName);
					InputStream _inputStream = null;
					for (String _localeResourceName : _localeResourceNames) {
						_inputStream = __EVENT_HANDLER.onLoadProperties(_localeResourceName);
						if (_inputStream != null) {
							break;
						}
					}
					if (_inputStream == null) {
						for (String _localeResourceName : _localeResourceNames) {
							_inputStream = I18N.class.getClassLoader().getResourceAsStream(_localeResourceName);
							if (_inputStream != null) {
								break;
							}
						}
					}
					if (_inputStream != null) {
						_prop = new Properties();
						_prop.load(_inputStream);
					}
					if (_prop != null && !_prop.isEmpty()) {
						if (_cache == null) {
							__RESOURCES_CAHCES.put(current(), new ConcurrentHashMap<String, Properties>());
						}
						__RESOURCES_CAHCES.get(current()).put(resourceName, _prop);
					}
				} catch (IOException e) {
					_LOG.warn("", RuntimeUtils.unwrapThrow(e));
				}
			}
		}
		String _returnValue = null;
		if (_prop != null) {
			_returnValue = _prop.getProperty(key, defaultValue);
		}
		return StringUtils.defaultIfEmpty(_returnValue, defaultValue);
	}

	/**
	 * @param locale
	 * @param resourceName
	 * @return 拼装资源文件名称集合
	 */
	protected static List<String> resourceNames(Locale locale, String resourceName) {
		List<String> _names = new ArrayList<String>();
		_names.add(resourceName + ".properties");
        String _localeKey =  (locale == null) ? "" : locale.toString();
        if (_localeKey.length() > 0) {
        	resourceName += ("_" + _localeKey);
        }
        _names.add(0, resourceName += ".properties");
        return _names;
    }

	/**
	 * 销毁
	 */
	public static void destroy() {
		if (__IS_INITED) {
			__RESOURCES_CAHCES.clear();
			__IS_INITED = false;
		}
	}

}
