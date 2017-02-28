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

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

public class NoticeProperties {

    Boolean showCount = false;
    String noticeCountUrl = null;

    Boolean showNoticeList = false;
    String noticeListUrl = null;

    Boolean showDetail = false;
    String showDetailUrl = null;

    String moreNoticeUrl = null;

    Integer maxCount = 5;

    public Boolean getShowCount() {
        return showCount;
    }

    public void setShowCount(Boolean showCount) {
        this.showCount = showCount;
    }

    public String getNoticeCountUrl() {
        return noticeCountUrl;
    }

    public void setNoticeCountUrl(String noticeCountUrl) {
        this.noticeCountUrl = noticeCountUrl;
    }

    public Boolean getShowNoticeList() {
        return showNoticeList;
    }

    public void setShowNoticeList(Boolean showNoticeList) {
        this.showNoticeList = showNoticeList;
    }

    public String getNoticeListUrl() {
        return noticeListUrl;
    }

    public void setNoticeListUrl(String noticeListUrl) {
        this.noticeListUrl = noticeListUrl;
    }

    public Boolean getShowDetail() {
        return showDetail;
    }

    public void setShowDetail(Boolean showDetail) {
        this.showDetail = showDetail;
    }

    public String getShowDetailUrl() {
        return showDetailUrl;
    }

    public void setShowDetailUrl(String showDetailUrl) {
        this.showDetailUrl = showDetailUrl;
    }

    public String getMoreNoticeUrl() {
        return moreNoticeUrl;
    }

    public void setMoreNoticeUrl(String moreNoticeUrl) {
        this.moreNoticeUrl = moreNoticeUrl;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

}
