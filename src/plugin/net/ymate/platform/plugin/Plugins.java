/**
 * <p>文件名:	Plugins.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.plugin;

import net.ymate.platform.plugin.impl.DefaultPluginConfig;




/**
 * <p>
 * Plugins
 * </p>
 * <p>
 * 插件管理器类；
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
 *          <td>2012-11-30下午6:28:20</td>
 *          </tr>
 *          </table>
 */
public class Plugins {

	/**
	 * 使用默认配置创建插件工厂
	 * 
	 * @return
	 * @throws PluginException
	 */
	public static IPluginFactory createPluginFactory() throws PluginException {
		return createPluginFactory(new DefaultPluginConfig());
	}

	/**
	 * 创建插件工厂
	 * 
	 * @param config 插件初始化配置
	 * @return
	 * @throws PluginException
	 */
	public static IPluginFactory createPluginFactory(IPluginConfig config) throws PluginException {
		if (config == null) {
			throw new PluginException("插件管理器初始化失败，参数配置对象为NULL");
		}
		IPluginFactory _factory = config.getPluginFactoryClassImpl();
		_factory.initialize(config);
		// 将所有设置为自动运行的插件启动起来...
		for (PluginMeta _meta : _factory.getPluginMetas()) {
			if (_meta.isAutomatic()) {
				_factory.getPlugin(_meta.getId());
			}
		}
		return _factory;
	}

}
