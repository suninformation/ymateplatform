package ${packageName}.model;

import net.ymate.platform.persistence.base.IEntity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
