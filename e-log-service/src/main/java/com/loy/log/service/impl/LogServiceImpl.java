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
package com.loy.log.service.impl;

import org.apache.catalina.connector.ResponseFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.loy.e.core.api.LogService;
import com.loy.e.core.api.vo.ExceptionRecord;
import com.loy.e.core.api.vo.ExeTimeLogRecord;
import com.loy.e.core.api.vo.LogRecord;
import com.loy.e.core.util.JsonUtil;
import com.loy.log.domain.entity.ExceptionEntity;
import com.loy.log.domain.entity.OperatorLogEntity;
import com.loy.log.domain.entity.PerformanceEntity;
import com.loy.log.repository.ExceptionRepository;
import com.loy.log.repository.OperatorLogRepository;
import com.loy.log.repository.PerformanceRepository;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

@RestController
@RequestMapping(value = "**/log", method = { RequestMethod.POST, RequestMethod.GET })
@Transactional
public class LogServiceImpl implements LogService {
    protected final Log logger = LogFactory.getLog(LogServiceImpl.class);
    @Autowired
    PerformanceRepository performanceRepository;
    @Autowired
    ExceptionRepository exceptionRepository;
    @Autowired
    OperatorLogRepository operatorLogRepository;

    /**
     * 记录操作日志
     */
    @RequestMapping(value = "/opt")
    public void log(@RequestBody LogRecord logRecord) {

        OperatorLogEntity operatorLogEntity = new OperatorLogEntity();
        operatorLogEntity.setUserId(logRecord.getUserId());
        operatorLogEntity.setOperator(logRecord.getName());
        operatorLogEntity.setOpName(logRecord.getOpName());
        operatorLogEntity.setSystemCode(logRecord.getSystemCode());
        Object[] args = logRecord.getArgs();
        if (args != null) {
            Object[] argss = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];

                if (arg instanceof MultipartFile ||
                        arg instanceof org.apache.catalina.connector.Response ||
                        arg instanceof ResponseFacade) {
                } else {
                    argss[i] = arg;
                }
            }
            String data = JsonUtil.json(argss);
            operatorLogEntity.setData(data);
        }

        logger.debug("操作日志：" + logRecord.getOpName() + " " + operatorLogEntity.getData());
        operatorLogRepository.save(operatorLogEntity);

    }

    /**
     * 主要记录一个业务方法用的时间
     */
    @RequestMapping(value = "/record")
    public void record(@RequestBody ExeTimeLogRecord exeTimeLogRecord) {

        PerformanceEntity performanceEntity = new PerformanceEntity();
        performanceEntity.setUrl(exeTimeLogRecord.getUrl());
        performanceEntity.setUseTime(exeTimeLogRecord.getExeTime());
        performanceEntity.setMethod(exeTimeLogRecord.getMethod());
        performanceEntity.setOpName(exeTimeLogRecord.getDescription());
        performanceEntity.setSystemCode(exeTimeLogRecord.getCode());
        performanceRepository.save(performanceEntity);

    }

    /**
     * 主要记录异常信息
     */
    @RequestMapping(value = "/exception")
    public void exception(@RequestBody ExceptionRecord exceptionRecord) {
        ExceptionEntity exceptionEntity = new ExceptionEntity();
        exceptionEntity.setExceptionName(exceptionRecord.getExceptionName());
        exceptionEntity.setStackTraceMsg(exceptionRecord.getStackTraceMsg());
        exceptionEntity.setSystemCode(exceptionRecord.getSystemCode());
        exceptionRepository.save(exceptionEntity);
    }

}
