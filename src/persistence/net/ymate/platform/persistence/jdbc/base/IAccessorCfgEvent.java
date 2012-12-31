/**
 * <p>文件名:	IAccessorCfgEvent.java</p>
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

/**
 * <p>
 * IAccessorCfgEvent
 * </p>
 * <p>
 * 访问器配置事件处理接口定义类，用于二次开发时方便对访问器的功能扩展；
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
 *          <td>2011-8-30上午09:51:01</td>
 *          </tr>
 *          </table>
 */
public interface IAccessorCfgEvent {

	/**
	 * @return 创建自定义 Statement 对象，默认 null
	 */
	public Statement getStatement(Connection conn) throws SQLException;

	/**
	 * @param conn
	 * @param sql
	 * @return 创建自定义 CallableStatement 对象，默认 null
	 */
	public CallableStatement getCallableStatement(Connection conn, String sql) throws SQLException;

	/**
	 * @param conn
	 * @param sql
	 * @return 创建自定义 PerparedStatement 对象，默认 null
	 */
	public PreparedStatement getPreparedStatement(Connection conn, String sql) throws SQLException;

	/**
	 * Statement 对象执行之前调用
	 * 
	 * @param context
	 * @throws SQLException
	 */
	public void beforeStatementExecution(AccessorEventContext context) throws SQLException;

	/**
	 * Statement 对象执行之后调用
	 * 
	 * @throws SQLException
	 */
	public void afterStatementExecution(AccessorEventContext context) throws SQLException;

	/**
	 * 向驱动程序提供关于方向的提示，在使用此 Statement 对象创建的 ResultSet 对象中将按该方向处理行。默认值为 ResultSet.FETCH_FORWARD。
	 * 注意，此方法为此 Statement 对象生成的结果集合设置默认获取方向。每个结果集合都具有它自己用于获取和设置其自身获取方向的方法。
	 * 取值范围：ResultSet.FETCH_FORWARD、ResultSet.FETCH_REVERSE 和 ResultSet.FETCH_UNKNOWN
	 * 
	 * @return
	 */
	public int getFetchDirection();

	/**
	 * 为 JDBC 驱动程序提供一个提示，它提示此 Statement 生成的 ResultSet 对象需要更多行时应该从数据库获取的行数。
	 * 指定的行数仅影响使用此语句创建的结果集合。如果指定的值为 0，则忽略该提示。默认值为 0。
	 * 
	 * @return
	 */
	public int getFetchSize();

	/**
	 * 设置此 Statement 对象生成的 ResultSet 对象中字符和二进制列值可以返回的最大字节数限制。 此限制仅应用于
	 * BINARY、VARBINARY、LONGVARBINARY、CHAR、VARCHAR、NCHAR、NVARCHAR、LONGNVARCHAR 和
	 * LONGVARCHAR 字段。如果超过了该限制，则直接丢弃多出的数据。为了获得最大的可移植性，应该使用大于 256 的值。
	 * 以字节为单位的新列大小限制；0 表示没有任何限制
	 * 
	 * @return
	 */
	public int getMaxFieldSize();

	/**
	 * 将此 Statement 对象生成的所有 ResultSet 对象可以包含的最大行数限制设置为给定数。如果超过了该限制，则直接撤消多出的行。
	 * 新的最大行数限制；0 表示没有任何限制
	 * 
	 * @return
	 */
	public int getMaxRows();

	/**
	 * 将驱动程序等待 Statement 对象执行的秒数设置为给定秒数。如果超过该限制，则抛出 SQLException。 JDBC
	 * 驱动程序必须将此限制应用于 execute、executeQuery 和 executeUpdate 方法。 JDBC
	 * 驱动程序实现也可以将此限制应用于 ResultSet 方法（有关详细信息，请参考驱动程序供应商文档）。
	 * 以秒为单位的查询超时限制；0 表示没有任何限制
	 * 
	 * @return
	 */
	public int getQueryTimeout();

}
