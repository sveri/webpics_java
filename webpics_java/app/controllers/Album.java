package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.album.album;

public class Album extends Controller {

    // @Restrict(Application.ADMIN_ROLE)
    public static Result index() {

	return ok("bla");
    }

    // @Restrictions(@Restrict(Application.ADMIN_ROLE))
    public static Result albums(String albumName) {

	return ok(album.render());
    }
}
