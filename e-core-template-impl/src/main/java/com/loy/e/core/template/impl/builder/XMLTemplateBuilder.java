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
package com.loy.e.core.template.impl.builder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.loy.e.core.template.Template;
import com.loy.e.core.template.impl.Configuration;
import com.loy.e.core.template.impl.DefaultTemplate;
import com.loy.e.core.template.impl.scripting.xmltags.ChooseNode;
import com.loy.e.core.template.impl.scripting.xmltags.ForEachNode;
import com.loy.e.core.template.impl.scripting.xmltags.IfNode;
import com.loy.e.core.template.impl.scripting.xmltags.MixedNode;
import com.loy.e.core.template.impl.scripting.xmltags.NotEmptyNode;
import com.loy.e.core.template.impl.scripting.xmltags.SetNode;
import com.loy.e.core.template.impl.scripting.xmltags.SqlNode;
import com.loy.e.core.template.impl.scripting.xmltags.TextNode;
import com.loy.e.core.template.impl.scripting.xmltags.TrimNode;

import com.loy.e.common.annotation.Author;

@Author(author = "Loy Fu", website = "http://www.17jee.com", contact = "qq群 540553957")
public class XMLTemplateBuilder {
    protected final Configuration configuration;

    private static final Map<String, TagHandler> nodeHandlers = new HashMap<String, TagHandler>();

    static {
        nodeHandlers.put("notEmpty", new NotEmptyHandler());
        nodeHandlers.put("trim", new TrimHandler());
        nodeHandlers.put("set", new SetHandler());
        nodeHandlers.put("foreach", new ForEachHandler());
        nodeHandlers.put("if", new IfHandler());
        nodeHandlers.put("choose", new ChooseHandler());
        nodeHandlers.put("when", new IfHandler());
        nodeHandlers.put("otherwise", new OtherwiseHandler());

    }

    public XMLTemplateBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    private interface TagHandler {
        void handleNode(Node nodeToHandle, List<SqlNode> targetContents);
    }

    public Template build(String content) {
        Document document = null;
        try {
            document = buildXml(content);
        } catch (Exception e) {
            throw new RuntimeException("Error constructing the XML object");
        }
        List<SqlNode> contents = buildDynamicTag(document.getElementsByTagName("script").item(0));
        return new DefaultTemplate(configuration, new MixedNode(contents));
    }

    private Document buildXml(String templateContent)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(false);
        factory.setCoalescing(false);
        factory.setExpandEntityReferences(true);

        DocumentBuilder builder = factory.newDocumentBuilder();

        builder.setErrorHandler(new ErrorHandler() {
            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }

            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }

            public void warning(SAXParseException exception) throws SAXException {
            }
        });
        InputSource inputSource = new InputSource(new StringReader(String.format(
                "<?xml version = \"1.0\" ?>\r\n<!DOCTYPE script>\r\n<script>%s</script>",
                templateContent)));
        return builder.parse(inputSource);
    }

    private static List<SqlNode> buildDynamicTag(Node node) {

        List<SqlNode> contents = new ArrayList<SqlNode>();
        NodeList children = node.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            String nodeName = child.getNodeName();

            if (child.getNodeType() == Node.CDATA_SECTION_NODE
                    || child.getNodeType() == Node.TEXT_NODE) {

                String content = child.getTextContent();

                TextNode textNode = new TextNode(content);

                contents.add(textNode);

            } else if (child.getNodeType() == Node.ELEMENT_NODE) {

                TagHandler tagHandler = nodeHandlers.get(nodeName);

                if (tagHandler != null) {
                    tagHandler.handleNode(child, contents);
                }

            }
        }

        return contents;
    }

    private static class TrimHandler implements TagHandler {
        public void handleNode(Node nodeToHandle,
                List<SqlNode> targetContents) {
            List<SqlNode> contents = buildDynamicTag(nodeToHandle);
            MixedNode mixedNode = new MixedNode(contents);

            NamedNodeMap attributes = nodeToHandle.getAttributes();
            Node prefixAtt = attributes
                    .getNamedItem("prefix");

            String prefix = prefixAtt == null ? null : prefixAtt.getTextContent();

            Node prefixOverridesAtt = attributes
                    .getNamedItem("prefixOverrides");

            String prefixOverrides = prefixOverridesAtt.getTextContent();

            Node suffixAtt = attributes
                    .getNamedItem("suffix");

            String suffix = suffixAtt == null ? null : suffixAtt.getTextContent();

            Node suffixOverridesAtt = attributes
                    .getNamedItem("suffixOverrides");

            String suffixOverrides = suffixOverridesAtt == null ? null
                    : suffixOverridesAtt.getTextContent();
            TrimNode trim = new TrimNode(mixedNode, prefix,
                    suffix, prefixOverrides, suffixOverrides);
            targetContents.add(trim);
        }
    }

    private static class SetHandler implements TagHandler {
        public void handleNode(Node nodeToHandle,
                List<SqlNode> targetContents) {
            List<SqlNode> contents = buildDynamicTag(nodeToHandle);
            MixedNode mixedNode = new MixedNode(contents);
            SqlNode set = new SetNode(mixedNode);
            targetContents.add(set);
        }
    }

    private static class ForEachHandler implements TagHandler {
        public void handleNode(Node nodeToHandle,
                List<SqlNode> targetContents) {
            List<SqlNode> contents = buildDynamicTag(nodeToHandle);
            MixedNode mixedNode = new MixedNode(
                    contents);
            NamedNodeMap attributes = nodeToHandle.getAttributes();
            Node collectionAtt = attributes.getNamedItem("collection");

            if (collectionAtt == null) {
                throw new RuntimeException(
                        nodeToHandle.getNodeName() + " must has a collection attribute !");
            }

            String collection = collectionAtt.getTextContent();

            Node itemAtt = attributes.getNamedItem("item");

            String item = itemAtt == null ? "item" : itemAtt.getTextContent();

            Node indexAtt = attributes.getNamedItem("index");

            String index = indexAtt == null ? "index" : indexAtt.getTextContent();

            Node openAtt = attributes.getNamedItem("open");

            String open = openAtt == null ? null : openAtt.getTextContent();

            Node closeAtt = attributes.getNamedItem("close");

            String close = closeAtt == null ? null : closeAtt.getTextContent();

            Node sparatorAtt = attributes.getNamedItem("separator");

            String separator = sparatorAtt == null ? null : sparatorAtt.getTextContent();

            ForEachNode forEachNode = new ForEachNode(
                    mixedNode, collection, index, item, open, close,
                    separator);
            targetContents.add(forEachNode);
        }
    }

    private static class IfHandler implements TagHandler {
        public void handleNode(Node nodeToHandle,
                List<SqlNode> targetContents) {
            List<SqlNode> contents = buildDynamicTag(nodeToHandle);
            MixedNode mixedNode = new MixedNode(
                    contents);

            NamedNodeMap attributes = nodeToHandle.getAttributes();

            Node testAtt = attributes.getNamedItem("test");

            if (testAtt == null) {
                throw new RuntimeException(
                        nodeToHandle.getNodeName() + " must has test attribute ! ");
            }

            String test = testAtt.getTextContent();

            IfNode ifNode = new IfNode(mixedNode,
                    test);
            targetContents.add(ifNode);
        }
    }

    private static class NotEmptyHandler implements TagHandler {
        public void handleNode(Node nodeToHandle,
                List<SqlNode> targetContents) {
            List<SqlNode> contents = buildDynamicTag(nodeToHandle);
            MixedNode mixedNode = new MixedNode(
                    contents);

            NamedNodeMap attributes = nodeToHandle.getAttributes();

            Node nameAtt = attributes.getNamedItem("name");

            if (nameAtt == null) {
                throw new RuntimeException(
                        nodeToHandle.getNodeName() + " must has name attribute ! ");
            }

            String value = nameAtt.getTextContent();

            NotEmptyNode notEmptyNode = new NotEmptyNode(mixedNode, value);
            targetContents.add(notEmptyNode);
        }
    }

    private static class OtherwiseHandler implements TagHandler {
        public void handleNode(Node nodeToHandle,
                List<SqlNode> targetContents) {
            List<SqlNode> contents = buildDynamicTag(nodeToHandle);
            MixedNode mixedNode = new MixedNode(
                    contents);
            targetContents.add(mixedNode);
        }
    }

    private static class ChooseHandler implements TagHandler {
        public void handleNode(Node nodeToHandle,
                List<SqlNode> targetContents) {
            List<SqlNode> whenNode = new ArrayList<SqlNode>();
            List<SqlNode> otherwiseNode = new ArrayList<SqlNode>();
            handleWhenOtherwiseNodes(nodeToHandle, whenNode,
                    otherwiseNode);
            SqlNode defaultNode = getDefaultNode(otherwiseNode);
            ChooseNode chooseNode = new ChooseNode(
                    whenNode, defaultNode);
            targetContents.add(chooseNode);
        }

        private void handleWhenOtherwiseNodes(Node chooseNode,
                List<SqlNode> ifNode,
                List<SqlNode> defaultNodes) {
            NodeList children = chooseNode.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    String nodeName = child.getNodeName();
                    TagHandler handler = nodeHandlers.get(nodeName);
                    if (handler instanceof IfHandler) {
                        handler.handleNode(child, ifNode);
                    } else if (handler instanceof OtherwiseHandler) {
                        handler.handleNode(child, defaultNodes);
                    }
                }
            }

        }

        private SqlNode getDefaultNode(
                List<SqlNode> defaultNodes) {
            SqlNode defaultNode = null;
            if (defaultNodes.size() == 1) {
                defaultNode = defaultNodes.get(0);
            } else if (defaultNodes.size() > 1) {
                throw new RuntimeException(
                        "Too many default (otherwise) elements in choose statement.");
            }
            return defaultNode;
        }
    }

}
