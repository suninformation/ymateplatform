package ${packageName}.model;

import net.ymate.platform.persistence.base.IEntityPK;
import net.ymate.platform.persistence.annotation.Property;
import net.ymate.platform.persistence.annotation.PK;

/**
 * <p>
 * ${modelName?cap_first}PK
 * </p>
 * <p>
 * Code Generator By yMatePlatform  ${lastUpdateTime?string("yyyy-MM-dd a HH:mm:ss")}
 * </p>
 * 
 * @author JdbcScaffold
 * @version 0.0.0
 */
@PK
public class ${modelName?cap_first}PK implements IEntityPK {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	<#list primaryKeyList as field>
	@Property(name = "${field.columnName}"<#if (field.autoIncrement)>, isAutoIncrement=true</#if>)
	private ${field.varType} ${field.varName};
	</#list>

	/**
	 * 构造器
	 */
	public ${modelName?cap_first}PK() {
	}

	/**
	 * 构造器
	 <#list primaryKeyList as field>
	 *	@param ${field.varName}
	</#list>
	 */
	public ${modelName?cap_first}PK(<#list primaryKeyList as field>${field.varType} ${field.varName}<#if field_has_next>, </#if></#list>) {
		<#list primaryKeyList as field>
		this.${field.varName} = ${field.varName};
		</#list>
	}

	<#list primaryKeyList as field>

	/**
	 * @return the ${field.varName}
	 */
	public ${field.varType} get${field.varName?cap_first}() {
		return ${field.varName};
	}

	/**
	 * @param ${field.varName} the ${field.varName} to set
	 */
	public void set${field.varName?cap_first}(${field.varType} ${field.varName}) {
		this.${field.varName} = ${field.varName};
	}

	</#list>

}
