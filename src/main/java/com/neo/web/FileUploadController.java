package com.neo.web;

import com.neo.secvice.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
public class FileUploadController {

    @Autowired
    private ImportService importService;

    @RequestMapping(value = "/fileUpload",method = RequestMethod.POST)
    public String fileUpload(@RequestParam("file") MultipartFile file){

        if (file.isEmpty()) {
            return "文件不能为空";
        }
        InputStream inputStream;
        List<List<Object>> list = null;
        try {
            inputStream = file.getInputStream();
             list = importService.getBankListByExcel(inputStream, file.getOriginalFilename());
        inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (List<Object> lo : list) {
            //TODO 随意发挥
            System.out.println(lo);
        }
        return "上传成功";
    }
}
