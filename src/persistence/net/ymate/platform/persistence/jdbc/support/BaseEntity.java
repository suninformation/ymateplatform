/**
 * <p>文件名:	BaseEntity.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.support;

import net.ymate.platform.persistence.jdbc.IEntity;

/**
 * <p>
 * BaseEntity
 * </p>
 * <p>
 * 实体模型接口抽象实现类，并集成实体存储器接口；
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
 *          <td>2013-7-16下午5:22:15</td>
 *          </tr>
 *          </table>
 */
public abstract class BaseEntity<Entity extends IEntity<PK>, PK> extends AbstractEntityRepository<Entity, PK> implements IEntity<PK> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2280348400927782598L;

	/**
	 * 构造器
	 */
	public BaseEntity() {
	}

}
