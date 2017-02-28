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
package com.loy.e.core.query.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class LoyPageRequest implements Pageable {
    Pageable pageable = null;

    public LoyPageRequest(Pageable pageable) {
        this.pageable = pageable;
    }

    public int getPageNumber() {
        return pageable.getPageNumber();
    }

    public int getPageSize() {
        return pageable.getPageSize();
    }

    public int getOffset() {
        int offset = pageable.getOffset() - pageable.getPageSize();
        if (offset < 0) {
            offset = 0;
        }
        return offset;
    }

    public Sort getSort() {
        return pageable.getSort();
    }

    public Pageable next() {
        return pageable.next();
    }

    public Pageable previousOrFirst() {
        return pageable.previousOrFirst();
    }

    public Pageable first() {
        return pageable.first();
    }

    public boolean hasPrevious() {
        return pageable.hasPrevious();
    }

}
