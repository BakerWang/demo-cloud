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

import com.loy.e.common.annotation.Author;
import com.loy.e.core.api.LogService;
import com.loy.e.core.api.vo.ExceptionRecord;
import com.loy.e.core.api.vo.ExeTimeLogRecord;
import com.loy.e.core.api.vo.LogRecord;
import com.loy.e.core.util.JsonUtil;
import com.loy.log.domain.LogQueryParam;
import com.loy.log.domain.MonitorVO;
import com.loy.log.domain.PerQueryParam;
import com.loy.log.domain.entity.ExceptionEntity;
import com.loy.log.domain.entity.OperatorLogEntity;
import com.loy.log.domain.entity.PerformanceEntity;
import com.loy.log.repository.ExceptionRepository;
import com.loy.log.repository.OperatorLogRepository;
import com.loy.log.repository.PerformanceRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "**/performance", method = { RequestMethod.POST, RequestMethod.GET })
@Transactional
public class PerformanceServiceImpl {
    protected final Log logger = LogFactory.getLog(PerformanceServiceImpl.class);
    @Autowired
    PerformanceRepository performanceRepository;

    @RequestMapping(value = "/page")
    @ApiOperation(value = "查方法执行情况", httpMethod = "GET", notes = "查方法执行情况")
    public Page<PerformanceEntity> queryPerformancePage(PerQueryParam perQueryParam, Pageable pageable) {
        Page<PerformanceEntity> page = performanceRepository.queryPage(perQueryParam,pageable);
        return page;

    }


}
