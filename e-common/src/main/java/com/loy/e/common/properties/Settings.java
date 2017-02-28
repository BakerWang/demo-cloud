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
package com.loy.e.common.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

public class Settings {

    public String defaultLocale;
    public List<String> supportLocales = new ArrayList<String>();
    public List<String> ignoreJsonResponseUrls = new ArrayList<String>();

    public Map<String, List<String>> staticMappings = new LinkedHashMap<String, List<String>>();

    public Map<String, Object> homePageConfig = new HashMap<String, Object>();

    public Boolean debugPageResult = false;
    public Boolean recordOperateLog = true;
    public String defaultPage = "blank.html";
    public String homePage = "home.html";

    public Boolean loyAspect = true;

    public String defaultPassword;

    public List<String> ignoreResources = new ArrayList<String>();

    public List<String> sqlErrorCodes = new ArrayList<String>();

    public String version;

    public List<String> getSupportLocales() {
        return this.supportLocales;
    }

    public void setSupportLocales(List<String> supportLocales) {
        this.supportLocales = supportLocales;
    }

    public Boolean getDebugPageResult() {
        return debugPageResult;
    }

    public void setDebugPageResult(Boolean debugPageResult) {
        this.debugPageResult = debugPageResult;
    }

    public Boolean getRecordOperateLog() {
        return recordOperateLog;
    }

    public void setRecordOperateLog(Boolean recordOperateLog) {
        this.recordOperateLog = recordOperateLog;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public Map<String, List<String>> getStaticMappings() {
        return staticMappings;
    }

    public void setStaticMappings(Map<String, List<String>> staticMappings) {
        this.staticMappings = staticMappings;
    }

    public String getDefaultPage() {
        return defaultPage;
    }

    public void setDefaultPage(String defaultPage) {
        this.defaultPage = defaultPage;
    }

    public List<String> getIgnoreJsonResponseUrls() {
        return ignoreJsonResponseUrls;
    }

    public void setIgnoreJsonResponseUrls(List<String> ignoreJsonResponseUrls) {
        this.ignoreJsonResponseUrls = ignoreJsonResponseUrls;
    }

    public Boolean getLoyAspect() {
        return loyAspect;
    }

    public void setLoyAspect(Boolean loyAspect) {
        this.loyAspect = loyAspect;
    }

    public List<String> getIgnoreResources() {
        return ignoreResources;
    }

    public void setIgnoreResources(List<String> ignoreResources) {
        this.ignoreResources = ignoreResources;
    }

    public List<String> getSqlErrorCodes() {
        return sqlErrorCodes;
    }

    public void setSqlErrorCodes(List<String> sqlErrorCodes) {
        this.sqlErrorCodes = sqlErrorCodes;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public Map<String, Object> getHomePageConfig() {
        return homePageConfig;
    }

    public void setHomePageConfig(Map<String, Object> homePageConfig) {
        this.homePageConfig = homePageConfig;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
