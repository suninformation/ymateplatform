/**
 * <p>文件名:	PluginContext.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Plugin</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.plugin;

/**
 * <p>
 * PluginContext
 * </p>
 * <p>
 * 插件环境上下文对象；
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
 *          <td>2011-10-18上午10:39:03</td>
 *          </tr>
 *          </table>
 */
public class PluginContext {

	private PluginMeta __pluginMeta;

	/**
	 * 构造器
	 * @param pluginMeta
	 */
	public PluginContext(PluginMeta pluginMeta) {
		this.__pluginMeta = pluginMeta;
	}

	/**
	 * @return 插件相关信息描述类
	 */
	public PluginMeta getPluginMeta() {
		return this.__pluginMeta;
	}

}
