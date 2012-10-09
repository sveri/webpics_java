package controllers;

import java.io.File;

import play.mvc.Controller;
import play.mvc.Result;
import service.PictureService;
import views.html.album.index;
import views.html.album.album;

public class Album extends Controller {

    // @Restrict(Application.ADMIN_ROLE)
    public static Result index() {

	return ok(index.render());
    }

    // @Restrictions(@Restrict(Application.ADMIN_ROLE))
    public static Result albums(String albumName) {

	return ok(album.render());
    }

    public static Result files(Long albumId, String qqfile) {

	final File file = request().body().asRaw().asFile();
	if (!PictureService.storePicture(file, albumId, qqfile)) {
	    return ok("{\"error\": Something went wrong.}");
	}

	return ok("{\"success\": true}");
    }
}
