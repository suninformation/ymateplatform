/**
 * <p>文件名:	IDataSourceAdapter.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc;

import java.sql.Connection;

import net.ymate.platform.persistence.jdbc.base.dialect.IDialect;
import net.ymate.platform.persistence.jdbc.support.DataSourceCfgMeta;

/**
 * <p>
 * IDataSourceAdapter
 * </p>
 * <p>
 * 数据源适配器接口；
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
 *          <td>2012-12-29上午12:28:41</td>
 *          </tr>
 *          </table>
 */
public interface IDataSourceAdapter {

	/**
	 * 数据源适配器初始化
	 * 
	 * @param cfgMeta 数据源配置参数
	 */
	public void initialize(DataSourceCfgMeta cfgMeta);

	/**
	 * @return 获取数据库连接
	 */
	public Connection getConnection() throws ConnectionException;

	/**
	 * @return 获取数据库方言
	 */
	public IDialect getDialect();

	/**
	 * 销毁数据源适配器
	 */
	public void destroy();

}
