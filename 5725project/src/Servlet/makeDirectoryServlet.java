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

//@WebServlet(value = {"/makeDirectory"} )
//public class makeDirectoryServlet extends HttpServlet {
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String path = request.getParameter("path");
//        boolean loop = true;
//        int a = 1;
//        while(loop){
//            if( a==1){
//                File file = new File(path + "/untitled folder ");
//                if(file.exists()){
//                    a++;
//                    continue;
//                }
//                else{
//                    path = path + "/untitled folder ";
//                    loop = false;
//                }
//                a++;
//            }
//            else{
//                File file = new File(path + "/untitled folder " + a);
//                if(file.exists()){
//                    a++;
//                    continue;
//                }
//                else{
//                    path = path + "/untitled folder " + a;
//                    loop = false;
//                }
//                a++;
//            }
//        }
//
//        File file = new File(path);
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("result","create new folder: success");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        file.mkdirs();
//        response.setContentType("text/html;charset=UTF-8");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.addHeader("Access-Control-Allow-Credentials" , "true");
//        PrintWriter printWriter = response.getWriter();
//        printWriter.println(obj);
//
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("utf-8");
//
//        System.out.println("这是post请求");
//    }
//}

@WebServlet(value = {"/makeDirectory"} )
public class makeDirectoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        String folderName = "";

        boolean loop = true;
        int a = 1;
        while(loop){
            if(a == 1){
                File file = new File(path + "/untitled folder");
                if(file.exists()){
                    a++;
                    continue;
                }
                else{
                    folderName = "untitled folder";
                    loop = false;
                }
                a++;
            }
            else{
                File file = new File(path + "/untitled folder" + a);
                if(file.exists()){
                    a++;
                    continue;
                }
                else{
                    folderName = "untitled folder" + a;
                    loop = false;
                }
                a++;
            }
        }

        File file = new File(path + "/" + folderName);
        JSONObject obj = new JSONObject();
        try {
            if(file.exists()){
                obj.put("result", "make directory: fail");  // @chr change
            }
            else{
                file.mkdirs();
                obj.put("result", "make directory: success");  // @chr change
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials" , "true");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(obj);
    }
}
