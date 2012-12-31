/**
 * <p>文件名:	IWebEventHandler.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpSessionEvent;

import net.ymate.platform.mvc.IEventHandler;


/**
 * <p>
 * IWebEventHandler
 * </p>
 * <p>
 * 基于Web应用的MVC框架事件处理器;
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
 *          <td>2012-12-6下午8:42:30</td>
 *          </tr>
 *          </table>
 */
public interface IWebEventHandler extends IEventHandler {

	/**
	 * WebMVC框架启动完毕后将执行此事件回调
	 * 
	 * @param event 事件对象
	 */
	public void onStartup(ServletContextEvent event);

	/**
	 * WebMVC框架停止前将执行此事件回调
	 * 
	 * @param event 事件对象
	 */
	public void onShutdown(ServletContextEvent event);

	/**
	 * Session对象创建完毕将执行此事件回调
	 * 
	 * @param event 事件对象
	 */
	public void onSessionCreated(HttpSessionEvent event);

	/**
	 * Session对象销毁前将执行此事件回调
	 * 
	 * @param event 事件对象
	 */
	public void onSessionDestroyed(HttpSessionEvent event);

	/**
	 * Request对象初始化完毕将执行些事件回调
	 * 
	 * @param event 事件对象
	 */
	public void onRequestInitialized(ServletRequestEvent event);

	/**
	 * Request对象销毁前将执行些事件回调
	 * 
	 * @param event 事件对象
	 */
	public void onRequestDestroyed(ServletRequestEvent event);

}
