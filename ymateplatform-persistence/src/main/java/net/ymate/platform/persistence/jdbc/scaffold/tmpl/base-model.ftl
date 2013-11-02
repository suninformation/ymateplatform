package ${packageName}.model;

import net.ymate.platform.persistence.jdbc.IEntity;

/**
 * <p>
 * BaseEntity
 * </p>
 * <p>
 * Code Generator By yMatePlatform  ${lastUpdateTime?string("yyyy-MM-dd a HH:mm:ss")}
 * </p>
 * 
 * @author JdbcScaffold
 * @version 0.0.0
 */
public abstract class BaseModel<PK> implements IEntity<PK> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造器
	 */
	public BaseModel() {
	}

}
