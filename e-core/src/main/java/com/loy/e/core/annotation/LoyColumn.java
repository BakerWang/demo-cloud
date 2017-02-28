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
package com.loy.e.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.loy.e.core.data.InputClazz;
import com.loy.e.core.query.annotation.LoyField;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Documented
public @interface LoyColumn {
    boolean list() default true;

    boolean edit() default true;

    boolean detail() default true;

    String description();

    String column() default "";

    boolean sortable() default false;

    InputClazz inputType() default InputClazz.NONE;

    String validate() default "";//这里的值必需是一个json串;{required:true, minlength: 5}

    LoyField[] lists() default {};

    LoyField[] details() default {};
}
