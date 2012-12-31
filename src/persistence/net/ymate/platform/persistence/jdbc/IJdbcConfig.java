/**
 * <p>文件名:	IJdbcConfig.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc;

import java.util.Set;

import net.ymate.platform.persistence.jdbc.support.DataSourceCfgMeta;

/**
 * <p>
 * IJdbcConfig
 * </p>
 * <p>
 * JDBC持久化框架初始化配置接口；
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
 *          <td>2012-12-29下午2:15:21</td>
 *          </tr>
 *          </table>
 */
public interface IJdbcConfig {

	/**
	 * @return 是否显示执行的SQL语句
	 */
	public boolean isShowSql();

	/**
	 * @return 数据库表前缀（主要适应多应用共用同一数据库，采用前缀区分），如："ym_"，默认为""
	 */
	public String getTablePrefix();

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
