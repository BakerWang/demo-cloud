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
package com.loy.cloud.authorize;

import java.util.Map;
import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.loy.e.common.vo.DefaultRespone;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

@FeignClient(name = "upm"
//,fallback = HystrixClientFallback.class
//,url="http://localhost:18080"
)
public interface ClientAuthorityService {
    @RequestMapping(value = "/authority/resource", method = RequestMethod.GET)
    DefaultRespone<Map<String, String>> getPermissionResource();

    @RequestMapping(value = "/authority/roles", method = RequestMethod.GET)
    DefaultRespone<Set<String>> getResourceRole(@RequestParam("resourceId") String resourceId);
    /*
     * @Component static class HystrixClientFallback implements
     * ClientAuthorityService {
     * 
     * @Override public DefaultRespone<Map<String, String>>
     * getPermissionResource() { return null; }
     * 
     * @Override public DefaultRespone<Set<String>> getResourceRole(String
     * resourceId) { return null; } }
     */
}
