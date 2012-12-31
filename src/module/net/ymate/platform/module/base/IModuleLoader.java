/**
 * <p>文件名:	IModuleLoader.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.module.base;

import java.util.Properties;


/**
 * <p>
 * IModuleLoader
 * </p>
 * <p>
 * 模块加载器接口类；
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
 *          <td>2012-11-30下午7:01:22</td>
 *          </tr>
 *          </table>
 */
public interface IModuleLoader {

	/**
	 * 初始化模块加载器
	 * 
	 * @param configs 配置参数集合
	 * @throws Exception
	 */
	public void initialize(Properties configs) throws Exception;

	/**
	 * 销毁
	 * 
	 * @throws Exception
	 */
	public void destroy() throws Exception;

}
