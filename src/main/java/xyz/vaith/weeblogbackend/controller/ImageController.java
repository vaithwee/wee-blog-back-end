package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.vaith.weeblogbackend.authority.Authority;
import xyz.vaith.weeblogbackend.model.Result;
import xyz.vaith.weeblogbackend.param.ImageParam;
import xyz.vaith.weeblogbackend.service.ImageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Authority
@RestController
@RequestMapping("/image")
public class ImageController {

    @Resource
    ImageService imageService;

    @RequestMapping("/upload")
    public Result uploadFile(HttpServletRequest request, @RequestParam MultipartFile file, @RequestParam String filename, @RequestParam Integer type) throws Exception {
        String path = request.getSession().getServletContext().getRealPath("/images/");
       return Result.success(imageService.saveImageFileToBucket(file, filename, path, type));
    }

    @RequestMapping("/list")
    public Result list(Integer page, Integer size) throws Exception {
        return Result.success(imageService.getImageList(page, size));
    }

    @RequestMapping("/remove")
    public Result remove(@RequestBody ImageParam param) throws Exception {
        imageService.deleteImage(param.getId());
        return Result.success(null);
    }
}
