/**
 * <p>文件名:	IRequestMethodHandler.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.support;

import java.lang.reflect.Method;
import java.util.Set;

import net.ymate.platform.validation.ValidateResult;

/**
 * <p>
 * IRequestMethodHandler
 * </p>
 * <p>
 * 控制器请求方法参数处理接口；
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
 *          <td>2013年9月13日下午6:27:57</td>
 *          </tr>
 *          </table>
 */
public interface IRequestMethodHandler {

	/**
	 * @return 返回方法对象invoke时所需的参数集合
	 */
	public Object[] getMethodParams();

	/**
	 * @param targetMethod 目标控制器方法对象
	 * @param params 方法对象参数集合
	 * @return 执行参数的有效性验证
	 */
	public Set<ValidateResult> doValidation(Method targetMethod, Object[] params);

}
