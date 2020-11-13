package com.neo;

import java.io.File;
import java.util.List;

public class CopyFile {

    public static void main(String[] args) {
        String sxly = "商丘公积金（地址修改为59段）";
        String dishi = "商丘";
        String folder = "caifen-shangqiu(1)";

        String deleteFolder = "C:\\Users\\Administrator\\Documents\\地市已打包文件夹\\";
        String fromFolder = "C:\\Users\\Administrator\\Documents\\chaifen\\";

        List<String[]> list = ChaiFen.excel(sxly);
        System.out.println(list.size());
        for (String[] strS : list) {
            String deletePath = deleteFolder + dishi + "\\" + strS[0] + "\\";
            ChaiFen.copyFile(new File(fromFolder + folder + "\\" + strS[0] + ".html"), deletePath + strS[1], 2);
            ChaiFen.ZipCompress(deletePath + strS[1], deletePath + strS[1] + ".zip");
            ChaiFen.deleteDir(new File(fromFolder + folder + "\\" + strS[0] + ".html"));
        }
    }
}
