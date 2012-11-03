package controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import models.pic.Photo;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import service.PictureService;
import views.html.album.album;
import views.html.album.index;

public class Album extends Controller {

    // @Restrict(Application.ADMIN_ROLE)
    public static Result index() {
	List<models.pic.Album> all = models.pic.Album.find.all();

	return ok(index.render(all));
    }

    // @Restrictions(@Restrict(Application.ADMIN_ROLE))
    public static Result albums(Long albumId) {
	List<Photo> photos = Photo.findPhotosByAlbumId(albumId);

	return ok(album.render(photos));
    }

    public static Result uploadFiles(Long albumId, String qqfile) {

	final File file = request().body().asRaw().asFile();
	if (!PictureService.storePicture(file, albumId, qqfile)) {
	    return ok("{\"error\": Something went wrong.}");
	}

	return ok("{\"success\": true}");
    }

    public static Result getFile(Long albumId, String fileName) {
	BufferedImage image = null;
	ByteArrayOutputStream baos = new ByteArrayOutputStream();

	try {
	    image = ImageIO.read(new File("/home/sveri/git/webpics_java/webpics_java/pix_bin/normal/album/1/"
		    + fileName));
	    ImageIO.write(image, "jpg", baos);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

	response().setContentType("image/jpg");

	return ok(bais);
    }
}
