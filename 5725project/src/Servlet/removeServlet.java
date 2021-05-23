package Servlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = {"/remove"} )
public class removeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        String type = request.getParameter("type");

        if(type.equals("file")){
            File file = new File(path);
            file.delete();
            JSONArray array = new JSONArray();
            JSONObject obj = new JSONObject();
            try {
                obj.put("result:", "success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
            response.setContentType("text/html;charset=UTF-8");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(array);
        }
        else {
            File file = new File(path);
            File[] filelist = file.listFiles();
            JSONArray array = new JSONArray();
            if (filelist == null) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("result:", "delete: fail");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                array.put(obj);
                response.setContentType("text/html;charset=UTF-8");
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Credentials", "true");
                PrintWriter printWriter = response.getWriter();
                printWriter.println(array);
            } else {
                JSONObject obj = new JSONObject();
                remove(file);
                try {
                    obj.put("result:", "delete: success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                array.put(obj);
                response.setContentType("text/html;charset=UTF-8");
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Credentials", "true");
                PrintWriter printWriter = response.getWriter();
                printWriter.println(array);
            }
        }
    }

    public static void remove(File file){
        File[] filelist = file.listFiles();
        if(file!=null){
            for(File file2: filelist){
                if(file2.isDirectory()){
                    remove(file2);
                }
                else{
                    file2.delete();
                }
            }
        }
        file.delete();
    }
}
