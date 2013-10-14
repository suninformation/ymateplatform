/**
 * <p>文件名:	DispatcherServlet.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ymate.platform.mvc.web.context.IWebRequestContext;
import net.ymate.platform.mvc.web.support.DispatchHelper;

/**
 * <p>
 * DispatcherServlet
 * </p>
 * <p>
 * WebMVC请求分发调度器；
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
 *          <td>2013年8月18日下午7:04:30</td>
 *          </tr>
 *          </table>
 */
public class DispatcherServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5707999783426434200L;

	private DispatchHelper __dispHelper;
	private ServletContext __servletContext;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		__servletContext = config.getServletContext();
		__dispHelper = new DispatchHelper(config);
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置默认编码和内容类型
		request.setCharacterEncoding(WebMVC.getConfig().getCharsetEncoding());
		response.setCharacterEncoding(WebMVC.getConfig().getCharsetEncoding());
		response.setContentType("text/html;charset=" + WebMVC.getConfig().getCharsetEncoding());
		//
		IWebRequestContext _context = __dispHelper.bindRequestContext((HttpServletRequest) request);
		__dispHelper.doRequestProcess(_context, __servletContext, request, response);
	}

}
