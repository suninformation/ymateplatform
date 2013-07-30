/**
 * <p>文件名:	AbstractModule.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.module.base;

import java.util.Map;

import net.ymate.platform.commons.util.ExpressionUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.configuration.Cfgs;

/**
 * <p>
 * AbstractModule
 * </p>
 * <p>
 * 框架模块加载器接口抽象实现类；
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
 *          <td>2013年7月30日下午9:27:01</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractModule implements IModule {

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.IModule#initialize(java.util.Map)
	 */
	public void initialize(Map<String, String> moduleCfgs) throws Exception {
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.IModule#destroy()
	 */
	public void destroy() throws Exception {
	}

	/**
	 * @param origin 原始字符串
	 * @return 替换${user.dir}变量
	 */
	protected String doParseVariableUserDir(String origin) {
		if (Cfgs.isInited()) {
			return ExpressionUtils.bind(origin).set("user.dir", Cfgs.getUserDir()).getResult();
		}
		return ExpressionUtils.bind(origin).set("user.dir", RuntimeUtils.getRootPath()).getResult();
	}

}
