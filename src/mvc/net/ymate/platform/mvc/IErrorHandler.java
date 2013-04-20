/**
 * <p>文件名:	IErrorHandler.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc;

import java.util.Set;

import net.ymate.platform.mvc.view.IView;
import net.ymate.platform.validation.ValidateResult;

/**
 * <p>
 * IErrorHandler
 * </p>
 * <p>
 * MVC框架异常错误处理器接口；
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
 *          <td>2012-12-9下午3:27:11</td>
 *          </tr>
 *          </table>
 */
public interface IErrorHandler {

	/**
	 * 异常时将执行事件回调
	 * 
	 * @param e
	 */
	public void onError(Throwable e);

	/**
	 * @param results 验证器执行结果集合
	 * @return 处理结果数据并返回视图对象，若返回null则由框架默认处理
	 */
	public IView onValidation(Set<ValidateResult> results);

}
