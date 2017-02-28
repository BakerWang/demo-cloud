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
package com.loy.e.core.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import com.loy.e.core.entity.Entity;

import com.loy.e.common.annotation.Author;

@NoRepositoryBean
public interface GenericRepository<T extends Entity<ID>, ID extends Serializable>
        extends Repository<T, ID> {

    T save(T entity);

    T get(ID id);

    List<T> findAll();

    void delete(ID id);

    void delete(T entity);

    void delete(List<ID> ids);
}
