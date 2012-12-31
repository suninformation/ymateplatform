/**
 * <p>文件名:	IQueryOperator.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.operator;

import java.util.List;


/**
 * <p>
 * IQueryOperator
 * </p>
 * <p>
 * 数据库查询操作者接口定义类；
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
 *          <td>2010-12-25下午02:00:07</td>
 *          </tr>
 *          </table>
 */
public interface IQueryOperator<T> extends IOperator {

	/**
	 * @return 当前结果集是否可用，即是否为空或元素数量为 0
	 */
	public boolean isResultSetAvailable();

	/**
	 * 设置结果集数据处理对象
	 * 
	 * @param handler
	 */
	public void setResultSetHandler(IResultSetHandler<T> handler);

	/**
	 * @return 获取当前的结果集，若当前操作者对象尚未被执行将抛出运行时异常
	 */
	public List<T> getResultSet();

	/**
	 * 设置结果集返回最大记录数
	 * 
	 * @param maxRow
	 */
	public void setMaxRow(int maxRow);

}
