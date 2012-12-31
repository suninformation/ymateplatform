/**
 * <p>文件名:	IWebView.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.view;

import net.ymate.platform.mvc.view.IView;

/**
 * <p>
 * IWebView
 * </p>
 * <p>
 * 基于Web应用的MVC视图接口；
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
 *          <td>2012-12-20下午7:11:43</td>
 *          </tr>
 *          </table>
 */
public interface IWebView extends IView {

	public String VIEW_REDIRECT = "redirect:";

	public String VIEW_FORWARD = "forward:";

	public String VIEW_HTTP_STATUS = "http:";

	public String VIEW_JSP = "jsp:";

	public String VIEW_JSON = "json:";

	public String VIEW_FTL = "ftl:";

	public String VIEW_INLINE_FILE = "inline_file:";

	public String VIEW_FILE = "file:";

	/**
	 * @return 返回视图内容类型
	 */
	public String getContentType();

	/**
	 * 设置视图内容类型
	 * 
	 * @param contentType 内容类型
	 */
	public void setContentType(String contentType);

}
