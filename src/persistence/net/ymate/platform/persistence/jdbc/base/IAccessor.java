/**
 * <p>文件名:	IAccessor.java</p>
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
 * IAccessor
 * </p>
 * <p>
 * 访问器接口定义类，用于提供 Statement 对象生成方式；
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
 *          <td>2011-8-30上午09:47:06</td>
 *          </tr>
 *          </table>
 */
public interface IAccessor {

	/**
	 * @param conn
	 * @return 使用Statement方式进行数据库访问操作，用于直接使用SQL文；
	 * @throws SQLException
	 */
	public abstract Statement getStatement(Connection conn) throws SQLException;

	/**
	 * @param conn 访问数据库的连接对象
	 * @return 使用PerparedStatement（参数化）方式进行数据库访问操作，用于直接使用SQL文；
	 * @throws SQLException
	 */
	public abstract PreparedStatement getPreparedStatement(Connection conn) throws SQLException;
	
	/**
	 * @param conn 访问数据库的连接对象
	 * @return 使用CallableStatement方式进行数据库访问操作，用于访问存储过程；
	 * @throws SQLException
	 */
	public abstract CallableStatement getCallableStatement(Connection conn) throws SQLException;


	/**
	 * @return 获取访问器配置事件处理对象
	 */
	public abstract IAccessorCfgEvent getAccessorCfgEvent();

}
