/**
 * <p>文件名:	ForwardView.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-WebMVC</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.view.impl;

import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.view.AbstractWebView;

/**
 * <p>
 * ForwardView
 * </p>
 * <p>
 * 内部重定向（请求转发）视图实现类；
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
 *          <td>2011-10-28下午05:15:10</td>
 *          </tr>
 *          </table>
 */
public class ForwardView extends AbstractWebView {

	/**
	 * 重定向URL
	 */
	private String path;

	/**
	 * 构造器
	 * 
	 * @param path 转发URL
	 */
	public ForwardView(String path) {
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#renderView()
	 */
	protected void renderView() throws Exception {
		// 绝对路径 : 以 '/' 开头的路径不增加 '/WEB-INF'
		if (path.charAt(0) == '/') {
		}
		// 包名形式的路径
		else {
			path = "/WEB-INF/" + path;
		}
		// 执行 Forward
		WebContext.getRequest().getRequestDispatcher(bindUrl(path)).forward(WebContext.getRequest(), WebContext.getResponse());
	}

}
