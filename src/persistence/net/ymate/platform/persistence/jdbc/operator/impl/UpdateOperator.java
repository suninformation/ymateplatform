/**
 * <p>文件名:	UpdateOperator.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.operator.impl;

import java.sql.SQLException;

import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.base.impl.GenericAccessor;
import net.ymate.platform.persistence.jdbc.operator.AbstractOperator;
import net.ymate.platform.persistence.jdbc.operator.IUpdateOperator;

/**
 * <p>
 * UpdateOperator
 * </p>
 * <p>
 * 数据库更新操作器实现类；
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
 *          <td>2011-9-23上午10:38:24</td>
 *          </tr>
 *          </table>
 */
public class UpdateOperator extends AbstractOperator implements IUpdateOperator {

	/**
	 * 更新操作影响的记录行数
	 */
	private int __effectCounts;

	/**
	 * 构造器
	 */
	public UpdateOperator() {
	}

	/**
	 * 构造器
	 * 
	 * @param sql SQL 语句
	 */
	public UpdateOperator(String sql) {
		this.setSql(sql);
	}

	/**
	 * 构造器
	 * 
	 * @param sql SQL 语句
	 * @param conn 数据库连接对象
	 */
	public UpdateOperator(String sql, IConnectionHolder conn) {
		this.setSql(sql);
		this.setConnection(conn);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#__execute()
	 */
	protected int __execute() throws SQLException {
		this.__effectCounts = this.doUpdate(new GenericAccessor(this.getSql(), this.getParameters(), this.getAccessorCfgEvent()) , this.getConnection());
		return this.__effectCounts;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.AbstractOperator#__parametersToString()
	 */
	protected String __parametersToString() {
		return this.getParameters().toString();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.operator.IUpdateOperator#getEffectCounts()
	 */
	public int getEffectCounts() {
		return this.__effectCounts;
	}

}
