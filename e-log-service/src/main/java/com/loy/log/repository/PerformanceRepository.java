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

import com.loy.log.domain.LogQueryParam;
import com.loy.log.domain.PerQueryParam;
import com.loy.log.domain.entity.OperatorLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.loy.e.core.query.annotation.DynamicQuery;
import com.loy.e.core.repository.GenericRepository;
import com.loy.log.domain.MonitorVO;
import com.loy.log.domain.entity.PerformanceEntity;

public interface PerformanceRepository extends GenericRepository<PerformanceEntity, String> {

    @Query(value = " select x.method as method, "
            + "avg(x.use_time) as useTime,"
            + "x.op_name as opName,"
            + "max(use_time) as maxUseTime,"
            + "min(use_time) as minUseTime,"
            + "count(*) as count from e_performance x "
            + "group by x.method order by avg(x.use_time) desc", countQuery = "select count(*) from "
                    + "(select x.method  from e_performance x group by x.method) as tmp", nativeQuery = true)
    @DynamicQuery
    Page<MonitorVO> queryPerformancePage(Pageable pageable);

    @DynamicQuery
    Page<PerformanceEntity> queryPage(PerQueryParam perQueryParam, Pageable pageable);

}
