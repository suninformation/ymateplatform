/**
 * <p>文件名:	IGuiceModule.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.module.base;

import java.util.Map;

/**
 * <p>
 * IGuiceModule
 * </p>
 * <p>
 * 框架模块加载器接口类；
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
 *          <td>2012-11-24下午6:13:22</td>
 *          </tr>
 *          </table>
 */
public interface IModule {

	/**
	 * 模块初始化，启动/完成模块加载过程
	 * 
	 * @param moduleCfgs 模块配置映射
	 */
	void initialize(Map<String, String> moduleCfgs) throws Exception;

	/**
	 * 销毁模块
	 * 
	 * @throws Exception
	 */
	void destroy() throws Exception;

}
