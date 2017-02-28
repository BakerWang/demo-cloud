/*
 * Copyright   Loy Fu. 付厚俊
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
package com.loy.e.core.advice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.loy.e.common.Constants;
import com.loy.e.common.properties.Settings;
import com.loy.e.common.util.Assert;
import com.loy.e.common.vo.ErrorResponseData;
import com.loy.e.core.api.LogService;
import com.loy.e.core.api.SystemKeyService;
import com.loy.e.core.api.vo.ExceptionRecord;
import com.loy.e.core.exception.LoyException;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@ControllerAdvice

public class ExceptionHandlerAdvice {
    protected final Log logger = LogFactory.getLog(ExceptionHandlerAdvice.class);
    @Autowired
    private MessageSource messageSource;
    @Autowired(required = false)
    private LogService logService;
    @Autowired(required = false)
    SystemKeyService systemKeyService;
    @Autowired
    Settings settings;

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(LoyException.class)
    @ResponseBody
    ErrorResponseData handleBadRequest(HttpServletRequest req, LoyException ex) {
        ErrorResponseData data = new ErrorResponseData();
        data.setErrorCode(ex.getErrorKey());
        String errorKey = ex.getErrorKey();
        String msg = messageSource.getMessage(errorKey, ex.getParams(),
                LocaleContextHolder.getLocale());
        data.setMsg(msg);
        return data;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ErrorResponseData handleBadRequest(HttpServletRequest req, Throwable ex) {
        String exceptionName = ex.getClass().getName();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTraceMsg = sw.toString();
        ExceptionRecord exceptionRecord = new ExceptionRecord();
        exceptionRecord.setExceptionName(exceptionName);
        exceptionRecord.setStackTraceMsg(stackTraceMsg);
        exceptionRecord.setSystemCode(systemKeyService.getSystemCode());
        try {
            logService.exception(exceptionRecord);
        } catch (Throwable e) {
            logger.error("", e);
        }
        logger.error("", ex);
        ErrorResponseData data = new ErrorResponseData();
        String msg = null;
        Throwable cause = ex.getCause();
        String i18nSqlkey = null;
        if (cause != null) {
            if (cause instanceof JDBCException) {
                JDBCException jdbcException = (JDBCException) cause;
                int errorCode = jdbcException.getErrorCode();
                String code = String.valueOf(errorCode);
                if (ArrayUtils.contains(Constants.SQL_ERROR, code)) {
                    i18nSqlkey = Constants.SQL_ERROR_I18N_PREX + code;
                } else {
                    if (settings != null) {
                        List<String> sqlErrorCodes = settings.getSqlErrorCodes();
                        if (sqlErrorCodes != null) {
                            if (sqlErrorCodes.contains(code)) {
                                i18nSqlkey = Constants.SQL_ERROR_I18N_PREX + code;
                            }
                        }
                    }
                }
            }
        }
        if (StringUtils.isEmpty(i18nSqlkey)) {
            i18nSqlkey = Assert.SYS_ERROR_CODE;
        }
        msg = messageSource.getMessage(i18nSqlkey, null,
                LocaleContextHolder.getLocale());
        data.setMsg(msg);
        return data;
    }
}
