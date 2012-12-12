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
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import service.PictureService;
import views.html.album.album;
import views.html.album.index;
import views.html.album.upload;
import be.objectify.deadbolt.actions.And;
import be.objectify.deadbolt.actions.Restrict;
import be.objectify.deadbolt.actions.Restrictions;
import be.objectify.deadbolt.actions.RoleHolderPresent;

public class Album extends Controller {

    @Restrictions({ @And(Application.USER_ROLE), @And(Application.ADMIN_ROLE) })
    public static Result albums(final Long albumId) {
	final List<Photo> photos = Photo.findPhotosByAlbumId(albumId);

	return ok(album.render(photos, albumId));
    }

    @RoleHolderPresent
    public static Result getFile(final Long albumId, final String fileName, final String size) {
	BufferedImage image = null;
	final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	String path = "/home/sveri/git/webpics_java/webpics_java/pix_bin/album/" + albumId + "/";
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
	} catch (final IOException e) {
	    Logger.error("Error creating target folder or moving file, or maybe album does not exist.", e);
	}
	final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

	response().setContentType("image/jpg");

	return ok(bais);
    }

    @Restrictions({ @And(Application.USER_ROLE), @And(Application.ADMIN_ROLE) })
    public static Result index() {
	final List<models.pic.Album> all = models.pic.Album.find.order("name").findList();

	return ok(index.render(all));
    }

    @Restrictions({ @And(Application.USER_ROLE), @And(Application.ADMIN_ROLE) })
    public static Result newAlbum() {
	final DynamicForm form = form().bindFromRequest();

	final models.pic.Album album = new models.pic.Album();
	album.name = form.field("name").value();
	album.save();

	return redirect(routes.Album.index());
    }

    @Restrict(Application.ADMIN_ROLE)
    public static Result upload(final Long albumId) {
	return ok(upload.render(albumId));
    }

    @Restrict(Application.ADMIN_ROLE)
    public static Result uploadFiles(final Long albumId, final String qqfile) {

	final File file = request().body().asRaw().asFile();
	if (qqfile.endsWith("zip")) {
	    if (!PictureService.storeZip(file, albumId)) {
		return ok("{\"error\": Something went wrong.}");
	    }
	} else if (!PictureService.storePicture(file, albumId, qqfile)) {
	    return ok("{\"error\": Something went wrong.}");
	}

	return ok("{\"success\": true}");
    }

    @Restrict(Application.ADMIN_ROLE)
    public static Result uploadLocalZipFile(final Long albumId) {
	final DynamicForm form = form().bindFromRequest();
	final String filePath = form.get("file_path");

	final File file = new File(filePath);
	final String fileName = file.getName();

	if (fileName.endsWith("zip")) {
	    if (!PictureService.storeZip(file, albumId)) {
		return ok("{\"error\": Something went wrong.}");
	    }
	}

	// } else if (!PictureService.storePicture(file, albumId, qqfile)) {
	// return ok("{\"error\": Something went wrong.}");
	// }

	return redirect(routes.Album.albums(albumId));
    }

    // =======
    // @Restrict(Application.ADMIN_ROLE)
    // public static Result uploadFiles(Long albumId, String qqfile) {
    //
    // final File file = request().body().asRaw().asFile();
    // if (!PictureService.storePicture(file, albumId, qqfile)) {
    // return ok("{\"error\": Something went wrong.}");
    // }
    //
    // return ok("{\"success\": true}");
    // }
    //
    // @Restrict(Application.USER_ROLE)
    // >>>>>>> branch 'master' of https://github.com/sveri/webpics_java.git
}
