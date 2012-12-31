/**
 * <p>文件名:	IView.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.view;

import java.io.OutputStream;


/**
 * <p>
 * IView
 * </p>
 * <p>
 * MVC视图接口；
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
 *          <td>2012-12-5下午3:11:32</td>
 *          </tr>
 *          </table>
 */
public interface IView {

	/**
     * 视图渲染动作
     * 
     * @throws Exception 抛出任何可能异常
     */
	public void render() throws Exception;

    /**
	 * @param output 视图渲染指定输出流
	 * @throws Exception 抛出任何可能异常
	 */
	public void render(OutputStream output) throws Exception;

}
