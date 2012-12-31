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
import net.ymate.platform.persistence.jdbc.ConnectionException;
import net.ymate.platform.persistence.jdbc.IDataSourceAdapter;
import net.ymate.platform.persistence.jdbc.base.dialect.IDialect;
import net.ymate.platform.persistence.jdbc.base.dialect.impl.MySqlDialect;
import net.ymate.platform.persistence.jdbc.base.dialect.impl.OracleDialect;
import net.ymate.platform.persistence.jdbc.base.dialect.impl.SQLServer2005Dialect;

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
public class DefaultDataSourceAdapter implements IDataSourceAdapter {

	protected DataSourceCfgMeta cfgMeta;

	protected IDialect dialect;

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IDataSourceAdapter#initialize(net.ymate.platform.persistence.jdbc.support.DataSourceCfgMeta)
	 */
	public void initialize(DataSourceCfgMeta cfgMeta) {
		this.cfgMeta = cfgMeta;
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
			Connection conn;
	        if (cfgMeta.getName() != null) {
				conn = DriverManager.getConnection(cfgMeta.getConnectionUrl(), cfgMeta.getUserName(), cfgMeta.getPassword());
	        } else {
	            conn = DriverManager.getConnection(cfgMeta.getConnectionUrl());
	        }
	        return conn;
		} catch (Exception e) {
			throw new ConnectionException(RuntimeUtils.unwrapThrow(e));
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IDataSourceAdapter#getDialect()
	 */
	public IDialect getDialect() {
		if (dialect == null) {
			try {
				String _prodName = this.getConnection().getMetaData().getDatabaseProductName().toLowerCase();
				if (_prodName.contains("oracle")) {
					dialect = new OracleDialect();
				} else if (_prodName.contains("mysql")) {
					dialect = new MySqlDialect();
				} else if (_prodName.contains("sql server")) {
					dialect = new SQLServer2005Dialect();
				}
			} catch (Exception e) {
				throw new Error(RuntimeUtils.unwrapThrow(e));
			}
		}
		return dialect;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IDataSourceAdapter#destroy()
	 */
	public void destroy() {
	}

}
