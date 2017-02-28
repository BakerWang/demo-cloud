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
package com.xx.demo.hello.service.impl;

import com.loy.e.core.annotation.ControllerLogExeTime;
import com.loy.e.core.util.TableToExcelUtil;
import com.xx.demo.hello.domain.FirstHelloQueryParam;
import com.xx.demo.hello.domain.NotifyQueryParam;
import com.xx.demo.hello.domain.entity.FirstHelloEntity;
import com.xx.demo.hello.domain.entity.Notify;
import com.xx.demo.hello.repository.FirstHelloRepository;
import com.xx.demo.hello.repository.NotifyRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "**/notify", method = { RequestMethod.POST, RequestMethod.GET })
@Transactional
public class NotifyServiceImpl {
    protected final Log logger = LogFactory.getLog(NotifyServiceImpl.class);

    @Autowired
    NotifyRepository notifyRepository;

    @ControllerLogExeTime(description = "分页查询hello", log = false)
    @RequestMapping(value = "/page")
    public Page<Notify> queryPage(NotifyQueryParam notifyQueryParam,
                                  Pageable pageable) {
        Page<Notify> page = notifyRepository.notifyPage(notifyQueryParam, pageable);
        return page;
    }

}
