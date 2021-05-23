package Servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class zip {
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
                out.putNextEntry(new ZipEntry(filePath+srcfile.get(i).getName()) );
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

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("压缩测试");
        String path = "/Users/apple/Desktop/5725project";
        ArrayList<File> files = getFiles(path);

        Map<String,String> bases = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            File s =  files.get(i);
            String value = s.getPath().substring(s.getPath().indexOf("/")+1); // 转为相对路径
            bases.put(s.getAbsolutePath(), value);
        }

        File zip = new File("hello.zip");
        zipFileFolders(files,zip,bases);
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

}
