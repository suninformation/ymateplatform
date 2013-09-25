/**
 * <p>文件名:	JdbcScaffold.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	ymateplatform</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.persistence.jdbc.scaffold;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.ResourceUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.module.base.YMP;
import net.ymate.platform.persistence.jdbc.IConnectionHolder;
import net.ymate.platform.persistence.jdbc.ISession;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.operator.impl.ArrayResultSetHandler;
import net.ymate.platform.persistence.jdbc.support.EntityMeta;
import net.ymate.platform.persistence.jdbc.support.ResultSetHelper;

import org.apache.commons.lang.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * <p>
 * JdbcScaffold
 * </p>
 * <p>
 * 持久层代码生成脚手架程序，通过已有数据库表结构返向生成Java代码；
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
 *          <td>2013年9月22日下午9:44:09</td>
 *          </tr>
 *          </table>
 */
public class JdbcScaffold {

//	private static final Log _LOG = LogFactory.getLog(JdbcScaffold.class);

	private String TEMPLATE_ROOT_PATH = JdbcScaffold.class.getPackage().getName().replace(".", "/");
	private File TARGET_ROOT_PATH;
	private Configuration FREEMARKER_CONF;
	private Properties JDBC_SCAFFOLD_CONF = new Properties();

	/**
	 * 构造器
	 * 
	 * @param outputPath 自定义输出路径
	 * @throws IOException
	 */
	public JdbcScaffold(String outputPath) throws IOException {
		FREEMARKER_CONF = new Configuration();
		FREEMARKER_CONF.setClassForTemplateLoading(JdbcScaffold.class, "/");
		FREEMARKER_CONF.setObjectWrapper(new DefaultObjectWrapper());
		FREEMARKER_CONF.setDefaultEncoding("UTF-8");
		//
		JDBC_SCAFFOLD_CONF.load(ResourceUtils.getResourceAsStream("ymp-scaffold-conf.properties", JdbcScaffold.class));
		if (StringUtils.isNotBlank(outputPath)) {
			TARGET_ROOT_PATH = new File(outputPath);
		} else {
			TARGET_ROOT_PATH = new File(JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jdbc.output_path"));
		}
		if (TARGET_ROOT_PATH == null || !TARGET_ROOT_PATH.exists() || TARGET_ROOT_PATH.isFile()) {
			throw new Error("The target output path \"" + TARGET_ROOT_PATH.getPath() + "\" is empty or is not a directory.");
		}
	}

	/**
	 * 主程序
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		YMP.initialize();
		JdbcScaffold _scaffold = new JdbcScaffold((args == null || args.length == 0) ? null : args[0]);
		_scaffold.createEntityClassFiles();
		_scaffold.createRepositoryClassFiles();
	}

	/**
	 * 根据数据库表定义创建实体类文件
	 */
	public void createEntityClassFiles() {
		Map<String, Object> _propMap = buildPropMap();
		//
		boolean _isUseBaseModel = new BlurObject(JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jdbc.use_base_model", "false")).toBooleanValue();
		_propMap.put("isUseBaseModel", _isUseBaseModel);
		if (_isUseBaseModel) {
			buildTargetFile("/model/BaseModel.java", "/tmpl/base-model.ftl", _propMap);
		}
		//
		List<String> _tableList = Arrays.asList(StringUtils.split(JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jdbc.table_gen_list"), "|"));
		if (_tableList == null || _tableList.isEmpty()) {
			_tableList = getTableNames();
		}
		//
		String _dbName = JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jdbc.db_name");
		String _dbUser = JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jdbc.db_username");
		String[] _prefixs = StringUtils.split(JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jdbc.table_prefix"), '|');
		boolean  _isRemovePrefix = new BlurObject(JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jdbc.remove_table_prefix")).toBooleanValue();
		List<String> _tableExcludeList = Arrays.asList(StringUtils.split(JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jdbc.table_exclude_list", "").toLowerCase(), "|"));
		for (String _tableName : _tableList) {
			// 判断黑名单
			if (!_tableExcludeList.isEmpty() && _tableExcludeList.contains(_tableName.toLowerCase())) {
				continue;
			}
			TableMeta _tableMeta = getTableMeta(_dbName, _dbUser, _tableName);
			if (_tableMeta != null) {
				String _modelName = null;
				for (String _prefix : _prefixs) {
					if (_tableName.startsWith(_prefix)) {
						if (_isRemovePrefix) {
							_tableName = _tableName.substring(_prefix.length());
						}
						_modelName = EntityMeta.buildFieldNameToClassAttribute(_tableName);
						break;
					}
				}
				if (StringUtils.isBlank(_modelName)) {
					_modelName = EntityMeta.buildFieldNameToClassAttribute(_tableName);
				}
				//
				_propMap.put("tableName", _tableName);
				_propMap.put("modelName", _modelName);
				List<Attr> _fieldList = new ArrayList<Attr>();
				List<Attr> _allFieldList = new ArrayList<Attr>(); // 用于生成字段名称常量
				if (_tableMeta.getPkSet().size() > 1) {
					_propMap.put("primaryKeyType", _modelName + "PK");
					_propMap.put("primaryKeyName", StringUtils.uncapitalize((String) _propMap.get("primaryKeyType")));
					List<Attr> _primaryKeyList = new ArrayList<Attr>();
					_propMap.put("primaryKeyList", _primaryKeyList);
					_fieldList.add(new Attr((String) _propMap.get("primaryKeyType"), (String) _propMap.get("primaryKeyName"), null, false));
					//
					for (String pkey : _tableMeta.getPkSet()) {
						ColumnInfo _ci = _tableMeta.getFieldMap().get(pkey);
						_primaryKeyList.add(new Attr(_ci.getColumnType(), StringUtils.uncapitalize(EntityMeta.buildFieldNameToClassAttribute(pkey.toLowerCase())), pkey, _ci.isAutoIncrement()));
						_allFieldList.add(new Attr("String", _ci.getColumnName().toUpperCase(), _ci.getColumnName(), false));
					}
					for (String key : _tableMeta.getFieldMap().keySet()) {
						if (_tableMeta.getPkSet().contains(key)) {
							continue;
						}
						ColumnInfo _ci = _tableMeta.getFieldMap().get(key);
						_fieldList.add(new Attr(_ci.getColumnType(), StringUtils.uncapitalize(EntityMeta.buildFieldNameToClassAttribute(key.toLowerCase())), key, _ci.isAutoIncrement()));
						_allFieldList.add(new Attr("String", _ci.getColumnName().toUpperCase(), _ci.getColumnName(), false));
					}
				} else {
					_propMap.put("primaryKeyType", _tableMeta.getFieldMap().get(_tableMeta.getPkSet().get(0)).getColumnType());
					_propMap.put("primaryKeyName", StringUtils.uncapitalize(EntityMeta.buildFieldNameToClassAttribute(_tableMeta.getPkSet().get(0))));
					for (String key : _tableMeta.getFieldMap().keySet()) {
						ColumnInfo _ci = _tableMeta.getFieldMap().get(key);
						_fieldList.add(new Attr(_ci.getColumnType(), StringUtils.uncapitalize(EntityMeta.buildFieldNameToClassAttribute(key.toLowerCase())), key, _ci.isAutoIncrement()));
						_allFieldList.add(new Attr("String", _ci.getColumnName().toUpperCase(), _ci.getColumnName(), false));
					}
				}
				_propMap.put("fieldList", _fieldList);
				_propMap.put("allFieldList", _allFieldList);
				//
				buildTargetFile("/model/" + _modelName + "Model.java", "/tmpl/model-entity.ftl", _propMap);
				//
				if (_tableMeta.getPkSet().size() > 1) {
					_propMap.put("modelName", _modelName);
					if (_tableMeta.getPkSet().size() > 1) {
						List<Attr> _primaryKeyList = new ArrayList<Attr>();
						_propMap.put("primaryKeyList", _primaryKeyList);
						//
						for (String pkey : _tableMeta.getPkSet()) {
							ColumnInfo _ci = _tableMeta.getFieldMap().get(pkey);
							_primaryKeyList.add(new Attr(_ci.getColumnType(), StringUtils.uncapitalize(EntityMeta.buildFieldNameToClassAttribute(pkey.toLowerCase())), pkey, _ci.isAutoIncrement()));
						}
					}
					buildTargetFile("/model/" + _modelName + "PK.java", "/tmpl/model-pk.ftl", _propMap);
				}
			}
		}
	}

	/**
	 * 根据配置文件中的预生成的存储器对象名称列表生成存储器类文件
	 */
	public void createRepositoryClassFiles() {
		Map<String, Object> _propMap = buildPropMap();
		String[] _repositoryList = StringUtils.split(JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jdbc.repository_name_list"), "|");
		String _repositoryName = null;
		for (String _name : _repositoryList) {
			_repositoryName = EntityMeta.buildFieldNameToClassAttribute(_name);
			_propMap.put("repositoryName", _repositoryName);
			buildTargetFile("/repository/I" + _repositoryName + "Repository.java", "/tmpl/repository-interface.ftl", _propMap);
			//
			buildTargetFile("/repository/impl/" + _repositoryName + "Repository.java", "/tmpl/repository-impl.ftl", _propMap);
		}
	}

	private void buildTargetFile(String targetFileName, String tmplFile, Map<String, Object> propMap) {
		Writer _outWriter = null;
		try {
			File _outputFile = new File(TARGET_ROOT_PATH, new File(((String) propMap.get("packageName")).replace('.', '/'), targetFileName).getPath());
			FileUtils.mkdirs(_outputFile.getParent(), true);
			Template _template = FREEMARKER_CONF.getTemplate(TEMPLATE_ROOT_PATH + tmplFile);
			_outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(_outputFile), StringUtils.defaultIfEmpty(FREEMARKER_CONF.getOutputEncoding(), FREEMARKER_CONF.getDefaultEncoding())));
			_template.process(propMap, _outWriter);
			System.out.println("Output file \"" + _outputFile + "\".");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			if (_outWriter != null) {
				try {
					_outWriter.flush();
					_outWriter.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private Map<String, Object> buildPropMap() {
		Map<String, Object> _propMap = new HashMap<String, Object>();
		_propMap.put("packageName", JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jdbc.package", ""));
		_propMap.put("lastUpdateTime", new Date());
		return _propMap;
	}

	/**
	 * @return 获取数据库表名称集合
	 */
	private List<String> getTableNames() {
		List<String> _tableNames = new ArrayList<String>();
		ISession _session = null;
		try {
			_session = JDBC.openSession();
			String _dbType = JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jbdc.db_type", "unknow");
			String _sql = null;
			if ("mysql".equalsIgnoreCase(_dbType)) {
				_sql = "show tables";
			} else if ("oracle".equalsIgnoreCase(_dbType)) {
				_sql = "select t.table_name from user_tables t";
			} else if ("sqlserver".equalsIgnoreCase(_dbType)) {
				_sql = "select name from sysobjects where xtype='U'";
			} else {
				throw new Error("The current database \"" + _dbType + "\" type not supported");
			}
			ResultSetHelper _helper = ResultSetHelper.bind(_session.findAll(_sql, new ArrayResultSetHandler(), null));
			for (int _idx = 0; _idx < _helper.getRowCount(); _idx++) {
				_helper.move(_idx);
				_tableNames.add(_helper.getAsString(0));
			}
		} catch (Throwable e) {
			if (e instanceof Error) {
				throw (Error) e;
			}
			throw new Error(RuntimeUtils.unwrapThrow(e));
		} finally {
			if (_session != null) _session.close();
		}
		return _tableNames;
	}

	/**
	 * @param dbName 数据库名称
	 * @param dbUserName 所属用户名称
	 * @param tableName 表名称
	 * @return 获取数据表元数据描述对象
	 */
	private TableMeta getTableMeta(String dbName, String dbUserName, String tableName) {
		IConnectionHolder _connHolder = null;
		Statement _statement = null;
		ResultSet _resultSet = null;
		Map<String, ColumnInfo> _tableFields = new LinkedHashMap<String, ColumnInfo>();
		List<String> _pkFields = new LinkedList<String>();
		TableMeta _meta = new TableMeta(_pkFields, _tableFields);
		try {
			_connHolder = JDBC.getConnectionHolder();
			String _dbType = JDBC_SCAFFOLD_CONF.getProperty("ymp.scaffold.jbdc.db_type", "unknow");
			_resultSet = _connHolder.getConnection().getMetaData().getPrimaryKeys(dbName, _dbType.equalsIgnoreCase("oracle") ? dbUserName.toUpperCase() : dbUserName, tableName);
			if (_resultSet == null) {
				_meta = null;
				System.err.println("Database table \"" + tableName + "\" primaryKey resultSet is null, ignored");
			} else {
				while (_resultSet.next()) {
					_pkFields.add(_resultSet.getString(4).toLowerCase());
				}
				if (_pkFields.isEmpty()) {
					_meta = null;
					System.err.println("Database table \"" + tableName + "\" does not set the primary key, ignored");
				} else {
					_statement = _connHolder.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					_resultSet = _statement.executeQuery("select * from " + tableName);
					ResultSetMetaData _rsMetaData = _resultSet.getMetaData();
					for (int _idx = 1; _idx <= _rsMetaData.getColumnCount(); _idx++) {
						_tableFields.put(_rsMetaData.getColumnName(_idx).toLowerCase(), new ColumnInfo(_rsMetaData.getColumnName(_idx).toLowerCase(), compressType(_rsMetaData.getColumnClassName(_idx)), _rsMetaData.isAutoIncrement(_idx)));
					}
					//
					System.err.println("TABLE_NAME: " + tableName + " ---------------->>");
					System.err.println("COLUMN_NAME\tPK\tCOLUMN_TYPE\tIS_AUTOINCREMENT");
					for (ColumnInfo _cInfo : _tableFields.values()) {
						System.err.println(_cInfo.getColumnName() + "\t" + _pkFields.contains(_cInfo.getColumnName()) + "\t" + _cInfo.getColumnType() + "\t" + _cInfo.isAutoIncrement());
					}
				}
			}
		} catch (Throwable e) {
			if (e instanceof Error) {
				throw (Error) e;
			}
			throw new Error(RuntimeUtils.unwrapThrow(e));
		} finally {
			_connHolder.release();
			_statement = null;
			_resultSet = null;
		}
		return _meta;
	}

	private String compressType(String javaType) {
		return javaType.substring(javaType.lastIndexOf(".") + 1);
	}

	/**
	 * <p>
	 * TableMeta
	 * </p>
	 * <p>
	 * 数据表元数据描述类；
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
	 *          <td>2011-10-26上午01:33:57</td>
	 *          </tr>
	 *          </table>
	 */
	private class TableMeta {
//		private String tableName;
		private List<String> pkSet;
		private Map<String, ColumnInfo> fieldMap;

		public TableMeta(/* String tableName, */List<String> pkSet, Map<String, ColumnInfo> fieldMap) {
//			this.tableName = tableName;
			this.pkSet = pkSet;
			this.fieldMap = fieldMap;
		}

//		public String getTableName() {
//			return tableName;
//		}

		public List<String> getPkSet() {
			return pkSet;
		}

		public Map<String, ColumnInfo> getFieldMap() {
			return fieldMap;
		}
	}

	/**
	 * <p>
	 * ColumnInfo
	 * </p>
	 * <p>
	 * 表字段信息类；
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
	 *          <td>2011-10-8下午11:19:57</td>
	 *          </tr>
	 *          </table>
	 */
	private class ColumnInfo {
	
		private String columnName;
		private String columnType;
		private boolean autoIncrement;

		/**
		 * 构造器
		 * @param columnName
		 * @param columnType
		 * @param autoIncrement
		 */
		public ColumnInfo(String columnName, String columnType, boolean autoIncrement) {
			this.columnName = columnName;
			this.autoIncrement = autoIncrement;
			this.columnType = columnType;
		}

		public String getColumnName() {
			return columnName;
		}

		public boolean isAutoIncrement() {
			return autoIncrement;
		}

		public String getColumnType() {
			return columnType;
		}

	}

	public class Attr {
		String varType;
		String varName;
		String columnName;
		boolean autoIncrement;
		
		public Attr(String varType, String varName, String columnName, boolean autoIncrement) {
			this.varName = varName;
			this.varType = varType;
			this.columnName = columnName;
			this.autoIncrement = autoIncrement;
		}

		public String getVarType() {
			return varType;
		}

		public String getVarName() {
			return varName;
		}

		public String getColumnName() {
			return columnName;
		}

		public boolean isAutoIncrement() {
			return autoIncrement;
		}

		@Override
		public String toString() {
			return this.getVarName();
		}

	}

}
