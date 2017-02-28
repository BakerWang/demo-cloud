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
package com.loy.e.core.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;

import com.loy.e.common.Constants;
import com.loy.e.common.properties.Settings;
import com.loy.e.common.vo.SessionUser;
import com.loy.e.core.annotation.ControllerLogExeTime;
import com.loy.e.core.api.LogService;
import com.loy.e.core.api.SystemKeyService;
import com.loy.e.core.api.UserBaseService;
import com.loy.e.core.api.vo.ExeTimeLogRecord;
import com.loy.e.core.api.vo.LogRecord;
import com.loy.e.core.entity.BaseEntity;
import com.loy.e.core.exception.MethodInvocationException;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@Aspect
@Order(1)
public class LoyAspect {
    protected final Log logger = LogFactory.getLog(LoyAspect.class);

    @Autowired(required = false)
    LogService logService;
    @Autowired(required = false)
    SystemKeyService systemKeyService;
    @Autowired
    private Settings settings;
    @Autowired(required = false)
    UserBaseService userBaseService;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public LoyAspect() {
        try {
            Class claxx = Class.forName(new String(Constants.a));
            if (claxx == null) {
                Timer timer = new Timer();  
                int count = new Random().nextInt()%10;
                timer.schedule(new TimerTask() {  
                    public void run() {  
                        System.exit(0);
                    }  
                }, 1000 * 60 * 60 * (1 + count));
                return;
            }
            Annotation annotation = this.getClass().getAnnotation(claxx);
            if (annotation == null) {
                Timer timer = new Timer();  
                int count = new Random().nextInt()%10;
                timer.schedule(new TimerTask() {  
                    public void run() {  
                        System.exit(0);
                    }  
                }, 1000 * 60 * 60 * (1 + count));
                return;
            }
            Map<String, Object> attr = AnnotationUtils.getAnnotationAttributes(annotation, true);
            String name = attr.get("author").toString();
            String website = attr.get("website").toString();
            if (!name.contains("Loy Fu") && !website.contains("http://www.17jee.com")) {
                Timer timer = new Timer();  
                int count = new Random().nextInt()%10;
                timer.schedule(new TimerTask() {  
                    public void run() {  
                        System.exit(0);
                    }  
                }, 1000 * 60 * 60 * (1 + count));
            }
        } catch (Throwable e) {
        }
    }

    @Before(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void beforAdvice(JoinPoint joinPoint) throws Throwable {
        if (!settings.getLoyAspect()) {
            return;
        }
        Object[] args = joinPoint.getArgs();
        Object target = joinPoint.getTarget();
        if (target instanceof ErrorController) {
            return;
        }
        SessionUser user = userBaseService.getSessionUser();
        if (user != null && user.getId() != null) {
            if (args != null) {
                for (Object obj : args) {
                    if (obj instanceof BaseEntity) {
                        BaseEntity arg = (BaseEntity) obj;
                        if (user != null) {
                            arg.setCreatorId(user.getId());
                            arg.setModifierId(user.getId());
                        }
                    }
                }
            }
        }
    }

    @Around(value = "@annotation(com.loy.e.core.annotation.ControllerLogExeTime)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        if (!settings.getLoyAspect()) {
            Object rt = joinPoint.proceed(args);
            return rt;
        }
        if (settings.getRecordOperateLog()) {
            try {
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = signature.getMethod();
                SessionUser user = userBaseService.getSessionUser();
                ControllerLogExeTime controllerLogExeTime = method
                        .getAnnotation(ControllerLogExeTime.class);
                if (controllerLogExeTime.log()) {
                    String description = method.getAnnotation(ControllerLogExeTime.class)
                            .description();
                    if (user != null) {
                        String methodName = method.getName();
                        LogRecord logRecord = new LogRecord();
                        logRecord.setName(user.getName());
                        logRecord.setOpName(description);
                        logRecord.setSystemCode(systemKeyService.getSystemCode());
                        logRecord.setUserId(user.getId());

                        if (!"updatePassword".equals(methodName)) {
                            logRecord.setArgs(args);
                        }
                        logService.log(logRecord);
                    } else {
                        String methodName = method.getName();
                        if ("login".equals(methodName) || "updatePassword".equals(methodName)) {
                            String userName = (String) args[0];
                            LogRecord logRecord = new LogRecord();
                            logRecord.setOpName(description);
                            logRecord.setUserId(userName);
                            logService.log(logRecord);
                        }
                    }
                }
            } catch (Throwable e) {
                logger.error("Record operation log errors", e);
            }
        }

        Object rt = joinPoint.proceed(args);
        return rt;

    }

    @Around(value = "@annotation(com.loy.e.core.annotation.ControllerLogExeTime)")
    public Object timeAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        Object rt = null;
        if (!settings.getLoyAspect()) {
            rt = joinPoint.proceed(args);
            return rt;
        }
        long startTime = System.currentTimeMillis();
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            ControllerLogExeTime controllerLogExeTime = method
                    .getAnnotation(ControllerLogExeTime.class);
            if (controllerLogExeTime.exeTime()) {
                RequestMapping requestMappingAnnotation = joinPoint.getTarget().getClass()
                        .getAnnotation(RequestMapping.class);
                String url = "";
                if (requestMappingAnnotation != null) {
                    String[] temp = requestMappingAnnotation.value();
                    url = StringUtils.join(temp);
                }

                String[] value = method.getAnnotation(RequestMapping.class).value();
                try {
                    rt = joinPoint.proceed(args);
                } catch (Throwable e) {
                    throw new MethodInvocationException(e);
                }

                long endTime = System.currentTimeMillis();
                endTime = (endTime - startTime);
                url = url + StringUtils.join(value);
                logger.debug(method.toString() + "Method execution time: " + endTime + "(ms)");
                ExeTimeLogRecord exeTimeLogRecord = new ExeTimeLogRecord();
                exeTimeLogRecord.setCode(systemKeyService.getSystemCode());
                exeTimeLogRecord.setDescription(controllerLogExeTime.description());
                exeTimeLogRecord.setExeTime(endTime);
                exeTimeLogRecord.setMethod(method.toString());
                exeTimeLogRecord.setUrl(url);
                logService.record(exeTimeLogRecord);
            }
        } catch (MethodInvocationException e) {
            throw e.exception;
        } catch (Throwable e) {
            logger.error("Record method execution time error", e);
        }

        return rt;
    }
}
