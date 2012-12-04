package controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import models.pic.Photo;
import play.mvc.Controller;
import play.mvc.Result;
import service.PictureService;
import views.html.album.album;
import views.html.album.index;
import views.html.album.upload;
import be.objectify.deadbolt.actions.And;
import be.objectify.deadbolt.actions.Restrict;
import be.objectify.deadbolt.actions.Restrictions;

public class Album extends Controller {

	@Restrict(Application.USER_ROLE)
	public static Result index() {
		List<models.pic.Album> all = models.pic.Album.find.all();

		return ok(index.render(all));
	}

	// @Restrict({ Application.USER_ROLE, Application.ADMIN_ROLE })
	@Restrictions({ @And(Application.USER_ROLE), @And(Application.ADMIN_ROLE) })
	public static Result albums(Long albumId) {
		List<Photo> photos = Photo.findPhotosByAlbumId(albumId);

		return ok(album.render(photos, albumId));
	}

	@Restrict(Application.ADMIN_ROLE)
	public static Result upload(Long albumId) {
		return ok(upload.render(albumId));
	}

	@Restrict(Application.ADMIN_ROLE)
	public static Result uploadFiles(Long albumId, String qqfile) {

		final File file = request().body().asRaw().asFile();
		if (!PictureService.storePicture(file, albumId, qqfile)) {
			return ok("{\"error\": Something went wrong.}");
		}

		return ok("{\"success\": true}");
	}

	@Restrict(Application.USER_ROLE)
	public static Result getFile(Long albumId, String fileName, String size) {
		BufferedImage image = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String path = "/home/sveri/git/webpics_java/webpics_java/pix_bin/album/"
				+ albumId + "/";
		switch (size) {
		case "big":
			path += "big/";
			break;
		case "thumbs":
			path += "thumbs/";
			break;

		default:
			break;
		}

		path += fileName;

		try {
			image = ImageIO.read(new File(path));
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
