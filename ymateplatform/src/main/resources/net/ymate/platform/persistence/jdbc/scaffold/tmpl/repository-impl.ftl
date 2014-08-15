package ${packageName}.repository.impl;

import net.ymate.platform.persistence.support.annotation.Repository;
import ${packageName}.repository.I${repositoryName?cap_first}Repository;
import net.ymate.platform.persistence.jdbc.support.AbstractEntityRepository;

/**
 * <p>
 * ${repositoryName?cap_first}<#if (isUseClassSuffix)>Repository</#if>
 * </p>
 * <p>
 * Code Generator By yMatePlatform  ${lastUpdateTime?string("yyyy-MM-dd a HH:mm:ss")}
 * </p>
 * 
 * @author JdbcScaffold
 * @version 0.0.0
 */
@Repository
public class ${repositoryName?cap_first}<#if (isUseClassSuffix)>Repository</#if> extends AbstractEntityRepository implements I${repositoryName?cap_first}<#if (isUseClassSuffix)>Repository</#if> {

	// 可以在这里添加您的业务接口的代码实现哦！

}
