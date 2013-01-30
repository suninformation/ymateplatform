/**
 * <p>文件名:	TextView.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-WebMVC</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.view.impl;

import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.view.AbstractWebView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * TextView
 * </p>
 * <p>
 * 文本视图实现类；
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
 *          <td>2011-10-23上午11:15:43</td>
 *          </tr>
 *          </table>
 */
public class TextView extends AbstractWebView {

	/**
	 * 文本内容
	 */
	protected String text;

	/**
	 * 构造器
	 */
	public TextView() {
		text = "";
	}

	/**
	 * 构造器
	 * 
	 * @param text 输出文本
	 */
	public TextView(String text) {
		this.text = text;
	}

	/**
	 * 构造器
	 * 
	 * @param text 输出文本
	 * @param contentType 内容类型
	 */
	public TextView(String text, String contentType) {
		this.text = text;
		setContentType(contentType);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#renderView()
	 */
	protected void renderView() throws Exception {
		if (StringUtils.isNotBlank(getContentType())) {
			WebContext.getResponse().setContentType(getContentType());
		}
		IOUtils.write(this.text, WebContext.getResponse().getOutputStream());
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
