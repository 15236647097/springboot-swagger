package com.neo;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;

public class ChaiFen {
    public static void main(String[] args) {
        String sxly = "新蔡公积金中心";
        String folder = "chaifen-xincai";

        String path = "C:\\Users\\Administrator\\Documents\\";
        String folderPath = "C:\\Users\\Administrator\\Documents\\" + folder + "\\";
        List<String[]> list = excel(sxly);
        System.out.println(list.size());
        for (String[] strS : list) {
            // 先复制文件
            copyFolder(folderPath + "css", folderPath + strS[1] + "\\css");
            copyFolder(folderPath + "img", folderPath + strS[1] + "\\img");
            copyFolder(folderPath + "js", folderPath + strS[1] + "\\js");
            copyFile(new File(folderPath + strS[0] + ".html"), folderPath + strS[1], 2);
            ZipCompress(folderPath + strS[1], folderPath + strS[1] + ".zip");

            //复制文件夹
            copyFolder(folderPath + strS[1], folderPath + strS[0] + "\\" + strS[1]);
            //修改版本检测清单excel内容后复制到指定文件夹
            modifyExcel(strS[0], folderPath + strS[0]);
            //复制图片
            String imgName = strS[2].replace(".0", "");
            if (Integer.parseInt(imgName) < 185) {
                copyFile(new File(path + "查询类图标\\1-184\\" + imgName + ".png"), folderPath + strS[0], 1);
            } else {
                copyFile(new File(path + "查询类图标\\185-369\\icon_" + imgName + ".png"), folderPath + strS[0], 1);
            }
            //复制压缩包
            copyFile(new File(folderPath + strS[1] + ".zip"), folderPath + strS[0], 1);
            deleteDir(new File(folderPath + strS[0] + ".html"));
            deleteDir(new File(folderPath + strS[1]));
            deleteDir(new File(folderPath + strS[1] + ".zip"));
        }
        // 再删除源文件，以免复制的时候错乱
        deleteDir(new File(folderPath +"css"));
        deleteDir(new File(folderPath +"img"));
        deleteDir(new File(folderPath +"js"));
    }

    static List<String[]> excel(String sxly) {
        List<String[]> list = new ArrayList<>();
        try {
            String filePath = "C:\\Users\\Administrator\\Documents\\369清单(4).xlsx";
            //用流的方式先读取到你想要的excel的文件
            InputStream fis = new FileInputStream(new File(filePath));
            //获取整个excel
            XSSFWorkbook hb = new XSSFWorkbook(fis);
            //获取第一个表单sheet
            Sheet sheet = hb.getSheetAt(1);
            //循环行数依次获取列数
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                //获取哪一行i
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(5).toString().equals(sxly)) {
                    String[] strings = new String[3];
                    strings[0] = row.getCell(3).toString();
                    strings[1] = row.getCell(4).toString();
                    strings[2] = row.getCell(7).toString();
                    list.add(strings);
                }
            }
            fis.close();
        } catch (Exception e) {
            System.out.println("读取excel文件出错" + e);
        }
        return list;
    }

    private static void modifyExcel(String value, String folderPath) {
        try {
            File f = new File("C:\\Users\\Administrator\\Documents\\版本检测清单.xls");
            InputStream inputStream = new FileInputStream(f);
            HSSFWorkbook xssfWorkbook = new HSSFWorkbook(inputStream);
            //  XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0); //如果是.xlsx文件使用这个
            HSSFSheet sheet1 = xssfWorkbook.getSheetAt(0);
            Row row = sheet1.getRow(1);
            Cell cell = row.getCell(2);
            cell.setCellValue(value);
            FileOutputStream out = new FileOutputStream(f);
            xssfWorkbook.write(out);
            xssfWorkbook.close();
            inputStream.close();
            out.close();
            copyFile(f, folderPath, 1);
        } catch (Exception e) {
            System.out.println("修改excel内容操作出错" + e);
        }
    }

    // 删除某个目录及目录下的所有子目录和文件
    static boolean deleteDir(File dir) {
        // 如果是文件夹
        if (dir.isDirectory()) {
            // 则读出该文件夹下的的所有文件
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (String aChildren : children) {
                // File f=new File（String parent ，String child）
                // parent抽象路径名用于表示目录，child 路径名字符串用于表示目录或文件。
                // 连起来刚好是文件路径
                boolean isDelete = deleteDir(new File(dir, aChildren));
                // 如果删完了，没东西删，isDelete==false的时候，则跳出此时递归
                if (!isDelete) {
                    return false;
                }
            }
        }
        // 读到的是一个文件或者是一个空目录，则可以直接删除
        return dir.delete();
    }

    // 复制某个目录及目录下的所有子目录和文件到新文件夹
    private static void copyFolder(String oldPath, String newPath) {
        try {
            // 如果文件夹不存在，则建立新文件夹
            (new File(newPath)).mkdirs();
            // 读取整个文件夹的内容到file字符串数组，下面设置一个游标i，不停地向下移开始读这个数组
            File filelist = new File(oldPath);
            String[] file = filelist.list();
            // 要注意，这个temp仅仅是一个临时文件指针
            // 整个程序并没有创建临时文件
            File temp;
            for (String aFile : file) {
                // 如果oldPath以路径分隔符/或者\结尾，那么则oldPath/文件名就可以了
                // 否则要自己oldPath后面补个路径分隔符再加文件名
                // 谁知道你传递过来的参数是f:/a还是f:/a/啊？
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + aFile);
                } else {
                    temp = new File(oldPath + File.separator + aFile);
                }

                // 如果游标遇到文件
                if (temp.isFile()) {
                    copyFile(temp, newPath, 1);
                }
                // 如果游标遇到文件夹
                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + aFile, newPath + "/" + aFile);
                }
            }
        } catch (Exception e) {
            System.out.println("复制oldPath" + oldPath + "整个文件夹内容到newPath" + newPath + "操作出错" + e);
        }
    }

    static void copyFile(File temp, String newPath, int copyType) {
        FileInputStream input;
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(temp),"UTF-8"));
            input = new FileInputStream(temp);
            // 复制并且改名，为1时原名复制，不为1时文件名更改为index
            FileOutputStream output;
            PrintWriter pw ;
            if (copyType == 1) {
                output = new FileOutputStream(newPath + "/" + temp.getName());
            } else {
                output = new FileOutputStream(newPath + "/index.html");
            }
            pw= new PrintWriter(output);
            String str;
            while((str = buf.readLine()) != null){
                String outStr = new String(str.getBytes("UTF-8"),"UTF-8");
                pw.write(outStr);
                pw.write("\r\n");
            }
            buf.close();
            pw.close();
        } catch (Exception e) {
            System.out.println("复制文件" + temp.getName() + "内容操作出错" + e);
        }
    }


    /**
     * zip文件压缩
     *
     * @param inputFile  待压缩文件夹/文件名
     * @param outputFile 生成的压缩包名字
     */

    static void ZipCompress(String inputFile, String outputFile) {
        try {
            //创建zip输出流
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
            //创建缓冲输出流
            BufferedOutputStream bos = new BufferedOutputStream(out);
            File input = new File(inputFile);
            compress(out, bos, input, null);
            bos.close();
            out.close();
        } catch (Exception e) {
            System.out.println("压缩文件时出现错误,文件夹名：" + inputFile + "--" + e);
        }
    }

    /**
     * @param name 压缩文件名，可以写为null保持默认
     */
    //递归压缩
    private static void compress(ZipOutputStream out, BufferedOutputStream bos, File input, String name) throws IOException {
        if (name == null) {
            name = input.getName();
        }
        //如果路径为目录（文件夹）
        if (input.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = input.listFiles();

            if (flist.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入
            {
                out.putNextEntry(new ZipEntry(name + "/"));
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for (File aFlist : flist) {
                    compress(out, bos, aFlist, name + "/" + aFlist.getName());
                }
            }
        } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            out.putNextEntry(new ZipEntry(name));
            FileInputStream fos = new FileInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int len;
            //将源文件写入到zip文件中
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bis.close();
            fos.close();
        }
    }
}
