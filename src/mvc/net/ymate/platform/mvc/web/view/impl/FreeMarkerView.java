/**
 * <p>文件名:	FreeMarkerView.java</p>
 * <p>版权:		详见产品版权说明书</p>
 * <p>公司:		YMateSoft Co., Ltd.</p>
 * <p>项目名：	yMatePlatform-WebMVC</p>
 * <p>作者：		刘镇(suninformation@163.com)</p>
 */
package net.ymate.platform.mvc.web.view.impl;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map.Entry;

import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.view.AbstractWebView;

import org.apache.commons.lang.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * <p>
 * FreeMarkerView
 * </p>
 * <p>
 * FreeMarker视图实现类；
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
 *          <td>2011-10-31下午08:45:22</td>
 *          </tr>
 *          </table>
 */
public class FreeMarkerView extends AbstractWebView {

	public static final String FREEMARKER_CONFIG = "__freemarker_config";

	protected String path;

	/**
	 * 构造器
	 */
	public FreeMarkerView() {
	}

	/**
	 * 构造器
	 * 
	 * @param path FTL文件路径
	 */
	public FreeMarkerView(String path) {
		this.path = path;
	}

	/**
	 * 构造器
	 * 
	 * @param path FTL文件路径
	 * @param key
	 * @param value
	 */
	public FreeMarkerView(String path, String key, Object value) {
		this.path = path;
		this.addAttribute(key, value);
	}

	protected Configuration processPath() {
		Configuration _freemarkerCfg = (Configuration) WebContext.getServletContext().getAttribute(FREEMARKER_CONFIG);
		if (_freemarkerCfg == null) {
			_freemarkerCfg = new Configuration();
			_freemarkerCfg.setDefaultEncoding("UTF-8");
			_freemarkerCfg.setServletContextForTemplateLoading(WebContext.getServletContext(), this.getBaseViewPath());
	        _freemarkerCfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
			WebContext.getServletContext().setAttribute(FREEMARKER_CONFIG, _freemarkerCfg);
		}
		//
		if (StringUtils.isNotBlank(getContentType())) {
			WebContext.getResponse().setContentType(getContentType());
		}
		for (Entry<String, Object> entry : getAttributes().entrySet()) {
			WebContext.getRequest().setAttribute(entry.getKey(), entry.getValue());
		}
		//
		if (StringUtils.isBlank(this.path)) {
			String _mapping = WebContext.getWebRequestContext().getRequestMapping();
			if (_mapping.endsWith("/")) {
				_mapping.substring(0, _mapping.length() - 1);
			}
			this.path = _mapping + ".ftl";
		} else {
			if (this.path.startsWith(this.getBaseViewPath())) {
				this.path = StringUtils.substringAfter(this.path, this.getBaseViewPath());
			}
			if (!this.path.endsWith(".ftl")) {
				this.path += ".ftl";
			}
		}
		//
		return _freemarkerCfg;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#renderView()
	 */
	protected void renderView() throws Exception {
		Template t = this.processPath().getTemplate(this.path, WebContext.getContext().getLocale());
		t.process(getAttributes(), WebContext.getResponse().getWriter());
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.AbstractWebView#render(java.io.OutputStream)
	 */
	public void render(OutputStream output) throws Exception {
		Template t = this.processPath().getTemplate(this.path, WebContext.getContext().getLocale());
		t.process(getAttributes(), new BufferedWriter(new OutputStreamWriter(output)));
	}

}
