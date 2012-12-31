/**
 * <p>文件名:	AccessorEventContext.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Persistence</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.base;

import java.sql.Statement;

/**
 * <p>
 * AccessorEventContext
 * </p>
 * <p>
 * 访问器配置事件处理上下文关联类；
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
 *          <td>2011-8-30上午10:05:44</td>
 *          </tr>
 *          </table>
 */
public class AccessorEventContext {

	Statement statement;
	boolean isUpdate;
	boolean isBatch;

	/**
	 * 构造器
	 * 
	 * @param statement Statement对象
	 * @param isUpdate 是否为更新类操作
	 * @param isBatch 是否为批处理操作
	 */
	public AccessorEventContext(Statement statement, boolean isUpdate, boolean isBatch) {
		this.statement = statement;
		this.isUpdate = isUpdate;
		this.isBatch = isBatch;
	}

	/**
	 * @return the statement
	 */
	public Statement getStatement() {
		return statement;
	}

	/**
	 * @return the isUpdate
	 */
	public boolean isUpdate() {
		return isUpdate;
	}

	/**
	 * @return the isBatch
	 */
	public boolean isBatch() {
		return isBatch;
	}

}
