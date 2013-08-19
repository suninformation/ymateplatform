/**
 * <p>文件名:	DispatcherFilter.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ymate.platform.mvc.web.context.IWebRequestContext;
import net.ymate.platform.mvc.web.support.DispatchHelper;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * DispatcherFilter
 * </p>
 * <p>
 * WebMVC请求分发调度过滤器；
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
 *          <td>2012-12-23下午11:19:39</td>
 *          </tr>
 *          </table>
 */
public class DispatcherFilter implements Filter {

	private static final String IGNORE = "^.+\\.(jsp|png|gif|jpg|js|css|jspx|jpeg|swf|ico)$";
	
	private Pattern ignorePatern;

	private FilterConfig __filterConfig;

	private DispatchHelper __dispHelper;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		__filterConfig = filterConfig;
		String _regx = StringUtils.defaultIfEmpty(__filterConfig.getInitParameter("ignore"), IGNORE);
        if (!"false".equalsIgnoreCase(_regx)) {
        	ignorePatern = Pattern.compile(_regx, Pattern.CASE_INSENSITIVE);
        }
        __dispHelper = new DispatchHelper(filterConfig);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 设置默认编码和内容类型
		request.setCharacterEncoding(WebMVC.getConfig().getCharsetEncoding());
		response.setContentType("text/html;charset=" + WebMVC.getConfig().getCharsetEncoding());
		//
		IWebRequestContext _context = __dispHelper.bindRequestContext((HttpServletRequest) request);
		if (null == ignorePatern || !ignorePatern.matcher(_context.getUrl()).find()) {
			__dispHelper.doRequestProcess(_context, __filterConfig.getServletContext(), (HttpServletRequest) request, (HttpServletResponse) response);
        } else {
        	chain.doFilter(request, response);
        }
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

}
