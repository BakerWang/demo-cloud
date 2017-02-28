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
package com.loy.log.domain;

import java.io.Serializable;

import com.loy.e.core.query.data.QueryResult;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")

public class MonitorVO implements Serializable, QueryResult {

    private static final long serialVersionUID = -6359163324521983639L;

    private String method;
    private long useTime;
    private String opName;
    private long maxUseTime;
    private long minUseTime;
    private long count;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public long getMaxUseTime() {
        return maxUseTime;
    }

    public void setMaxUseTime(long maxUseTime) {
        this.maxUseTime = maxUseTime;
    }

    public long getMinUseTime() {
        return minUseTime;
    }

    public void setMinUseTime(long minUseTime) {
        this.minUseTime = minUseTime;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
