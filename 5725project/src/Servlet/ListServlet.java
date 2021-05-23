package Servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@WebServlet(value = {"/list"})
public class ListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getParameter("path");
        if(!path.isEmpty()){
            System.out.println("成功");
            File files = new File(path);
            File[] filelist = files.listFiles();

            Arrays.sort(filelist, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if(o1.isDirectory() && o2.isFile()) return -1;
                    if(o1.isDirectory() && o2.isDirectory()) return o1.getName().compareTo(o2.getName());
                    if(o1.isFile() && o2.isFile()) return o1.getName().compareTo(o2.getName());
                    if(o1.isFile() && o2.isDirectory()) return 1;
                    return 0;
                }
            });

            System.out.println(filelist.length);
            JSONArray array = new JSONArray();
            for(File file : filelist){
                try {
                    String fileName = file.getName();
                    if(file.isHidden() || fileName.charAt(0)=='~' || fileName.charAt(0)=='～' || fileName.charAt(0)=='$'){
                        continue;
                    }
                    JSONObject obj = new JSONObject();
                    int index = fileName.lastIndexOf(".");
                    if(file.isDirectory()){
                        obj.put("type","folder");
                        obj.put("name",fileName);
                    }
                    else{
                        obj.put("type","file");
                        obj.put("name",fileName);
                    }
                    obj.put("path",file.getAbsolutePath());
                    obj.put("selected",0);
                    array.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            resp.setContentType("text/html;charset=UTF-8");
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Credentials" , "true");
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(array);
        }
        else{
            System.out.println("失败");
            resp.setContentType("text/html;charset=UTF-8");
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Credentials" , "true");
            PrintWriter printWriter = resp.getWriter();
            printWriter.println("请输入一个正确的路径");
        }

    }

}
