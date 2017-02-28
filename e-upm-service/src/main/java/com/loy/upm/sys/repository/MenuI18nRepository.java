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
package com.loy.upm.sys.repository;

//import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import com.loy.e.core.repository.GenericRepository;
import com.loy.upm.sys.domain.entity.MenuI18nEntity;

//@CacheConfig(cacheNames = "menuI18ns")
public interface MenuI18nRepository extends GenericRepository<MenuI18nEntity, String> {
//    @Cacheable
    public MenuI18nEntity findByKeyAndLang(String key, String lang);

}
