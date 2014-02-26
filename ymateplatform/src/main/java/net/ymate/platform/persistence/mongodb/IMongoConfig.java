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
package net.ymate.platform.persistence.mongodb;

import java.util.Set;

import net.ymate.platform.persistence.support.DataSourceCfgMeta;

/**
 * <p>
 * IMongoConfig
 * </p>
 * <p>
 * MongoDB持久化框架初始化配置接口；
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
 *          <td>2014年2月5日下午5:12:13</td>
 *          </tr>
 *          </table>
 */
public interface IMongoConfig {

	/**
	 * @return 是否显示执行的语句日志
	 */
	public boolean isShowLog();

	/**
	 * @return 集合前缀（主要适应多应用共用同一集合，采用前缀区分），如："ym_"，默认为""
	 */
	public String getCollectionPrefix();

	/**
	 * @return 返回默认数据源名称
	 */
	public String getDefaultDataSourceName();

	/**
	 * @return 返回自动扫描的存储器包名称集合
	 */
	public String[] getRepositoryPackages();

	/**
	 * @return 返回数据源配置对象集合
	 */
	public Set<DataSourceCfgMeta> getDataSourceCfgMetas();

}
