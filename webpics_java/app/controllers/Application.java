package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.Session;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MyLogin;
import views.html.index;
import views.html.login;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;

public class Application extends Controller {

    public static final String FLASH_MESSAGE_KEY = "message";
    public static final String FLASH_ERROR_KEY = "error";
    public static final String USER_ROLE = "user";
    public static final String ADMIN_ROLE = "admin";
    public static final String VIEWER_ROLE = "viewer";

    public static Result doLogin() {
	final Form<MyLogin> filledForm = MyUsernamePasswordAuthProvider.LOGIN_FORM.bindFromRequest();
	if (filledForm.hasErrors()) {
	    // User did not fill everything properly
	    return badRequest(login.render(filledForm));
	} else {
	    // Everything was filled
	    return UsernamePasswordAuthProvider.handleLogin(ctx());
	}
    }

    public static String formatTimestamp(final long t) {
	return new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date(t));
    }

    // @Restrict(Application.USER_ROLE)
    // public static Result profile() {
    // final User localUser = getLocalUser(session());
    // return ok(profile.render(localUser));
    // }

    public static User getLocalUser(final Session session) {
	final User localUser = User.findByAuthUserIdentity(PlayAuthenticate.getUser(session));
	return localUser;
    }

    public static Result index() {
	return ok(index.render());
    }

    public static Result jsRoutes() {
	// return ok(Routes.javascriptRouter("jsRoutes",
	// controllers.routes.javascript.Signup.forgotPassword()))
	// .as("text/javascript");
	return ok();
    }

    public static Result login() {
	return ok(login.render(MyUsernamePasswordAuthProvider.LOGIN_FORM));
    }

}