/**
 * <p>文件名:	AbstractAccessor.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


/**
 * <p>
 * AbstractAccessor
 * </p>
 * <p>
 * 访问器接口抽象实现类；
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
 *          <td>2011-9-2下午03:17:32</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractAccessor implements IAccessor {

	private String __sqlStr;

	private IAccessorCfgEvent __eventObj;

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessor#getAccessorCfgEvent()
	 */
	public IAccessorCfgEvent getAccessorCfgEvent() {
		return __eventObj;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessor#getPreparedStatement(java.sql.Connection)
	 */
	public PreparedStatement getPreparedStatement(Connection conn) throws SQLException {
		PreparedStatement _returnValue = null;
		if (this.getAccessorCfgEvent() != null) {
			_returnValue = this.getAccessorCfgEvent().getPreparedStatement(conn, this.getSqlStr());
		}
		if (_returnValue == null) {
			_returnValue = conn.prepareStatement(this.getSqlStr());
		}
		//
		processSqlParams(_returnValue);
		//
		return _returnValue;
	}

	/**
	 * 处理 SQL 参数，由具体实现类完成
	 * 
	 * @param statement
	 * @throws SQLException
	 */
	protected abstract void processSqlParams(PreparedStatement statement) throws SQLException;

	/**
	 * 设置 SQL 参数
	 * 
	 * @param statement
	 * @param params
	 * @exception SQLException
	 */
	protected void doSetSqlParams(PreparedStatement statement, List<SqlParameter> params) throws SQLException{
		for (int i = 0; i < params.size(); i++) {
			SqlParameter _param = params.get(i);
			if (_param.getValue() == null) {
				statement.setNull(i + 1, _param.getType());
			} else {
				statement.setObject(i + 1, _param.getValue());
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessor#getCallableStatement(java.sql.Connection)
	 */
	public CallableStatement getCallableStatement(Connection conn) throws SQLException {
		CallableStatement _stm = null;
		if (__eventObj != null) {
			_stm = __eventObj.getCallableStatement(conn, this.__sqlStr);
		}
		return _stm == null ? conn.prepareCall(this.__sqlStr) : _stm;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.base.IAccessor#getStatement(java.sql.Connection)
	 */
	public Statement getStatement(Connection conn) throws SQLException {
		Statement _stm = null;
		if (__eventObj != null) {
			_stm = __eventObj.getStatement(conn);
		}
		return _stm == null ? conn.createStatement() : _stm;
	}

	protected void setAccessorCfgEvent(IAccessorCfgEvent eventObj) {
		this.__eventObj = eventObj;
	}

	protected String getSqlStr() {
		return __sqlStr;
	}

	protected void setSqlStr(String str) {
		__sqlStr = str;
	}

}
