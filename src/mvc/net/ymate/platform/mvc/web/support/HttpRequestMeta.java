/**
 * <p>文件名:	HttpRequestMeta.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.support.RequestMeta;
import net.ymate.platform.mvc.web.annotation.RequestMethod;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * HttpRequestMeta
 * </p>
 * <p>
 * 基于Web应用的MVC请求元数据描述对象；
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
 *          <td>2012-12-13下午10:12:58</td>
 *          </tr>
 *          </table>
 */
public class HttpRequestMeta extends RequestMeta {

	protected final Set<HttpMethod> allowHttpMethods;

	protected final Map<String, String> allowHttpHeaders;
	
	protected final Map<String, String> allowHttpParams;

	/**
	 * 构造器
     *
	 * @param target 目标控制器对象
     * @param rootMapping 控制器根请求映射
     * @param method 方法
     * @param interceptors 控制器根拦截器集合
	 */
	public HttpRequestMeta(Object target, String rootMapping, Method method, List<PairObject<Class<IFilter>, String>> interceptors) {
		super(target, rootMapping, method, interceptors);
		RequestMethod _requestMethod = method.getAnnotation(RequestMethod.class);
		if (_requestMethod == null) {
			allowHttpMethods = Collections.emptySet();
			allowHttpHeaders = allowHttpParams = Collections.emptyMap();
		} else {
			allowHttpMethods = new HashSet<HttpMethod>(Arrays.asList(_requestMethod.value()));
			allowHttpHeaders = new HashMap<String, String>();
			for (String _header : _requestMethod.headers()) {
				String[] _headerParts = StringUtils.split(StringUtils.trimToEmpty(_header), "=");
				if (_headerParts.length == 2) {
					allowHttpHeaders.put(_headerParts[0].trim(), _headerParts[1].trim());
				}
			}
			allowHttpParams = new HashMap<String, String>();
			for (String _param : _requestMethod.params()) {
				String[] _paramParts = StringUtils.split(StringUtils.trimToEmpty(_param), "=");
				if (_paramParts.length == 2) {
					allowHttpParams.put(_paramParts[0].trim(), _paramParts[1].trim());
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.support.RequestMeta#getMappingSeparator()
	 */
	protected String getMappingSeparator() {
		return "/";
    }

	/**
	 * @param method 方法名称
	 * @return 判断是否允许method方式的HTTP请求，若允许的请求方式集合为空，则默认不限制
	 */
	public boolean allowHttpMethod(HttpMethod method) {
		// 若允许的请求方式集合为空，则默认不限制
        return this.allowHttpMethods.isEmpty() || this.allowHttpMethods.contains(method);
    }

	/**
	 * @return the allowHttpMethods
	 */
	public Set<HttpMethod> getAllowHttpMethods() {
		return Collections.unmodifiableSet(allowHttpMethods);
	}

	/**
	 * @return the allowHttpHeaders
	 */
	public Map<String, String> getAllowHttpHeaders() {
		return Collections.unmodifiableMap(allowHttpHeaders);
	}

	/**
	 * @return the allowHttpParams
	 */
	public Map<String, String> getAllowHttpParams() {
		return Collections.unmodifiableMap(allowHttpParams);
	}

}
