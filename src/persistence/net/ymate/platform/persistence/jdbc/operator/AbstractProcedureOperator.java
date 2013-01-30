/**
 * <p>文件名:	AbstractProcedureOperator.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.operator;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.ymate.platform.persistence.jdbc.base.SqlParameter;
import net.ymate.platform.persistence.jdbc.base.impl.GenericAccessor;
import net.ymate.platform.persistence.jdbc.operator.impl.ArrayResultSetHandler;

/**
 * <p>
 * AbstractProcedureOperator
 * </p>
 * <p>
 * 数据库存储过程操作者抽象实现类；实现方式上支持多结果集的返回方式，但由于存储过程的多结果集的实现方式有所不同(如：采用输出参数方式或getMoreResults()方式等)，所以在编写子类时请根据实际情况注意结果集相关部份的处理方法；
 * </p>
 * <p>
 * 其实最好的方法就是能够统一一下存储过程的使用规范；
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
 *          <td>2011-9-26下午03:21:53</td>
 *          </tr>
 *          </table>
 * @deprecated 尚未完成
 */
public class AbstractProcedureOperator extends AbstractOperator implements IProcedureOperator {

	/**
	 * 存储过程OUT参数集合
	 */
	private List<SqlParameter> __outParameters = new ArrayList<SqlParameter>();

	/**
	 * 保存OUT参数的结果值
	 */
	private List<Object> __resultOutParameter = new ArrayList<Object>();

	/**
	 * 存放结果集合，同时支持多结果集
	 */
	private List<List<Object[]>> __resultSet = new LinkedList<List<Object[]>>();

	private int __returnFlag; // 存储过程输出参数，成功与否的返回标记

	private String __returnMsg; // 存储过程输出参数，存储过程返回信息

	private boolean __isMultipleResultSet; // 是否标记为多结果集返回方式

	/**
	 * 构造器
	 */
	public AbstractProcedureOperator() {
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#__execute()
	 */
	protected int __execute() throws OperatorException, SQLException {
		CallableStatement _call = null;
		ResultSet _rs = null;
		try {
			// 构造call语句
			_call = new GenericAccessor(__getCallString(), null, getAccessorCfgEvent()).getCallableStatement(this.getConnection().getConnection());
			// 处理存储输入参数
			__processInputParameter(_call);
			// 登记输出参数
			__registerOutParameters(_call, this.getParameters().size());
			// 执行（不考虑第一个返回内容是更新计数还是结果集）
			_call.execute();
			// 处理必须的返回值参数
			int _position = this.getParameters().size() + this.getOutParameters().size();
			this.__returnFlag = _call.getInt(_position - 2);
			this.__returnMsg = _call.getString(_position - 1);
			// 处理自定义的OUT参数值
			if (!this.getOutParameters().isEmpty()) {
				for (int i = 0; i < this.getParameters().size() - 2; i++) {
					this.__resultOutParameter.add(_call.getObject(i + 1));
				}
			}
			//
			if (this.__returnFlag >= PROC_SUCCESS_FLAG) {
				if (this.__returnFlag == PROC_HAVE_RESULT) {
					this.__processResultSet(_call, _rs);
				}
			}
			return -1;
		} finally {
			_call = null;
			_rs = null;
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#__parametersToString()
	 */
	protected String __parametersToString() {
		return this.getParameters().toString();
	}

	/**
	 * @return 构建存储过程Call语句(根据不同的数据库，可由子类重新实现)
	 */
	protected String __getCallString() {
		StringBuilder _callStr = new StringBuilder();
		_callStr.append("{call ").append(this.getSql()).append("(");
		for (int i = 0; i < this.getParameters().size(); i++) {
			if (i > 0) {
				_callStr.append(",");
			}
			_callStr.append("?");
		}
		if (_callStr.toString().indexOf('?') >= 0) {
			_callStr.append(",?,?)}");
		} else {
			_callStr.append("?,?)}");
		}
		return _callStr.toString();
	}

	/**
	 * 注册存储过程输出的参数(根据不同的数据库，可由子类重新实现)
	 * 
	 * @param call
	 * @param position 参数的位置(一般从最后一个输入参数后开始)
	 * @throws SQLException
	 */
	protected void __registerOutParameters(CallableStatement call, int position) throws SQLException {
		List<SqlParameter> _outParameters = this.getOutParameters();
		int i = 0;
		for (i = 0; i < _outParameters.size(); i++) {
			SqlParameter _param = _outParameters.get(i);
			call.registerOutParameter(i + 1, _param.getType());
		}
		// 注册必须的参数
		call.registerOutParameter(i + 1, java.sql.Types.INTEGER);
		call.registerOutParameter(i + 2, java.sql.Types.VARCHAR);
	}

	/**
	 * 处理数据结果集(根据不同的数据库，可由子类重新实现)
	 * 
	 * @param call
	 * @param resultSet
	 * @throws OperatorException
	 * @throws SQLException
	 */
	protected void __processResultSet(CallableStatement call, ResultSet resultSet) throws OperatorException, SQLException {
        int _updateCount;
        do {
            _updateCount = call.getUpdateCount();
            if (_updateCount != -1) { // 说明当前行是一个更新计数
                call.getMoreResults();
                // 已经是更新计数了,处理完成后应该移动到下一行
                // 不再判断是否是ResultSet
                continue;
            }
            resultSet = call.getResultSet();
            if (resultSet != null) {
                // 处理ResultSet
                // 为当前结果集对象创建新的处理器
                IResultSetHandler<Object[]> _handlerImp = new ArrayResultSetHandler();
                // 循环处理当前结果集中记录
                while (resultSet.next()) {
                    _handlerImp.handle(resultSet, -1);
                }
                // 若结果集中存在数据，则添加到最终结果集合中
                if (_handlerImp.getRowCount() > 0) {
                    this.addResultSet(_handlerImp.getResultDataSet());
                }
                resultSet.close();
                call.getMoreResults();
                // 结果集处理完成后应该移动到下一行
            }
        } while (!(_updateCount == -1 && resultSet == null));
        // 判断并设置当前是否为多结果集属性值
        if (this.getResultSetAll().size() > 1) {
        	this.__isMultipleResultSet = true;
        }
	}

	/**
	 * 处理存储过程的输入参数
	 * 
	 * @param call
	 * @throws SQLException
	 */
	protected void __processInputParameter(CallableStatement call) throws SQLException {
		List<SqlParameter> _parameters = this.getParameters();
		for (int i = 0; i < _parameters.size(); i++) {
			SqlParameter _param = _parameters.get(i);
			call.setObject(i + 1, _param.getValue(), _param.getType());
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IProcedureOperator#getResultOutParameters()
	 */
	public List<Object> getResultOutParameters() {
		return this.__resultOutParameter;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IProcedureOperator#getResultOutParameter(int)
	 */
	public Object getResultOutParameter(int index) {
		return this.__resultOutParameter.get(index);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IProcedureOperator#getOutParameters()
	 */
	public List<SqlParameter> getOutParameters() {
		return this.__outParameters;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IProcedureOperator#addOutParameter(net.ymate.platform.persistence.jdbc.base.SqlParameter)
	 */
	public void addOutParameter(SqlParameter sqlParameter) {
		if (sqlParameter != null) {
			this.__outParameters.add(sqlParameter);
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IProcedureOperator#getResultSetAll()
	 */
	public List<List<Object[]>> getResultSetAll() {
		return this.__resultSet;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IProcedureOperator#getResultSet(int)
	 */
	public List<Object[]> getResultSet(int index) {
		return this.__resultSet.get(index);
	}

	/**
	 * 添加结果集合对象
	 * 
	 * @param resultSet
	 */
	protected void addResultSet(List<Object[]> resultSet) {
		this.__resultSet.add(resultSet);
	}

	/**
	 * 移除指定的结果集合对象
	 * 
	 * @param index
	 */
	protected void removeResultSet(int index) {
		this.__resultSet.remove(index);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IProcedureOperator#getReturnFlag()
	 */
	public int getReturnFlag() {
		return __returnFlag;
	}

	/**
	 * 设置执行结果返回标记
	 * 
	 * @param flag
	 */
	protected void setReturnFlag(int flag) {
		__returnFlag = flag;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IProcedureOperator#getReturnMsg()
	 */
	public String getReturnMsg() {
		return __returnMsg;
	}

	/**
	 * 设置执行结果返回消息
	 *
	 * @param msg
	 */
	protected void setReturnMsg(String msg) {
		__returnMsg = msg;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IProcedureOperator#isMultipleResultSet()
	 */
	public boolean isMultipleResultSet() {
		return __isMultipleResultSet;
	}

	/**
	 * 设置是否为多返回结果集方式
	 * 
	 * @param multipleResultSet
	 */
	protected void setMultipleResultSet(boolean multipleResultSet) {
		__isMultipleResultSet = multipleResultSet;
	}

}
