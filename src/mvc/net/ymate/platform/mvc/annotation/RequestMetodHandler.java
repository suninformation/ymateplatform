/**
 * <p>文件名:	RequestMetodHandler.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.ymate.platform.mvc.support.IRequestMethodHandler;

/**
 * <p>
 * RequestMetodHandler
 * </p>
 * <p>
 * 控制器请求方法自定义处理程序注解；
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
 *          <td>2013年9月13日下午7:37:48</td>
 *          </tr>
 *          </table>
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMetodHandler {

	/**
	 * @return 指定控制器请求方法参数的自定义处理过程，若提供则屏蔽框架默认参数绑定及验证能力，默认: NULL
	 */
	Class<? extends IRequestMethodHandler> value();

}
