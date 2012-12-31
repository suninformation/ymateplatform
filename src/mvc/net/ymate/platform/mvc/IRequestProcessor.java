/**
 * <p>文件名:	IRequestProcessor.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc;

import net.ymate.platform.mvc.context.IRequestContext;
import net.ymate.platform.mvc.support.IControllerBeanFactory;
import net.ymate.platform.mvc.support.RequestExecutor;

/**
 * <p>
 * IRequestProcessor
 * </p>
 * <p>
 * MVC控制器请求处理器接口；
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
 *          <td>2012-12-10下午10:27:59</td>
 *          </tr>
 *          </table>
 */
public interface IRequestProcessor {

	/**
	 * 初始化请求处理器
	 */
	public void initialize();

	/**
	 * 销毁
	 * @throws Exception
	 */
	public void destroy();

	/**
	 * @return 返回当前MVC控制器工厂类对象，若不存在则创建工厂类并返回
	 */
	public IControllerBeanFactory getControllerBeanFactory();

	/**
	 * 注册并分析控制器
	 * 
	 * @param controller 目标控制器类
	 */
	public void addController(Class<?> controller);

	/**
	 * @param context 请求上下文对象
	 * @return 绑定请求执行器，返回对象可能为空
	 */
	public RequestExecutor bindRequestExecutor(IRequestContext context);

}
