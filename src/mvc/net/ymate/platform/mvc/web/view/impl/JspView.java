/**
 * <p>文件名:	JspView.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-WebMVC</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.view.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.view.AbstractWebView;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * JspView
 * </p>
 * <p>
 * JSP视图实现类；
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
 *          <td>2011-7-24下午06:49:28</td>
 *          </tr>
 *          </table>
 */
public class JspView extends AbstractWebView {

	/**
	 * JSP文件路径
	 */
	protected String path;

	/**
	 * 构造器
	 */
	public JspView() {
	}

	/**
	 * 构造器
	 * 
	 * @param path
	 */
	public JspView(String path) {
		this.path = path;
	}

	/**
	 * 构造器
	 * 
	 * @param path
	 * @param key
	 * @param value
	 */
	public JspView(String path, String key, Object value) {
		this.path = path;
		this.addAttribute(key, value);
	}

	protected void processPath() {
		if (StringUtils.isNotBlank(getContentType())) {
			WebContext.getResponse().setContentType(getContentType());
		}
		for (Entry<String, Object> entry : getAttributes().entrySet()) {
			WebContext.getRequest().setAttribute(entry.getKey(), entry.getValue());
		}
		//
		String _viewBasePath = this.getBaseViewPath();
		if (StringUtils.isBlank(this.path)) {
			String _mapping = WebContext.getWebRequestContext().getRequestMapping();
			if (_mapping.charAt(0) == '/') {
				_mapping = _mapping.substring(1);
			}
			if (_mapping.endsWith("/")) {
				_mapping.substring(0, _mapping.length() - 1);
			}
			this.path = _viewBasePath + _mapping + ".jsp";
		} else {
			if (!this.path.startsWith("/")) {
				this.path = _viewBasePath + this.path;
			}
			if (this.path.indexOf('?') == -1 && !this.path.endsWith(".jsp")) {
				this.path += ".jsp";
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#renderView()
	 */
	protected void renderView() throws Exception {
		this.processPath();
		WebContext.getRequest().getRequestDispatcher(this.path).forward(WebContext.getRequest(), WebContext.getResponse());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#render(java.io.OutputStream)
	 */
	public void render(final OutputStream output) throws Exception {
		// 输出JSP内容到文件流（生成静态文件），字符编码默认采用UTF-8
		this.processPath();
		final ServletOutputStream _oStream = new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				output.write(b);
			}
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				output.write(b, off, len);
			}
		};
		final PrintWriter _printWriter = new PrintWriter(new OutputStreamWriter(output));
		HttpServletResponse _newResponse = new HttpServletResponseWrapper(WebContext.getResponse()) {
			@Override
			public ServletOutputStream getOutputStream() {
				return _oStream;
			}
			@Override
			public PrintWriter getWriter() {
				return _printWriter;
			}
		};
		_newResponse.setCharacterEncoding(WebMVC.getConfig().getCharsetEncoding());
		WebContext.getRequest().getRequestDispatcher(this.path).include(WebContext.getRequest(), _newResponse);
		_printWriter.flush();
	}

}
