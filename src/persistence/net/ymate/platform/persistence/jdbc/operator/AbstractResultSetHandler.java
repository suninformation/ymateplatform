/**
 * <p>文件名:	AbstractResultSetHandler.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.operator;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/**
 * <p>
 * AbstractResultSetHandler
 * </p>
 * <p>
 * 结果集数据处理接口抽象实现类；
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
 *          <td>2011-9-22下午04:14:15</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractResultSetHandler<T> implements IResultSetHandler<T> {

	private int __rowCount;

	private int __columnCount;

	private int[] __columnTypes;

	private String[] __columnNames;

	private List<T> __resultDataSet;

	private boolean __isProcessed = false;

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IResultSetHandler#getColumnCount()
	 */
	public int getColumnCount() {
		return this.__columnCount;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IResultSetHandler#getColumnNames()
	 */
	public String[] getColumnNames() {
		if (this.__isProcessed) {
			return this.__columnNames;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IResultSetHandler#getColumnTypes()
	 */
	public int[] getColumnTypes() {
		if (this.__isProcessed) {
			return this.__columnTypes;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IResultSetHandler#getResultDataSet()
	 */
	public List<T> getResultDataSet() {
		return this.__resultDataSet;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IResultSetHandler#getRowCount()
	 */
	public int getRowCount() {
		return this.__rowCount;
	}

	/**
	 * 记录行数据处理方法，由具体实现类确定处理过程
	 * 
	 * @param rs 数据结果集对象，切勿对其进行游标移动等操作，仅约定用于提取当前行字段数据
	 * @param result
	 * @throws OperatorException
	 * @throws SQLException
	 */
	public abstract void processRowData(ResultSet rs, List<T> result) throws OperatorException, SQLException;

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IResultSetHandler#handle(java.sql.ResultSet, int)
	 */
	public void handle(ResultSet rs, int maxRow) throws OperatorException, SQLException {
		this.__isProcessed = true;
		List<T> _result = new ArrayList<T>();
		if (this.__rowCount == 0) {
			ResultSetMetaData _rsMeta = rs.getMetaData();
			this.__columnCount = _rsMeta.getColumnCount();
			this.__columnTypes = new int[this.__columnCount];
			this.__columnNames = new String[this.__columnCount];
			for (int i = 0; i < this.__columnCount; i++) {
				this.__columnTypes[i] = _rsMeta.getColumnType(i + 1);
				// 若不使用 getColumnLabel 方法，则可能出现部分数据库驱动无法处理 as 同名的 BUG。
				this.__columnNames[i] = _rsMeta.getColumnLabel(i + 1);
			}
		}
		if (maxRow <= 0) {
			while (rs.next()) {
				this.processRowData(rs, _result);
				this.__rowCount++;
			}
		} else {
			int i = 0;
			while (rs.next()) {
				this.processRowData(rs, _result);
				this.__rowCount++;
				i++;
				if (i > maxRow) {
					break;
				}
			}
		}
		__resultDataSet = new ArrayList<T>(_result);
	}

}
