package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.vaith.weeblogbackend.model.Result;
import xyz.vaith.weeblogbackend.security.SecurityBody;
import xyz.vaith.weeblogbackend.service.ImageService;
import xyz.vaith.weeblogbackend.util.QiniuUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Resource
    ImageService imageService;

    @RequestMapping("/upload")
    public Result uploadFile(HttpServletRequest request, @RequestParam MultipartFile file, @RequestParam String filename) throws Exception {
        String path = request.getSession().getServletContext().getRealPath("/images/");
       return Result.success(imageService.saveImageFileToBucket(file, filename, path));
    }

    @RequestMapping("/list")
    public Result list(Integer page, Integer size) throws Exception {
        return Result.success(imageService.getImageList(page, size));
    }
}
