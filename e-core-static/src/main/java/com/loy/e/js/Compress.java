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
package com.loy.e.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class Compress {

    static String[] files = {
            "component/assets/js/jquery.js",
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
            "component/assets/js/jquery.cookie.js"
            // "component/assets/custom/jquery.loyGrid.js"
    };

    public static void main(String[] args) throws IOException {
        compressMerge();
        compressMergeIE();
    }

    public static void jsCompress(File src, File dest) throws IOException {
        int linebreakpos = -1;
        boolean munge = true;
        boolean verbose = false;
        boolean preserveAllSemiColons = false;
        boolean disableOptimizations = false;
        Reader in = new FileReader(src);
        String fileName = src.getName();

        Writer out = new FileWriter(dest);
        if (src.getName().endsWith(".js")) {
            JavaScriptCompressor jscompressor = new JavaScriptCompressor(in, new ErrorReporter() {
                public void warning(String message,
                        String sourceName,
                        int line,
                        String lineSource,
                        int lineOffset) {
                    if (line < 0) {
                        System.err.println("\n[WARNING] " + message);
                    } else {
                        System.err
                                .println("\n[WARNING] " + line + ':' + lineOffset + ':' + message);
                    }
                }

                public void error(String message,
                        String sourceName,
                        int line,
                        String lineSource,
                        int lineOffset) {
                    if (line < 0) {
                        System.err.println("\n[ERROR] " + message);
                    } else {
                        System.err.println("\n[ERROR] " + line + ':' + lineOffset + ':' + message);
                    }
                }

                public EvaluatorException runtimeError(String message,
                        String sourceName,
                        int line,
                        String lineSource,
                        int lineOffset) {
                    error(message, sourceName, line, lineSource, lineOffset);
                    return new EvaluatorException(message);
                }
            });
            jscompressor.compress(out, linebreakpos, munge, verbose, preserveAllSemiColons,
                    disableOptimizations);
        } else if (fileName.endsWith(".css")) {
            CssCompressor csscompressor = new CssCompressor(in);
            csscompressor.compress(out, linebreakpos);
        }
        out.close();
        in.close();
    }

    public static void merge(List<File> list, File dest) throws IOException {

        if (!dest.exists()) {
            dest.createNewFile();
        }
        FileWriter writer;
        writer = new FileWriter(dest);

        for (File file : list) {
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {

                writer.write(lineTxt);
            }
            read.close();
        }
        writer.flush();
        writer.close();
    }


    public static void compressMerge() throws IOException {
        files[0] = "component/assets/js/jquery.js";
        String url = Compress.class.getResource("").getPath();
        url = url.replaceFirst("/", "");
        url = url.replaceFirst("bin/com/loy/e/js/", "src/main/resources/public/");
        File temp = new File(url+"/temp/");
        if(!temp.exists()){
            temp.mkdir();
        }
        
        String path = url;
        for (String f : files) {
            File ff = new File(path + f);
            jsCompress(ff, new File(url+"/temp/temp." + ff.getName()));
        }

        List<File> jsList = new ArrayList<File>();
        List<File> cssList = new ArrayList<File>();
        for (String f : files) {
            File ff = new File(path + f);
            if (ff.getName().endsWith(".js")) {
                jsList.add(new File(url+"/temp/temp." + ff.getName()));
            } else {
                cssList.add(new File(url+"/temp/temp." + ff.getName()));
            }

        }

        merge(jsList, new File(url+"component/assets/e-all-min.js"));
        
        File[] list = temp.listFiles();
        if(list != null){
            for(File f: list){
                f.delete();
            }
        }
        temp.delete();
    }
    
    public static void compressMergeIE() throws IOException {
        files[0] = "component/assets/js/jquery-ie.js";
        String url = Compress.class.getResource("").getPath();
        url = url.replaceFirst("/", "");
        url = url.replaceFirst("bin/com/loy/e/js/", "src/main/resources/public/");
        File temp = new File(url+"/temp/");
        if(!temp.exists()){
            temp.mkdir();
        }
        
        String path = url;
        for (String f : files) {
            File ff = new File(path + f);
            jsCompress(ff, new File(url+"/temp/temp." + ff.getName()));
        }

        List<File> jsList = new ArrayList<File>();
        List<File> cssList = new ArrayList<File>();
        for (String f : files) {
            File ff = new File(path + f);
            if (ff.getName().endsWith(".js")) {
                jsList.add(new File(url+"/temp/temp." + ff.getName()));
            } else {
                cssList.add(new File(url+"/temp/temp." + ff.getName()));
            }

        }

        merge(jsList, new File(url+"component/assets/e-all-min-ie.js"));
        
        File[] list = temp.listFiles();
        if(list != null){
            for(File f: list){
                f.delete();
            }
        }
        temp.delete();
    }
}
