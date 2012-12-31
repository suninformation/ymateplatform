/**
 * <p>文件名:	GenericAccessor.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.base.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.ymate.platform.persistence.jdbc.base.AbstractAccessor;
import net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent;
import net.ymate.platform.persistence.jdbc.base.SqlParameter;

/**
 * <p>
 * GenericAccessor
 * </p>
 * <p>
 * 普通数据库访问器实现类；
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
 *          <td>2011-8-31下午03:21:03</td>
 *          </tr>
 *          </table>
 */
public class GenericAccessor extends AbstractAccessor {

	private List<SqlParameter> __parameters = new ArrayList<SqlParameter>();

	/**
	 * 构造器
	 * 
	 * @param sql
	 */
	public GenericAccessor(String sql) {
		this.setSqlStr(sql);
	}

	/**
	 * 构造器
	 * 
	 * @param sql
	 * @param params
	 */
	public GenericAccessor(String sql, List<SqlParameter> params) {
		this(sql, params, null);
	}

	/**
	 * 构造器
	 * 
	 * @param sql
	 * @param params
	 * @param eventObj
	 */
	public GenericAccessor(String sql, List<SqlParameter> params, IAccessorCfgEvent eventObj) {
		this.setSqlStr(sql);
		this.setAccessorCfgEvent(eventObj);
		if (params != null && !params.isEmpty()) {
			__parameters.addAll(params);
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.AbstractAccessor#processSqlParams(java.sql.PreparedStatement)
	 */
	protected void processSqlParams(PreparedStatement statement) throws SQLException {
		if (!this.__parameters.isEmpty()) {
			this.doSetSqlParams(statement, this.__parameters);
		}
	}

}
