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
package com.loy.e.common.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class TreeUtil {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<TreeNode> build(List<? extends Tree> list) {

        if (list != null) {
            int len = list.size();
            if (len > 0) {
                List<TreeNode> mainList = new ArrayList<TreeNode>();
                Map<String, TreeNode> map = new HashMap<String, TreeNode>();
                for (Tree t : list) {
                    String id = t.getId();
                    String parentId = t.getParentId();
                    Object data = t.getData();
                    TreeNode treeNode = new TreeNode();
                    treeNode.setData(data);
                    treeNode.setId(id);
                    treeNode.setParentId(parentId);
                    if (parentId == null || "".equals(parentId)) {
                        mainList.add(treeNode);
                    }
                    map.put(id, treeNode);
                }
                Collection<TreeNode> values = map.values();
                for (TreeNode t : values) {
                    String parentId = t.getParentId();
                    if (parentId != null && !"".equals(parentId)) {
                        TreeNode parent = map.get(parentId);
                        List<TreeNode> children = parent.getChildren();
                        if (children == null) {
                            children = new ArrayList<TreeNode>();
                            parent.setChildren(children);
                        }
                        children.add(t);
                    }
                }
                return mainList;
            }
        }
        return null;
    }
}
