/**
 * <p>文件名:	DefaultPluginConfig.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.plugin.impl;

import net.ymate.platform.plugin.IPluginConfig;
import net.ymate.platform.plugin.IPluginExtraParser;
import net.ymate.platform.plugin.IPluginFactory;
import net.ymate.platform.plugin.IPluginParser;

/**
 * <p>
 * DefaultPluginConfig
 * </p>
 * <p>
 * 默认插件初始化配置接口实现类；
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
 *          <td>2012-12-2下午4:33:37</td>
 *          </tr>
 *          </table>
 */
public final class DefaultPluginConfig implements IPluginConfig {

	private IPluginFactory __pluginFactoryImpl;

	private IPluginParser __pluginParserImpl;

	private IPluginExtraParser __extraParserImpl;

	private String __pluginHome;

	/**
	 * 构造器
	 */
	public DefaultPluginConfig() {
		this(new DefaultPluginFactory(), new DefaultPluginParser(), null, null);
	}

	/**
	 * 构造器
	 * @param extraParser
	 * @param pluginHome
	 */
	public DefaultPluginConfig(IPluginExtraParser extraParser, String pluginHome) {
		this(new DefaultPluginFactory(), new DefaultPluginParser(), extraParser, pluginHome);
	}

	/**
	 * 构造器
	 * @param pluginHome
	 */
	public DefaultPluginConfig(String pluginHome) {
		this(null, pluginHome);
	}

	/**
	 * 构造器
	 * @param factory
	 * @param parser
	 * @param extraParser
	 * @param pluginHome
	 */
	public DefaultPluginConfig(IPluginFactory factory, IPluginParser parser, IPluginExtraParser extraParser, String pluginHome) {
		__pluginFactoryImpl = factory;
		__pluginParserImpl = parser;
		__extraParserImpl = extraParser;
		__pluginHome = pluginHome;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginConfig#getPluginFactoryClassImpl()
	 */
	public IPluginFactory getPluginFactoryClassImpl() {
		return __pluginFactoryImpl;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginConfig#getPluginParserClassImpl()
	 */
	public IPluginParser getPluginParserClassImpl() {
		return __pluginParserImpl;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginConfig#getPluginExtraParserClassImpl()
	 */
	public IPluginExtraParser getPluginExtraParserClassImpl() {
		return __extraParserImpl;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.plugin.IPluginConfig#getPluginHomePath()
	 */
	public String getPluginHomePath() {
		return __pluginHome;
	}

}
