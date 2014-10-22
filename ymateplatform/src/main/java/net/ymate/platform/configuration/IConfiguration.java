/*
 * Copyright 2007-2107 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.platform.configuration;

import java.util.List;
import java.util.Map;

import net.ymate.platform.configuration.provider.IConfigurationProvider;


/**
 * <p>
 * IConfiguration
 * </p>
 * <p>
 * 配置接口定义类；
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
 *          <td>2011-8-27上午12:20:51</td>
 *          </tr>
 *          </table>
 */
public interface IConfiguration {

	/**
	 * 配置项键值间隔符号
	 */
	public static String CFG_KEY_SEPERATE = "|";

	/**
	 * 默认配置分类名称
	 */
	public static String DEFAULT_CFG_CATEGORY_NAME = "general";

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
	 * 初始化配置项，一般用来从provider中获取配置项，是实现自定义的配置项进入内存的位置
	 *
	 * @return 是否初始化加载成功
	 */
	public boolean isInited();

	/**
	 * 初始化配置项，一般用来从provider中获取配置项，是实现自定义的配置项进入内存的位置
	 *
	 *@param provider 配置提供者
	 */
	public void initialize(IConfigurationProvider provider);

	/**
	 * 获得配置标签名，一般一个类别的配置对象都会有个固定的扩展名形式，但是不推荐每个配置对象都使用特殊的标记名，
	 * 所以尽量在一个abstract的cfg类中使用final固定此返回值
	 *
	 * @return 返回值加在XXXX.xml中间，形成XXXX.YYY.xml形式，其中需要返回.YYY
	 */
	public String getCfgTagName();

	/**
	 * 是否包括某个配置项，满足 "|" 的级别分割，不获取数据，只是判断这个配置项是否存在
	 * 
	 * @param key
	 * @return 如果存在配置项那么返回true，否则返回false
	 */
	public boolean contains(String key);

    /**
     * @return 获取分类的名称集合
     */
    public List<String> getCategoryNames();

}
