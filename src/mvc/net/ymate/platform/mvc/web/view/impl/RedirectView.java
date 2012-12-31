/**
 * <p>文件名:	RedirectView.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-WebMVC</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.view.impl;

import javax.servlet.http.HttpServletResponse;

import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.view.AbstractWebView;

/**
 * <p>
 * RedirectView
 * </p>
 * <p>
 * 重定向视图实现类；
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
 *          <td>2011-10-28下午03:49:52</td>
 *          </tr>
 *          </table>
 */
public class RedirectView extends AbstractWebView {

	/**
	 * 重定向URL
	 */
	private String path;

	/**
	 * 构造器
	 * @param path 重定向URL
	 */
	public RedirectView(String path) {
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#renderView()
	 */
	protected void renderView() throws Exception {
		HttpServletResponse response = WebContext.getResponse();
		// 重定向到其它站点
		if (path.startsWith("http://") || path.startsWith("https://")) {}
		// 重定向决对路径
		else if (path.length() > 0 && path.charAt(0) == '/') {
			path = WebContext.getRequest().getContextPath() + path;
		}
		// 重定向相对路径
		else {
			String _rootPath = RuntimeUtils.getRootPath();
			int pos = _rootPath.lastIndexOf('/');
			if (pos > 0) {
				path = _rootPath.substring(0, pos) + "/" + path;
			} else {
				path = "/" + path;
			}
		}
		response.sendRedirect(bindUrl(path));
		response.flushBuffer();
	}

}
