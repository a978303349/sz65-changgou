package com.changgou.seckill.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.file.FastDFSFile;
import com.changgou.util.FastDFSUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin
public class FileController {

    @PostMapping("/upload")
    public Result upload(@RequestParam("file")MultipartFile file) throws Exception {
        FastDFSFile fastDFSFile=new FastDFSFile(
        file.getOriginalFilename(),  //文件名
        file.getBytes(),  //文件字节数组
                StringUtils.getFilenameExtension(file.getOriginalFilename()));//文件扩展名

        String[] uploads = FastDFSUtil.upload(fastDFSFile);

        String url="http://192.168.211.132:8080/"+uploads[0]+"/"+uploads[1];
        System.out.println(url);
        return new Result(true, StatusCode.OK,"上传成功",url);

    }
}
