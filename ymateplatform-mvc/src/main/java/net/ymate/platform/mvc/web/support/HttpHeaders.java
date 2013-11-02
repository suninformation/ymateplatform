/*
 * Copyright 2007-2107 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.platform.mvc.web.support;

import java.util.TimeZone;

/**
 * <p>
 * HttpHeaders
 * </p>
 * <p>
 * 
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
 *          <td>2012-12-20下午7:59:42</td>
 *          </tr>
 *          </table>
 */
public class HttpHeaders {

	public static final String ACCEPT = "Accept";

	public static final String ACCEPT_CHARSET = "Accept-Charset";

	public static final String ALLOW = "Allow";

	public static final String CACHE_CONTROL = "Cache-Control";

	public static final String CONTENT_DISPOSITION = "Content-Disposition";

	public static final String CONTENT_LENGTH = "Content-Length";

	public static final String CONTENT_TYPE = "Content-Type";

	public static final String DATE = "Date";

	public static final String ETAG = "ETag";

	public static final String EXPIRES = "Expires";

	public static final String IF_MODIFIED_SINCE = "If-Modified-Since";

	public static final String IF_NONE_MATCH = "If-None-Match";

	public static final String LAST_MODIFIED = "Last-Modified";

	public static final String LOCATION = "Location";

	public static final String PRAGMA = "Pragma";

	public static final String USER_AGENT = "User-Agent";

	public static final String[] DATE_FORMATS = new String[] {
		"EEE, dd MMM yyyy HH:mm:ss zzz",
        "EEE, dd-MMM-yy HH:mm:ss zzz",
        "EEE MMM dd HH:mm:ss yyyy"
	};

	public static TimeZone GMT = TimeZone.getTimeZone("GMT");

}
