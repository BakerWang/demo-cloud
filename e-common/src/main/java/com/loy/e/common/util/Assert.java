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
package com.loy.e.common.util;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;

import com.loy.e.core.exception.LoyException;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class Assert {
    public final static String SYS_ERROR_CODE = "system_error";

    public static void throwException() {
        throw new LoyException(SYS_ERROR_CODE);

    }

    public static void throwException(String errorKey) {
        throw new LoyException(errorKey);

    }

    public static void throwException(boolean expression, String errorKey) {
        if (!expression) {
            throw new LoyException(errorKey);
        }
    }

    public static void throwException(boolean expression, String errorKey, Object... params) {
        if (!expression) {
            throw new LoyException(errorKey, params);
        }
    }

    public static void isTrue(boolean expression, String errorKey) {
        if (!expression) {
            throw new LoyException(errorKey);
        }
    }

    public static void isNull(Object object, String errorKey) {
        if (object != null) {
            throw new LoyException(errorKey);
        }
    }

    public static void notNull(Object object, String errorKey) {
        if (object == null) {
            throw new LoyException(errorKey);
        }
    }

    public static void notEmpty(Object[] array, String errorKey) {
        if (ArrayUtils.isEmpty(array)) {
            throw new LoyException(errorKey);
        }
    }

    public static void notEmpty(Collection<?> collection, String errorKey) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new LoyException(errorKey);
        }
    }

    public static void notEmpty(Map<?, ?> map, String errorKey) {
        if (MapUtils.isEmpty(map)) {
            throw new LoyException(errorKey);
        }
    }

}
