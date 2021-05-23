package Servlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet(value = {"/upload"})
@MultipartConfig

public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("这是一个post请求方法");

        String path = request.getParameter("path");
        System.out.println("path:" + path);
        Part part = request.getPart("filename");
        String submittedFileName = part.getSubmittedFileName();
        System.out.println("submittedFileName:" + submittedFileName);

        JSONObject obj = new JSONObject();
        if(submittedFileName.isEmpty()){
            try {
                obj.put("result","upload: fail");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.setContentType("text/html;charset=UTF-8");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Credentials" , "true");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(obj);
        }
        else{
            File file = new File(path);
            // 判断文件是否存在不存在创建
            if(!file.exists()) {
                file.mkdir();
                System.out.println("成功创建");
            }
            String path2 = file+"/"+submittedFileName;
            System.out.println(path2);
            part.write(path2);
            try {
                obj.put("result","upload: success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.setContentType("text/html;charset=UTF-8");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Credentials" , "true");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(obj);
        }
    }

}
