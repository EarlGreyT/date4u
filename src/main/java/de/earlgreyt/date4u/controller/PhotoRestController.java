package de.earlgreyt.date4u.controller;

import de.earlgreyt.date4u.core.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RestController
public class PhotoRestController {
    private final PhotoService photoService;

    @Autowired
    public PhotoRestController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping( path     = "/api/photo/{photoName}",
            produces = MediaType.IMAGE_JPEG_VALUE )
    public byte[] getPhoto(@PathVariable String photoName) throws IOException {
        Optional<byte[]> optionalBytes = photoService.download(photoName);
        byte[] photo;
        if (optionalBytes.isPresent()){
            photo = optionalBytes.get();
        } else {
            Resource resource = new UrlResource(
                    "https://upload.wikimedia.org/wikipedia/commons/6/6a/A_blank_flag.png"
            );
            InputStream inputStream = resource.getInputStream();
            photo = StreamUtils.copyToByteArray( inputStream );
        }
        return photo;
    }
}
