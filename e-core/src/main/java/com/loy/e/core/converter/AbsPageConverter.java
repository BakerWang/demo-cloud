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
package com.loy.e.core.converter;

import org.springframework.data.domain.PageImpl;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

public abstract class AbsPageConverter<S, T>
        implements Converter, org.springframework.core.convert.converter.Converter<S, T> {

    @Override
    public Object converter(Object source) {
        @SuppressWarnings("rawtypes")
        PageImpl p = (PageImpl) source;

        @SuppressWarnings("unchecked")
        Object page = p.map(this);
        return page;
    }

}
