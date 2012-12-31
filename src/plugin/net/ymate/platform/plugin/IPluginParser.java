/**
 * <p>文件名:	IPluginParser.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Plugin</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.plugin;

import java.util.Map;

/**
 * <p>
 * IPluginParser
 * </p>
 * <p>
 * 插件主配置文件（ymate_plugin.xml）分析器接口定义类；
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
 *          <td>2012-5-23下午3:42:40</td>
 *          </tr>
 *          </table>
 */
public interface IPluginParser {

	public static String PLUGIN_XML_MAIN_CONFIG_FILE = "ymate_plugin.xml";

	/**
	 * @return 执行插件分析，返回插件元描述对象映射
	 * @throws PluginParserException
	 */
	public Map<String, PluginMeta> doParser() throws PluginParserException;

	/**
	 * 设置插件工厂类对象
	 * 
	 * @param factory
	 */
	public void setPluginFactory(IPluginFactory factory);

}
