/**
 * <p>文件名:	IEventHandler.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc;



/**
 * <p>
 * IEventHandler
 * </p>
 * <p>
 * MVC框架事件处理器;
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
 *          <td>2012-12-3下午3:48:30</td>
 *          </tr>
 *          </table>
 */
public interface IEventHandler {

	/**
	 * MVC框架初始化时将执行此事件回调
	 */
	public void onInitialized();

	/**
	 * MVC框架销毁时将执行此事件回调
	 */
	public void onDestroyed();

}
