/**
 * <p>文件名:	WebContext.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-WebMVC</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import net.ymate.platform.mvc.context.Context;
import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.context.impl.WebRequestContext;


/**
 * <p>
 * WebContext
 * </p>
 * <p>
 * Web环境上下文封装类，为了能够方便代码移植并脱离Web环境依赖进行开发测试（此功能参考Struts2）；
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
 *          <td>2011-7-24下午10:31:48</td>
 *          </tr>
 *          </table>
 */
public class WebContext extends Context {

	/**
	 * Constant for the action's session.
	 */
	public static final String SESSION = "net.ymate.platform.mvc.context.web.WebContext.Session";

	/**
	 * Constant for the action's application context.
	 */
	public static final String APPLICATION = "net.ymate.platform.mvc.context.web.WebContext.Application";

	/**
	 * Constant for the action's request context
	 */
	public static final String REQUEST = "net.ymate.platform.mvc.context.web.WebContext.Request";

	/**
	 * Constant for the action's parameters.
	 */
	public static final String PARAMETERS = "net.ymate.platform.mvc.context.web.WebContext.Parameters";

	/**
	 * Constant for the action's locale.
	 */
	public static final String LOCALE = "net.ymate.platform.mvc.context.web.WebContext.Locale";

	/**
	 * Constant for the HTTP request object.
	 */
	public static final String HTTP_REQUEST = "net.ymate.platform.mvc.context.web.WebContext.HttpServletRequest";

	/**
	 * Constant for the HTTP response object.
	 */
	public static final String HTTP_RESPONSE = "net.ymate.platform.mvc.context.web.WebContext.HttpServletResponse";

	/**
	 * Constant for the {@link javax.servlet.ServletContext servlet context} object.
	 */
	public static final String SERVLET_CONTEXT = "net.ymate.platform.mvc.context.web.WebContext.ServletContext";

	/**
	 * Constant for the JSP {@link javax.servlet.jsp.PageContext page context}.
	 */
	public static final String PAGE_CONTEXT = "net.ymate.platform.mvc.context.web.WebContext.PageContext";

	/**
	 * WEBMVC请求上下文对象
	 */
	public static final String WEB_REQUEST_CONTEXT = "net.ymate.platform.mvc.context.web.WebContext.WebRequestContext";

	/**
	 * 构造器
	 * 
	 * @param context
	 */
	public WebContext(Map<String, Object> context, IWebRequestContext requestContext) {
		super(context);
		put(WEB_REQUEST_CONTEXT, requestContext);
	}

	// ------------------------------------------------------

	/**
	 * Sets the WebContext for the current thread.
	 * 
	 * @param context the WebContext.
	 */
	public static void setContext(WebContext context) {
		__THREAD_LOCAL_CONTEXT.set(context);
	}

	/**
	 * Returns the WebContext specific to the current thread.
	 * 
	 * @return the WebContext for the current thread, is never <tt>null</tt>.
	 */
	public static WebContext getContext() {
		return (WebContext) __THREAD_LOCAL_CONTEXT.get();
	}

	public static WebRequestContext getWebRequestContext() {
		return (WebRequestContext) WebContext.getContext().get(WEB_REQUEST_CONTEXT);
	}

	/**
	 * Returns the HTTP page context.
	 * 
	 * @return the HTTP page context.
	 */
	public static PageContext getPageContext() {
		return (PageContext) WebContext.getContext().get(PAGE_CONTEXT);
	}

	/**
	 * Sets the HTTP servlet request object.
	 * 
	 * @param request the HTTP servlet request object.
	 */
	public static void setRequest(HttpServletRequest request) {
		WebContext.getContext().put(HTTP_REQUEST, request);
	}

	/**
	 * Gets the HTTP servlet request object.
	 * 
	 * @return the HTTP servlet request object.
	 */
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) WebContext.getContext().get(HTTP_REQUEST);
	}

	/**
	 * Sets the HTTP servlet response object.
	 * 
	 * @param response the HTTP servlet response object.
	 */
	public static void setResponse(HttpServletResponse response) {
		WebContext.getContext().put(HTTP_RESPONSE, response);
	}

	/**
	 * Gets the HTTP servlet response object.
	 * 
	 * @return the HTTP servlet response object.
	 */
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) WebContext.getContext().get(HTTP_RESPONSE);
	}

	/**
	 * Gets the servlet context.
	 * 
	 * @return the servlet context.
	 */
	public static ServletContext getServletContext() {
		return (ServletContext) WebContext.getContext().get(SERVLET_CONTEXT);
	}

	/**
	 * Sets the current servlet context object
	 * 
	 * @param servletContext The servlet context to use
	 */
	public static void setServletContext(ServletContext servletContext) {
		WebContext.getContext().put(SERVLET_CONTEXT, servletContext);
	}

	// ------------------------------------------------------

	/**
	 * Sets the action's application context.
	 * 
	 * @param application the action's application context.
	 */
	public void setApplication(Map<String, Object> application) {
		put(APPLICATION, application);
	}

	/**
	 * Returns a Map of the ServletContext when in a servlet environment or a generic application level Map otherwise.
	 * 
	 * @return a Map of ServletContext or generic application level Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getApplication() {
		return (Map<String, Object>) get(APPLICATION);
	}

	/**
	 * Sets the Locale for the current action.
	 * 
	 * @param locale the Locale for the current action.
	 */
	public void setLocale(Locale locale) {
		put(LOCALE, locale);
	}

	/**
	 * Gets the Locale of the current action. If no locale was ever specified the platform's {@link java.util.Locale#getDefault() default locale} is used.
	 * 
	 * @return the Locale of the current action.
	 */
	public Locale getLocale() {
		Locale locale = (Locale) get(LOCALE);
		if (locale == null) {
			locale = Locale.getDefault();
			setLocale(locale);
		}
		return locale;
	}

	/**
	 * Sets the action parameters.
	 * 
	 * @param parameters the parameters for the current action.
	 */
	public void setParameters(Map<String, Object> parameters) {
		put(PARAMETERS, parameters);
	}

	/**
	 * Returns a Map of the HttpServletRequest parameters when in a servlet environment or a generic Map of parameters otherwise.
	 * 
	 * @return a Map of HttpServletRequest parameters or a multipart map when in a servlet environment, or a generic Map of parameters otherwise.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getParameters() {
		return (Map<String, Object>) get(PARAMETERS);
	}

	/**
	 * Sets a map of action session values.
	 * 
	 * @param session the session values.
	 */
	public void setSession(Map<String, Object> session) {
		put(SESSION, session);
	}

	/**
	 * Gets the Map of HttpSession values when in a servlet environment or a generic session map otherwise.
	 * 
	 * @return the Map of HttpSession values when in a servlet environment or a generic session map otherwise.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSession() {
		return (Map<String, Object>) get(SESSION);
	}

	/// ----------------------------------------------------

	/**
	 * 
	 * Create a context map containing all the wrapped request objects
	 * 
	 * @param servletContext
	 * @param request
	 * @param response
	 * @param locale
	 * @return A map of context objects
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Object> createWebContextMap(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		// request map wrapping the http request objects
		Map requestMap = new RequestMap(request);
		// parameters map wrapping the http parameters. ActionMapping parameters are now handled and applied separately
		Map params = new HashMap(request.getParameterMap());
		// session map wrapping the http session
		Map session = new SessionMap(request);
		// application map wrapping the ServletContext
		Map application = new ApplicationMap(servletContext);
		Map<String, Object> extraContext = createWebContextMap(servletContext, request, response, requestMap, params, session, application, locale);
		//
		return extraContext;
	}

	/**
	 * Merge all application and servlet attributes into a single <tt>HashMap</tt> to represent the entire <tt>Action</tt> context.
	 * 
	 * @param requestMap a Map of all request attributes.
	 * @param parameterMap a Map of all request parameters.
	 * @param sessionMap a Map of all session attributes.
	 * @param applicationMap a Map of all servlet context attributes.
	 * @param request The RequestContext
	 * @return a HashMap representing the <tt>Action</tt> context.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap<String, Object> createWebContextMap(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, Map requestMap, Map parameterMap, Map sessionMap, Map applicationMap, Locale locale) {
		HashMap<String, Object> _extraContext = new HashMap<String, Object>();
		_extraContext.put(WebContext.PARAMETERS, new HashMap(parameterMap));
		_extraContext.put(WebContext.REQUEST, requestMap);
		_extraContext.put(WebContext.SESSION, sessionMap);
		_extraContext.put(WebContext.APPLICATION, applicationMap);
		_extraContext.put(WebContext.LOCALE, locale == null ? (WebMVC.getConfig().getLocale() != null ? WebMVC.getConfig().getLocale() : request.getLocale()) : locale);
		_extraContext.put(WebContext.HTTP_REQUEST, request);
		_extraContext.put(WebContext.HTTP_RESPONSE, response);
		_extraContext.put(WebContext.SERVLET_CONTEXT, servletContext);
		//
		return _extraContext;
	}

}
