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
package com.loy.e.common.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loy.e.common.properties.NoticeProperties;
import com.loy.e.common.tree.TreeNode;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
@SuppressWarnings("rawtypes")
public class IndexData implements Serializable {
    private static final long serialVersionUID = 385770670924715475L;

    private String version;
    private SessionUser simpleUser;

    List<TreeNode> menuData;
    List<System> mySystems;
    private boolean photo;

    String defaultPage = "blank.html";

    private Map<String, Boolean> accessCodes = new HashMap<String, Boolean>();

    private List<LocaleVO> supportLocales = new ArrayList<LocaleVO>();

    NoticeProperties noticeProperties;

    public List<TreeNode> getMenuData() {
        return menuData;
    }

    public void setMenuData(List<TreeNode> menuData) {
        this.menuData = menuData;
    }

    public SessionUser getSimpleUser() {
        return simpleUser;
    }

    public void setSimipleUser(SessionUser simpleUser) {
        this.simpleUser = simpleUser;
    }

    public Map<String, Boolean> getAccessCodes() {
        return accessCodes;
    }

    public void setAccessCodes(Map<String, Boolean> accessCodes) {
        this.accessCodes = accessCodes;
    }

    public boolean isPhoto() {
        return photo;
    }

    public void setPhoto(boolean photo) {
        this.photo = photo;
    }

    public List<LocaleVO> getSupportLocales() {
        return supportLocales;
    }

    public void setSupportLocales(List<LocaleVO> supportLocales) {
        this.supportLocales = supportLocales;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<System> getMySystems() {
        return mySystems;
    }

    public void setMySystems(List<System> mySystems) {
        this.mySystems = mySystems;
    }

    public String getDefaultPage() {
        return defaultPage;
    }

    public void setDefaultPage(String defaultPage) {
        this.defaultPage = defaultPage;
    }

    public NoticeProperties getNoticeProperties() {
        return noticeProperties;
    }

    public void setNoticeProperties(NoticeProperties noticeProperties) {
        this.noticeProperties = noticeProperties;
    }

}
