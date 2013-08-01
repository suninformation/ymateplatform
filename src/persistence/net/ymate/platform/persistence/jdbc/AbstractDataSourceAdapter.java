/**
 * <p>文件名:	AbstractDataSourceAdapter.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc;

import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.persistence.jdbc.base.dialect.IDialect;
import net.ymate.platform.persistence.jdbc.support.DataSourceCfgMeta;

/**
 * <p>
 * AbstractDataSourceAdapter
 * </p>
 * <p>
 * 数据源适配器接口抽象实现类；
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
 *          <td>2013年8月1日下午8:30:34</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractDataSourceAdapter implements IDataSourceAdapter {

	protected DataSourceCfgMeta cfgMeta;

	protected IDialect dialect;

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IDataSourceAdapter#initialize(net.ymate.platform.persistence.jdbc.support.DataSourceCfgMeta)
	 */
	public void initialize(DataSourceCfgMeta cfgMeta) {
		this.cfgMeta = cfgMeta;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.IDataSourceAdapter#getDialect()
	 */
	public IDialect getDialect() {
		if (dialect == null) {
			try {
				String _prodName = this.getConnection().getMetaData().getDatabaseProductName();
				dialect = JDBC.getDialectClass(_prodName).newInstance();
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
		this.cfgMeta = null;
		this.dialect = null;
	}

}
