/*
 * Copyright   Loy Fu. 浠樺帤淇�
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

import java.io.Serializable;
import java.util.List;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq缇� 540553957")
public class TreeNode<T> implements Tree<T>, Serializable {

    private static final long serialVersionUID = -1653478000503676779L;
    private String id;
    private String text;
    private String parentId;
    private T data;
    private String type;
    boolean selected = false;

    List<TreeNode<T>> children;

    public List<TreeNode<T>> getChildren() {
        return this.children;
    }

    public T getData() {
        return data;
    }

    public String getParentId() {
        return this.parentId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
