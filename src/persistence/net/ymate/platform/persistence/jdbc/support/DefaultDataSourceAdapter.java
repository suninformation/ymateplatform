/**
 * <p>文件名:	DefaultDataSourceAdapter.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.support;

import java.sql.Connection;
import java.sql.DriverManager;

import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.persistence.jdbc.AbstractDataSourceAdapter;
import net.ymate.platform.persistence.jdbc.ConnectionException;

/**
 * <p>
 * DefaultDataSourceAdapter
 * </p>
 * <p>
 * 数据源适配器接口默认实现类；
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
 *          <td>2012-12-29下午4:12:05</td>
 *          </tr>
 *          </table>
 */
public class DefaultDataSourceAdapter extends AbstractDataSourceAdapter {

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.AbstractDataSourceAdapter#initialize(net.ymate.platform.persistence.jdbc.support.DataSourceCfgMeta)
	 */
	public void initialize(DataSourceCfgMeta cfgMeta) {
		super.initialize(cfgMeta);
		try {
			Class.forName(cfgMeta.getDriverClass());
		} catch (ClassNotFoundException e) {
			throw new Error(RuntimeUtils.unwrapThrow(e));
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IDataSourceAdapter#getConnection()
	 */
	public Connection getConnection() throws ConnectionException {
		try {
	        return DriverManager.getConnection(cfgMeta.getConnectionUrl(), cfgMeta.getUserName(), cfgMeta.getPassword());
		} catch (Exception e) {
			throw new ConnectionException(RuntimeUtils.unwrapThrow(e));
		}
	}

}
