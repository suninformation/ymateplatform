/**
 * <p>文件名:	AbstractEntityRepository.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.support;

import java.util.List;

import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.persistence.jdbc.ConnectionException;
import net.ymate.platform.persistence.jdbc.IEntity;
import net.ymate.platform.persistence.jdbc.ISession;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.operator.OperatorException;
import net.ymate.platform.persistence.jdbc.query.PageResultSet;
import net.ymate.platform.persistence.jdbc.transaction.Trans;
import net.ymate.platform.persistence.jdbc.transaction.TransResultTask;
import net.ymate.platform.persistence.jdbc.transaction.TransactionException;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * AbstractEntityRepository
 * </p>
 * <p>
 * 实体存储器接口抽象实现;
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
 *          <td>2013-7-16下午1:06:46</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractEntityRepository<Entity extends IEntity<PK>, PK> implements IEntityRepository<Entity, PK> {

	private Class<Entity> __entityClass;
	private Class<PK> __pkClass;
	private String __dsName;

	/**
	 * 构造器
	 */
	@SuppressWarnings("unchecked")
	public AbstractEntityRepository() {
		List<Class<?>> _c = ClassUtils.getParameterizedTypes(getClass());
		__entityClass = (Class<Entity>) _c.get(0);
		__pkClass = (Class<PK>) _c.get(1);
	}

	/**
	 * @return 获取实体对象类型
	 */
	protected Class<Entity> getEntityClass() {
		return this.__entityClass;
	}

	/**
	 * @return 获取主键类型
	 */
	protected Class<?> getPrimaryKeyClass() {
		return this.__pkClass;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.support.IEntityRepository#setDataSourceName(java.lang.String)
	 */
	public void setDataSourceName(String dsName) {
		this.__dsName = dsName;
	}

	/**
	 * @return 获取当前数据源名称，若为空则返回默认数据源名称
	 */
	protected String getDataSourceName() {
		return StringUtils.defaultIfEmpty(this.__dsName, JDBC.DATASOURCE_DEFAULT_NAME);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.support.IEntityRepository#get(java.lang.Object, java.lang.String[])
	 */
	public Entity get(PK id, String... fieldFilter) throws OperatorException, ConnectionException {
		ISession _session = JDBC.openSession(this.getDataSourceName());
		try {
			return _session.find(this.getEntityClass(), id, fieldFilter);
		} finally {
			_session.close();
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.support.IEntityRepository#save(net.ymate.platform.persistence.jdbc.IEntity)
	 */
	public Entity save(final Entity t) throws OperatorException, TransactionException {
		return Trans.exec(new TransResultTask<Entity>() {
			public void doTask() throws ConnectionException, OperatorException {
				ISession _session = JDBC.openSession(getDataSourceName());
				try {
					this.setResult(_session.insert(t));
				} finally {
					_session.close();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.support.IEntityRepository#update(net.ymate.platform.persistence.jdbc.IEntity, java.lang.String[])
	 */
	public Entity update(final Entity t, final String... fieldFilter) throws OperatorException, TransactionException {
		return Trans.exec(new TransResultTask<Entity>() {
			public void doTask() throws ConnectionException, OperatorException {
				ISession _session = JDBC.openSession(getDataSourceName());
				try {
					this.setResult(_session.update(t, fieldFilter));
				} finally {
					_session.close();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.support.IEntityRepository#delete(net.ymate.platform.persistence.jdbc.IEntity)
	 */
	public Entity delete(final Entity t) throws OperatorException, TransactionException {
		return Trans.exec(new TransResultTask<Entity>() {
			public void doTask() throws ConnectionException, OperatorException {
				ISession _session = JDBC.openSession(getDataSourceName());
				try {
					this.setResult(_session.delete(t));
				} finally {
					_session.close();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.support.IEntityRepository#delete(java.lang.Object)
	 */
	public boolean delete(final PK pk) throws OperatorException, TransactionException {
		return Trans.exec(new TransResultTask<Boolean>() {
			public void doTask() throws ConnectionException, OperatorException {
				ISession _session = JDBC.openSession(getDataSourceName());
				try {
					this.setResult(_session.delete(getEntityClass(), pk) > 0);
				} finally {
					_session.close();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.support.IEntityRepository#findAll(java.lang.String, java.lang.Object[], java.lang.String[])
	 */
	public List<Entity> findAll(String cond, Object[] params, String... fieldFilter) throws OperatorException, ConnectionException {
		ISession _session = JDBC.openSession(this.getDataSourceName());
		try {
			return _session.findAll(this.getEntityClass(), cond, fieldFilter, params);
		} finally {
			_session.close();
		}
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.jdbc.support.IEntityRepository#findAll(java.lang.String, java.lang.Object[], int, int, boolean, java.lang.String[])
	 */
	public PageResultSet<Entity> findAll(String cond, Object[] params, int pageSize, int currentPage, boolean allowRecordCount, String... fieldFilter) throws OperatorException, ConnectionException {
		ISession _session = JDBC.openSession(this.getDataSourceName());
		try {
			return _session.findAll(this.getEntityClass(), cond, fieldFilter, pageSize, currentPage, allowRecordCount, params);
		} finally {
			_session.close();
		}
	}

}
