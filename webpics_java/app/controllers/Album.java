package controllers;

import java.io.File;
import java.util.List;

import play.mvc.Controller;
import play.mvc.Result;
import service.PictureService;
import views.html.album.index;
import views.html.album.album;

public class Album extends Controller {

    // @Restrict(Application.ADMIN_ROLE)
    public static Result index() {
	List<models.pic.Album> all = models.pic.Album.find.all();

	return ok(index.render(all));
    }

    // @Restrictions(@Restrict(Application.ADMIN_ROLE))
    public static Result albums(Long albumId) {

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
