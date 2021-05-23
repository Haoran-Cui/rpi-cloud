package Servlet;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@WebServlet(value = {"/download"})
public class DownloadServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("这是一个get方法");

        String path = request.getParameter("path");
        String type = request.getParameter("type");

        if(type.equals("folder")){
            ArrayList<File> files = getFiles(path);
            Map<String,String> bases = new HashMap<>();
            for (int i = 0; i < files.size(); i++) {
                File s =  files.get(i);
                String value = s.getPath().substring(s.getPath().indexOf("/")+1); // 转为相对路径
                bases.put(s.getAbsolutePath(), value);
            }

            String zip_path = path+".zip";
            System.out.println(zip_path);
            File zip = new File(zip_path);
            zipFileFolders(files,zip,bases);

            String fileName = zip_path.substring(zip_path.lastIndexOf("/") + 1);
            System.out.println(fileName);
            String mimeType = getServletContext().getMimeType(fileName);
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            FileInputStream fileInputStream = new FileInputStream(zip_path);

            int len = 0;
            byte[] bytes = new byte[1024];
            ServletOutputStream servletOutputStream = response.getOutputStream();
            while ((len = fileInputStream.read(bytes)) > 0) {
                servletOutputStream.write(bytes, 0, len);
            }
            servletOutputStream.close();
            fileInputStream.close();

            File file = new File(zip_path);
            file.delete();

        }
        else {
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            String mimeType = getServletContext().getMimeType(fileName);
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            FileInputStream fileInputStream = new FileInputStream(path);

            int len = 0;
            byte[] bytes = new byte[1024];
            ServletOutputStream servletOutputStream = response.getOutputStream();
            while ((len = fileInputStream.read(bytes)) > 0) {
                servletOutputStream.write(bytes, 0, len);
            }
            servletOutputStream.close();
            fileInputStream.close();
        }

        // @chr change
        JSONObject obj = new JSONObject();
        try {
            obj.put("result","download: success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials" , "true");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(obj);
        // @chr change end

    }

    public static ArrayList<File> getFiles(String path) {
        ArrayList<File> files = new ArrayList<File>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i]);
            }
            if (tempList[i].isDirectory()) {
                ArrayList<File> files1 = getFiles(tempList[i].getPath());
                files.addAll(files1);
            }
        }
        return files;
    }

    public static void zipFileFolders(ArrayList<File> srcfile, File zipfile, Map<String,String> bases) {
        byte[] buf = new byte[1024];
        ZipOutputStream out = null;
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
            out = new ZipOutputStream(new FileOutputStream(zipfile));
            for (int i = 0; i < srcfile.size(); i++) {
                FileInputStream in = new FileInputStream(srcfile.get(i));
                String filePath = bases.get(srcfile.get(i).getAbsolutePath());
                if(filePath==null)
                    filePath = "";
                else
                    filePath = filePath.substring(0,filePath.lastIndexOf("/"));
                    filePath += "/";
                out.putNextEntry(new ZipEntry(filePath+srcfile.get(i).getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            System.out.println("压缩完成.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
