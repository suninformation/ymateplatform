/**
 * <p>文件名:	ExpressionUtils.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-Commons</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.commons.util;

/**
 * <p>
 * ExpressionUtils
 * </p>
 * <p>
 * 字符串表达式工具类，用于处理${variable}字符替换；<br/>
 * 例: I am ${name},and sex is ${sex}. <br>
 * name=Henry,sex=M <br>
 * result:I am Henry,and sex is M.
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
 *          <td>2010-12-20上午11:37:00</td>
 *          </tr>
 *          </table>
 */
public class ExpressionUtils {

	private final static String __pre = "\\$\\{";
	private final static String __suf = "\\}";
	private String __result;

	/**
	 * 创建表达式工具类实例对象
	 * 
	 * @param expressionStr
	 * @return
	 */
	public static ExpressionUtils bind(String expressionStr) {
		return new ExpressionUtils(expressionStr);
	}

	/**
	 * 构造器
	 */
	private ExpressionUtils(String expressionStr) {
		this.__result = expressionStr; // .replace("\\", "/");
	}

	/**
	 * 获取结果
	 * @return
	 */
	public String getResult() {
		return this.__result;
	}

	/**
	 * 设置变量值
	 * @param key 变量名称
	 * @param value 变量值
	 * @return
	 */
	public ExpressionUtils set(String key, String value) {
		String namePattern = __pre + key + __suf;
		this.__result = this.__result.replaceAll(namePattern, value.replace("\\", "/"));
		return this;
	}

}
