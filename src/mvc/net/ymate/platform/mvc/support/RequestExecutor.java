/**
 * <p>文件名:	RequestExecutor.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform_V2_1</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.ymate.platform.commons.lang.PairObject;
import net.ymate.platform.mvc.filter.IFilterChain;
import net.ymate.platform.mvc.view.IView;
import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.Validates;
import net.ymate.platform.validation.ValidationException;
import net.ymate.platform.validation.annotation.ValidateRule;
import net.ymate.platform.validation.annotation.Validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * RequestExecutor
 * </p>
 * <p>
 * MVC请求执行器；
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
 *          <td>2012-12-14下午4:30:27</td>
 *          </tr>
 *          </table>
 */
public class RequestExecutor {

	private static final Log _LOG = LogFactory.getLog(RequestExecutor.class);

	protected final RequestMeta requestMeta;
	protected final IFilterChain chain;

	/**
	 * 当前方法包含的参数验证规则配置
	 */
	protected PairObject<Validation, Map<String, ValidateRule[]>> validateRuleConf;
	
	/**
	 * 所有子类若想正确使用参数验证特性，必须在处理请求参数时将参数名与值手工添加到此映射中，映射中的值对象种类有：null与String、String[]和IUploadFileWrapper、IUploadFileWrapper[]
	 */
	protected Map<String, Object> validateFieldValues = new HashMap<String, Object>();

	/**
	 * 构造器
	 * 
	 * @param meta MVC请求元数据描述对象
	 */
	public RequestExecutor(RequestMeta meta) {
		this(meta, null);
	}

	/**
	 * 构造器
	 * 
	 * @param meta MVC请求元数据描述对象
	 * @param chain 拦截器执行链对象
	 */
	public RequestExecutor(RequestMeta meta, IFilterChain chain) {
		this.requestMeta = meta;
		this.chain = chain;
		validateRuleConf = Validates.loadValidateRule(meta.getMethod(), meta.getMethodParamNames());
	}

	/**
	 * @return 处理并返回方法对象invoke时所需的参数集合
	 */
	protected Object[] getMethodParams() {
		return new Object[this.requestMeta.getParameterTypes().length];
	}

	/**
	 * @param result 方法执行结果对象
	 * @return 处理执行结果转IView视图对象
	 * @throws Exception 抛出任何可能异常
	 */
	protected IView processMethodResultToView(Object result) throws Exception {
		return (IView) result;
	}

	/**
	 * @return 执行并返回执行结果视图对象
	 * @throws Exception 抛出任何可能异常
	 */
	public IView execute() throws Exception {
		_LOG.info("开始执行请求...");
		IView _view = null;
		if (chain != null) {
			_view = chain.doChain(this.requestMeta);
		}
		if (_view == null) {
			try {
				Object[] _params = this.getMethodParams();
				if (hasValidation()) {
					Set<ValidateResult> _results = Validates.execute(validateRuleConf.getKey(), validateRuleConf.getValue(), validateFieldValues);
					if (!_results.isEmpty()) {
						if (WebMVC.getConfig().getErrorHandlerClassImpl() != null) {
							_view = WebMVC.getConfig().getErrorHandlerClassImpl().onValidation(_results);
						}
						if (_view == null) {
							throw new ValidationException(_results.toString());
						}
					}
				}
				if (_view == null) {
					Object _result = this.requestMeta.getMethod().invoke(this.requestMeta.getTarget(), _params);
					_view = this.processMethodResultToView(_result);
				}
			} finally {
				validateFieldValues.clear();
			}
		}
		_LOG.info("请求执行完毕");
		return _view;
	}

	/**
	 * @return 判断当前方法是否包含参数验证注解
	 */
	protected boolean hasValidation() {
		return validateRuleConf.getKey() != null;
	}

}
