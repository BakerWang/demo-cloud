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
package com.loy.e.core.template.impl.scripting.xmltags;

import java.util.List;

import com.loy.e.core.template.impl.Context;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class ChooseNode implements SqlNode {

    private SqlNode node;
    private List<SqlNode> ifNodes;

    public ChooseNode(List<SqlNode> ifNodes,
            SqlNode node) {
        this.ifNodes = ifNodes;
        this.node = node;
    }

    public boolean apply(Context context) {
        for (SqlNode sqlNode : this.ifNodes) {
            if (sqlNode.apply(context)) {
                return true;
            }
        }
        if (node != null) {
            node.apply(context);
            return true;
        }
        return false;
    }

}
