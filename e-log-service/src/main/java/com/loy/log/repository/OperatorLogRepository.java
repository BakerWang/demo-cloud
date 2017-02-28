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
package com.loy.log.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.loy.e.core.query.annotation.DynamicQuery;
import com.loy.e.core.repository.GenericRepository;
import com.loy.log.domain.LogQueryParam;
import com.loy.log.domain.entity.OperatorLogEntity;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public interface OperatorLogRepository extends GenericRepository<OperatorLogEntity, String> {

    @DynamicQuery
    Page<OperatorLogEntity> findLogPage(LogQueryParam logQueryParam, Pageable pageable);
}
