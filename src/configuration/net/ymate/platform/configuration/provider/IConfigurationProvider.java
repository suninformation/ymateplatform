/**
 * <p>文件名:	IConfigurationProvider.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Configuration</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.configuration.provider;

import java.util.List;
import java.util.Map;

import net.ymate.platform.configuration.ConfigurationLoadException;

/**
 * <p>
 * IConfigurationProvider
 * </p>
 * <p>
 * 配置提供者接口，通过配置提供者来获取配置;
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
 *          <td>2011-8-27上午12:23:13</td>
 *          </tr>
 *          </table>
 */
public interface IConfigurationProvider {

	/**
	 * 根据文件名绝对路径加载配置
	 *
	 * @param cfgFileName
	 * @throws ConfigurationLoadException
	 */
	public void load(String cfgFileName) throws ConfigurationLoadException;

	/**
	 * 使用现有文件名重新加载
	 *
	 * @throws ConfigurationLoadException
	 */
	public void reload() throws ConfigurationLoadException;

	/**
	 * 获得当前配置提供者装载参数
	 *
	 * @return
	 */
	public String getCfgFileName();

	/**
	 * 使用 "|" 分割的分级键值获得对应的文字值
	 *
	 * @param key
	 * @return
	 */
	public String getString(String key);
	
	public String getString(String key, String defaultValue);

	/**
	 * 使用 "|" 分割的分级键值获得对应的文字值列表，其中匹配以key开头的键串
	 *
	 * @param key
	 * @return
	 */
	public List<String> getList(String key);

	/**
	 * 获取键值映射，其中键为去除keyHead后的部分，值为对应设置项的包装
	 *
	 * @param keyHead
	 *            允许使用"|"进行分割级别的键头标识
	 * @return
	 */
	public Map<String, String> getMap(String keyHead);

	/**
	 * 获取键值数组值，数组用  ',' 分隔
	 * 
	 * @param key
	 * @return
	 */
	public String[] getArray(String key);
	
	public String[] getArray(String key, boolean zeroSize);

	/**
	 * 使用 "|" 分割的分级键值获得对应的数字值
	 *
	 * @param key
	 * @return
	 */
	public int getInt(String key);
	
	public int getInt(String key, int defaultValue);

	/**
	 * 使用 "|" 分割的分级键值获得对应的布尔值
	 *
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key);
	
	public boolean getBoolean(String key, boolean defaultValue);

	/**
	 * 使用 "|" 分割的分级键值获取长整数
	 *
	 * @param key
	 * @return
	 */
	public long getLong(String key);
	
	public long getLong(String key, long defaultValue);

	/**
	 * 使用 "|" 分割的分级键值获取浮点数
	 *
	 * @param key
	 * @return
	 */
	public float getFloat(String key);
	
	public float getFloat(String key, float defaultValue);

	/**
	 * 使用 "|" 分割的分级键值获取双精度浮点数
	 *
	 * @param key
	 * @return
	 */
	public double getDouble(String key);
	
	public double getDouble(String key, double defaultValue);

	/**
	 * 获得配置对象内部加载的配置项映射
	 *
	 * @return
	 */
	public Map<String, String> getCfgsMap();

	/**
	 * 是否包括某个配置项，满足 "|" 的级别分割，不获取数据，只是判断这个配置项是否存在
	 * 
	 * @param key
	 * @return 如果存在配置项那么返回true，否则返回false
	 */
	public boolean contains(String key);

}
