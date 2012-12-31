/**
 * <p>文件名:	IOperator.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.operator;

import java.util.List;

import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.base.IAccessorCfgEvent;
import net.ymate.platform.persistence.jdbc.base.SqlParameter;

/**
 * <p>
 * IOperator
 * </p>
 * <p>
 * 数据库操作器接口定义类；
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
 *          <td>2011-9-22下午03:46:12</td>
 *          </tr>
 *          </table>
 */
public interface IOperator {

	/**
	 * 执行操作
	 * 
	 * @throws OperatorException
	 */
	public void execute() throws OperatorException;

	/**
	 * 使用外部数据库连接对象执行操作
	 * 
	 * @param conn
	 * @throws OperatorException
	 */
	public void execute(IConnectionHolder conn) throws OperatorException;

	/**
	 * 设置预执行SQL语句串
	 * 
	 * @param sql
	 */
	public void setSql(String sql);

	/**
	 * @return 获取执行SQL语句串
	 */
	public String getSql();

	/**
	 * 设置当前使用的数据库连接对象
	 * 
	 * @param conn
	 */
	public void setConnection(IConnectionHolder conn);

	/**
	 * @return 获取当前使用的数据库连接对象
	 */
	public IConnectionHolder getConnection();

	/**
	 * @return 当前数据库连接是否可用
	 */
	public boolean isConnectionAvailable();

	/**
	 * @return 是否已执行过操作
	 */
	public boolean isExecuted();

	/**
	 * 是否显示 SQL 语句
	 * 
	 * @param showSql
	 */
	public void setShowSql(boolean showSql);

	/**
	 * @return 是否显示 SQL 语句
	 */
	public boolean isShowSql();

	/**
	 * @return 获取SQL参数集合
	 */
	public List<SqlParameter> getParameters();

	/**
	 * 添加SQL参数，若参数为NULL则忽略
	 * 
	 * @param parameter
	 */
	public void addParameter(SqlParameter parameter);

	/**
	 * 添加SQL参数，若参数为NULL则将默认向SQL传递Null值对象
	 * 
	 * @param parameter
	 */
	public void addParameter(Object parameter);

	/**
	 * @return 获取访问器配置事件处理接口实现
	 */
	public IAccessorCfgEvent getAccessorCfgEvent();

	/**
	 * 设置访问器配置事件处理接口实现
	 * 
	 * @param config
	 */
	public void setAccessorCfgEvent(IAccessorCfgEvent config);

	/**
	 * @return 获取本次操作所消耗的时间（单位：毫秒值）
	 */
	public long getExpenseTime();

}
