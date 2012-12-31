/**
 * <p>文件名:	IPluginConfig.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.plugin;


/**
 * <p>
 * IPluginConfig
 * </p>
 * <p>
 * 插件初始化配置接口；
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
 *          <td>2012-11-30下午6:35:14</td>
 *          </tr>
 *          </table>
 */
public interface IPluginConfig {

	/**
	 * @return 返回插件工厂接口实现类对象
	 */
	public IPluginFactory getPluginFactoryClassImpl();

	/**
	 * @return 返回插件主配置文件分析器接口实现类对象
	 */
	public IPluginParser getPluginParserClassImpl();

	/**
	 * @return 返回插件附加配置分析器接口实现类对象
	 */
	public IPluginExtraParser getPluginExtraParserClassImpl();

	/**
	 * @return 返回插件存放路径
	 */
	public String getPluginHomePath();

}
