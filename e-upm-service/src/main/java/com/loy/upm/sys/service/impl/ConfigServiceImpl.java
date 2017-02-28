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
package com.loy.upm.sys.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.loy.e.common.properties.Settings;
import com.loy.e.core.util.JsonUtil;

import com.loy.e.common.annotation.Author;
@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@RestController
@RequestMapping(value = "/", method = { RequestMethod.GET })
public class ConfigServiceImpl {

    public static final String ACTIVE_DEV = "dev";
    public static final String ACTIVE_TEST = "test";
    public static final String ACTIVE_PROD = "prod";

    static String[] PROD_FILES = {
            "component/assets/e-all-min-ie.js",
            "component/assets/custom/validator_zh_CN.js",
            "component/assets/js/jqGrid/i18n/grid.locale-zh_CN.js",
            "component/assets/js/date-time/locales/bootstrap-datepicker.zh_CN.js",
            "component/assets/custom/jquery.loyGrid.js",
            "component/assets/custom/home.js" };

    static String[] DEV_FILES = {

            "component/assets/js/jquery-ie.js",
            "component/assets/js/ace-extra.js",
            "component/assets/js/fastclick.js",
            "component/assets/js/bootstrap.js",
            "component/assets/js/bootbox.js",

            "component/assets/js/jquery.placeholder.js",
            "component/assets/custom/loy.js",
            "component/assets/js/ace/ace.js",
            "component/assets/js/ace/ace.ajax-content.js",
            "component/assets/js/date-time/bootstrap-datepicker.js",
            "component/assets/js/jquery.gritter.js",
            "component/assets/js/jqGrid/jquery.jqGrid.js",
            "component/assets/custom/cust_chosen.jquery.js",
            "component/assets/js/chosen.jquery.js",
            "component/assets/js/ace/elements.fileinput.js",
            "component/assets/js/x-editable/bootstrap-editable.js",
            "component/assets/js/x-editable/ace-editable.js",

            "component/assets/js/ace/elements.scroller.js",
            "component/assets/js/ace/elements.colorpicker.js",
            "component/assets/js/ace/elements.typeahead.js",
            "component/assets/js/ace/elements.spinner.js",
            "component/assets/js/ace/elements.treeview.js",
            "component/assets/js/ace/elements.wizard.js",
            "component/assets/js/ace/elements.aside.js",

            "component/assets/js/ace/ace.touch-drag.js",
            "component/assets/js/ace/ace.sidebar.js",
            "component/assets/js/ace/ace.sidebar-scroll-1.js",
            "component/assets/js/ace/ace.submenu-hover.js",
            "component/assets/js/ace/ace.widget-box.js",
            "component/assets/js/ace/ace.settings.js",
            "component/assets/js/ace/ace.settings-rtl.js",
            "component/assets/js/ace/ace.settings-skin.js",
            "component/assets/js/ace/ace.widget-on-reload.js",
            "component/assets/js/ace/ace.searchbox-autocomplete.js",
            "component/assets/js/fuelux/fuelux.tree.js",
            "component/assets/js/fuelux/fuelux.spinner.js",
            "component/assets/js/jquery.validate.js",

            "component/assets/js/jquery.i18n.properties.js",
            "component/assets/js/jquery.ztree.core-3.5.js",
            "component/assets/js/jquery.ztree.excheck-3.5.js",
            "component/assets/js/json2.js",
            "component/assets/js/jquery.cookie.js",
            "component/assets/custom/validator_zh_CN.js",
            "component/assets/js/jqGrid/i18n/grid.locale-zh_CN.js",
            "component/assets/js/date-time/locales/bootstrap-datepicker.zh_CN.js",
            "component/assets/custom/jquery.loyGrid.js",
            "component/assets/custom/home.js"
    };

    @Autowired
    Settings settings;

    @Value(value = "${spring.profiles.active}")
    private String active = "dev";

    @RequestMapping(value = "**/config.js")
    public void config(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.addHeader("Content-type", "application/javascript");
        Map<String, Object> homePageConfig = settings.getHomePageConfig();
        PrintWriter out = response.getWriter();
        out.println("Config = ");

        if (homePageConfig == null) {
            homePageConfig = new LinkedHashMap<String, Object>();
        }

        String version = settings.getVersion();
        if (StringUtils.isNotEmpty(version)) {
            homePageConfig.put("VERSION", version);
        }
        homePageConfig.put("PROFILES_ACTIVE", active);

        Object jsCssFile = homePageConfig.get("jsCssFile");
        if (ACTIVE_DEV.equals(active)) {
            if (jsCssFile == null) {
                homePageConfig.put("jsCssFile", DEV_FILES);
            }
        } else {
            if (jsCssFile == null) {
                homePageConfig.put("jsCssFile", PROD_FILES);
            }
        }
        out.println(JsonUtil.json(homePageConfig));
        out.flush();
        out.close();
    }
}
