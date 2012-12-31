/**
 * <p>文件名:	ICfgConfig.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.configuration;

import net.ymate.platform.configuration.provider.IConfigurationProvider;

/**
 * <p>
 * ICfgConfig
 * </p>
 * <p>
 * 配置体系框架初始化配置接口；
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
 *          <td>2012-11-28下午9:04:52</td>
 *          </tr>
 *          </table>
 */
public interface ICfgConfig {

	public static String PROJECTS_FORLDER_NAME = "projects";

	public static String MODULES_FORLDER_NAME = "modules";

	/**
	 * @return 返回配置体系根路径
	 */
	public String getConfigHome();

	/**
	 * @return 返回项目名称
	 */
	public String getProjectName();

	/**
	 * @return 返回模块名称
	 */
	public String getModuleName();

	/**
	 * @return 返回配置提供者接口实现类对象
	 */
	public IConfigurationProvider getConfigurationProviderClassImpl();

}
