package de.earlgreyt.date4u.controller;

import de.earlgreyt.date4u.core.PhotoService;
import de.earlgreyt.date4u.core.UnicornDetailService;
import de.earlgreyt.date4u.core.UnicornDetails;
import de.earlgreyt.date4u.core.entitybeans.Photo;
import de.earlgreyt.date4u.core.entitybeans.Profile;
import de.earlgreyt.date4u.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Optional;

@RestController
public class PhotoRestController {
    private final PhotoService photoService;
    private final UnicornDetailService unicornDetailService;
    private final ProfileRepository profileRepository;
    @Autowired
    public PhotoRestController(PhotoService photoService, UnicornDetailService unicornDetailService, ProfileRepository profileRepository) {
        this.photoService = photoService;
        this.unicornDetailService = unicornDetailService;
        this.profileRepository = profileRepository;
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
    @GetMapping( path     = "/api/thumb/{photoName}",
            produces = MediaType.IMAGE_JPEG_VALUE )
    public byte[] getThumbPhoto(@PathVariable String photoName) throws IOException {
        Optional<byte[]> optionalBytes = photoService.download(photoName+"-thumb");
        byte[] photo;
        if (optionalBytes.isPresent()){
            photo = optionalBytes.get();
        } else {
            return getPhoto(photoName);
        }
        return photo;
    }

}
