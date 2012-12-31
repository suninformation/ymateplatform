/**
 * <p>文件名:	IWebRequestContext.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.context;

import net.ymate.platform.mvc.context.IRequestContext;

/**
 * <p>
 * IWebRequestContext
 * </p>
 * <p>
 * WebMVC请求上下文接口，分析请求路径，仅返回控制器请求映射；
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
 *          <td>2012-12-24上午4:12:29</td>
 *          </tr>
 *          </table>
 */
public interface IWebRequestContext extends IRequestContext {

	/**
	 * @return 返回原始URL请求路径
	 */
	public String getUrl();

	/**
	 * @return 返回URL前缀
	 */
	public String getPrefix();

	/**
	 * @return 返回URL后缀(扩展名称)
	 */
	public String getSuffix();

}
