package de.earlgreyt.date4u.core;

import de.earlgreyt.date4u.Date4uApplication;
import de.earlgreyt.date4u.core.entitybeans.Photo;
import de.earlgreyt.date4u.core.thumbnails.AwtBicubicThumbnail;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
public class PhotoService {

  private final FileSystem fs;
  private final AwtBicubicThumbnail thumbnail;

  public PhotoService(FileSystem fs, AwtBicubicThumbnail thumbnail) {
    this.fs = fs;
    this.thumbnail = thumbnail;
  }

  public Optional<byte[]> download(String name) {
    try {
      return Optional.of(fs.load(name + ".jpg"));
    } catch (UncheckedIOException e) {
      return Optional.empty();
    }
  }
  public Optional<byte[]> download(@Valid Photo photo){
    return download(photo.getName());
  }
  public String upload(byte[] imageBytes) {
    String imageName = UUID.randomUUID().toString();
    fs.store(imageName + ".jpg", imageBytes);
    byte[] thumbnailBytes = thumbnail.thumbnail(imageBytes);
    fs.store(imageName + "-thumb.jpg", thumbnailBytes);
    return imageName;
  }
}
