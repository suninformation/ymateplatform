/**
 * <p>文件名:	WebControllerBeanFactory.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.support;

import java.util.List;

import net.ymate.platform.commons.beans.IBeanMeta;
import net.ymate.platform.mvc.support.impl.ControllerBeanFactory;

/**
 * <p>
 * WebControllerBeanFactory
 * </p>
 * <p>
 * 基于Web应用的MVC框架控制器对象工厂接口实现类；
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
 *          <td>2012-12-15下午11:57:08</td>
 *          </tr>
 *          </table>
 */
public class WebControllerBeanFactory extends ControllerBeanFactory {

	/**
	 * 构造器
	 * 
	 * @param packageNames 自动扫描的控制器包名称集合
	 */
	public WebControllerBeanFactory(String... packageNames) {
		super(packageNames);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.support.impl.ControllerBeanFactory#loadBeanMetas()
	 */
	protected List<IBeanMeta> loadBeanMetas() {
		return new WebControllerBeanMetaLoader(this.packageNames).loadBeanMetas();
	}

}
