/**
 * <p>文件名:	AbstractOperator.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.operator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import net.ymate.platform.commons.logger.Logs;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.base.AccessorEventContext;
import net.ymate.platform.persistence.jdbc.base.IAccessor;
import net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent;
import net.ymate.platform.persistence.jdbc.base.SqlParameter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;

/**
 * <p>
 * AbstractOperator
 * </p>
 * <p>
 * 数据库操作器接口抽象实现类，所有操作相关类继承于此并需要实现具体的操作过程执行方法；
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
 *          <td>2011-9-22下午10:19:53</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractOperator implements IOperator {

	/**
	 * 预执行的SQL语句串
	 */
	private String __sql;

	/**
	 * 当前使用的数据库连接对象
	 */
	private IConnectionHolder __currentConnection;

	/**
	 * 访问器配置事件处理接口
	 */
	private IAccessorCfgEvent __config;

	/**
	 * SQL参数集合
	 */
	private List<SqlParameter> __parameters = new ArrayList<SqlParameter>();

	/**
	 * 本次操作所消耗的时间（单位：毫秒值）
	 */
	private long __expenseTime = 0;

	/**
	 * 是否已执行过
	 */
	private boolean __isExecuted;

	/**
	 * 是否显示 SQL 语句
	 */
	private boolean __isShowSql;

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#addParameter(net.ymate.platform.persistence.jdbc.base.SqlParameter)
	 */
	public void addParameter(SqlParameter parameter) {
		if (parameter != null) {
			this.__parameters.add(parameter);
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#addParameter(java.lang.Object)
	 */
	public void addParameter(Object parameter) {
		if (parameter == null) {
			this.__parameters.add(new SqlParameter(Types.VARCHAR, null));
		} else {
			this.__parameters.add(new SqlParameter(parameter));
		}
	}

	/**
	 * 具体的操作过程执行方法（需子类实现）
	 * 
	 * @return 返回此次操作返回结果集记录行数，默认为-1
	 * @throws OperatorException
	 * @throws SQLException
	 */
	protected abstract int __execute() throws OperatorException, SQLException;

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#execute()
	 */
	public void execute() throws OperatorException {
		if (!this.__isExecuted) {
			if (StringUtils.isBlank(this.__sql)) {
				throw new OperatorException("尚未提供可执行的SQL语句");
			} else {
				try {
					StopWatch _time = new StopWatch();
					_time.start();
					int _recordSize = this.__execute();
					_time.stop();
					this.setExpenseTime(_time.getTime());
					if (JDBC.isShowSQL) {
						Logs.debug("执行SQL语句: " + this.getSql() + "，参数：" + this.getParameters() + (_recordSize >= 0 ? "，影响/记录数：" + _recordSize : "") + "， 耗时: " + this.getExpenseTime() + "ms");
					}
				} catch (SQLException e) {
					throw new OperatorException("操作执行时异常, [" + this.getSql() + ", " + this.getParameters() + "]", RuntimeUtils.unwrapThrow(e));
				} finally {
					this.__isExecuted = true;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#execute(net.ymate.platform.persistence.jdbc.connection.ConnectionHolder)
	 */
	public void execute(IConnectionHolder conn) throws OperatorException {
		this.setConnection(conn);
		this.execute();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#getConnection()
	 */
	public IConnectionHolder getConnection() {
		return this.__currentConnection;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#setConnection(net.ymate.platform.persistence.jdbc.connection.ConnectionHolder)
	 */
	public void setConnection(IConnectionHolder conn) {
		this.__currentConnection = conn;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#getParameters()
	 */
	public List<SqlParameter> getParameters() {
		return this.__parameters;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#getSql()
	 */
	public String getSql() {
		return this.__sql;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#setSql(java.lang.String)
	 */
	public void setSql(String sql) {
		this.__sql = sql;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#isConnectionAvailable()
	 */
	public boolean isConnectionAvailable() {
		if (this.__currentConnection != null) {
			boolean _able = true;
			try {
				if (this.__currentConnection.getConnection().isClosed()) {
					_able = false;
				}
			} catch (SQLException e) {
				_able = false;
			}
			return _able;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#isExecuted()
	 */
	public boolean isExecuted() {
		return this.__isExecuted;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#setShowSql(boolean)
	 */
	public void setShowSql(boolean showSql) {
		this.__isShowSql = showSql;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#isShowSql()
	 */
	public boolean isShowSql() {
		return this.__isShowSql;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#getAccessorCfgEvent()
	 */
	public IAccessorCfgEvent getAccessorCfgEvent() {
		return this.__config;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#setAccessorCfgEvent(net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent)
	 */
	public void setAccessorCfgEvent(IAccessorCfgEvent config) {
		this.__config = config;
	}

	/**
	 * 设置本次操作所消耗的时间（单位：毫秒值）
	 * 
	 * @param time
	 */
	protected void setExpenseTime(long time) {
		this.__expenseTime = time;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IOperator#getExpenseTime()
	 */
	public long getExpenseTime() {
		return this.__expenseTime;
	}

	//=================================
	// DB Operator Base Code
	//=================================

	/**
	 * 执行查询操作
	 *  
	 * @param accessor 数据库访问器对象
	 * @param conn 数据库连接对象
	 * @param handler 结果集处理接口实现
	 * @param maxRow
	 * @throws OperatorException
	 * @throws SQLException
	 */
	protected void doQuery(IAccessor accessor, IConnectionHolder conn, IResultSetHandler<?> handler, int maxRow) throws OperatorException, SQLException {
		PreparedStatement _statement = null;
		ResultSet _rs = null;
		AccessorEventContext _context = null;
		try {
			_statement = accessor.getPreparedStatement(conn.getConnection());
			if (accessor.getAccessorCfgEvent() != null) {
				_context = new AccessorEventContext(_statement, false, false);
				accessor.getAccessorCfgEvent().beforeStatementExecution(_context);
			}
			_rs = _statement.executeQuery();
			handler.handle(_rs, maxRow);
			if (accessor.getAccessorCfgEvent() != null && _context != null) {
				accessor.getAccessorCfgEvent().afterStatementExecution(_context);
			}
		} finally {
			if (_rs != null) {
				_rs.close();
				_rs = null;
			}
			if (_statement != null) {
				_statement.close();
				_statement = null;
			}
			_context = null;
		}
	}

	/**
	 * 执行更新操作
	 * 
	 * @param accessor 数据库访问器对象
	 * @param conn 数据库连接对象
	 * @return 返回受影响的记录行数
	 * @throws SQLException
	 */
	protected int doUpdate(IAccessor accessor, IConnectionHolder conn) throws SQLException {
		PreparedStatement _statement = null;
		AccessorEventContext _context = null;
		try {
			_statement = accessor.getPreparedStatement(conn.getConnection());
			if (accessor.getAccessorCfgEvent() != null) {
				_context = new AccessorEventContext(_statement, false, false);
				accessor.getAccessorCfgEvent().beforeStatementExecution(_context);
			}
			int _returnValue = _statement.executeUpdate();
			if (accessor.getAccessorCfgEvent() != null && _context != null) {
				accessor.getAccessorCfgEvent().afterStatementExecution(_context);
			}
			return _returnValue;
		} finally {
			if (_statement != null) {
				_statement.close();
				_statement = null;
			}
			_context = null;
		}
	}

	/**
	 * 执行批更新操作
	 * 
	 * @param accessor 数据库访问器对象
	 * @param conn 数据库连接对象
	 * @return 分别返回受影响的记录行数
	 * @throws SQLException
	 */
	protected int[] doBatchUpdate(IAccessor accessor, IConnectionHolder conn) throws SQLException {
		PreparedStatement _statement = null;
		AccessorEventContext _context = null;
		try {
			_statement = accessor.getPreparedStatement(conn.getConnection());
			if (accessor.getAccessorCfgEvent() != null) {
				_context = new AccessorEventContext(_statement, false, false);
				accessor.getAccessorCfgEvent().beforeStatementExecution(_context);
			}
			int _returnValue[] = _statement.executeBatch();
			if (accessor.getAccessorCfgEvent() != null && _context != null) {
				accessor.getAccessorCfgEvent().afterStatementExecution(_context);
			}
			_statement.clearBatch();
			return _returnValue;
		} finally {
			if (_statement != null) {
				_statement.close();
				_statement = null;
			}
			_context = null;
		}
	}

}
