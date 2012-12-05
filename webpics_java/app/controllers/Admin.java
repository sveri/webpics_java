package controllers;

import java.util.List;

import models.User;
import play.data.Form;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.user.add_user;
import views.html.user.edit_user;
import views.html.user.user_overview;

public class Admin extends Controller {

    // public class UserForm {
    //
    // public UserForm() {
    // }
    //
    // public UserForm(final User user) {
    // this.active = user.active;
    // this.email = user.email;
    // this.emailValidated = user.emailValidated;
    // this.name = user.name;
    // }
    //
    // @Required
    // @MinLength(5)
    // public String password;
    //
    // @Required
    // @MinLength(5)
    // public String repeatPassword;
    //
    // @Required
    // public String name;
    //
    // @Required
    // public String email;
    //
    // public boolean active;
    //
    // public boolean emailValidated;
    //
    // public String validate() {
    // if (!this.password.equals(this.repeatPassword)) {
    // return
    // Messages.get("playauthenticate.password.signup.error.passwords_not_same");
    // }
    // return null;
    // }
    // }

    // public static final Form<UserForm> USER_FORM = form(UserForm.class);
    public static final Form<User> USER_FORM = form(User.class);

    public static Result userOverview() {
	final List<User> users = User.find.order("name").findList();
	return ok(user_overview.render(users));
    }

    public static Result addUser() {
	return ok(add_user.render());
    }

    public static Result editUser(final Long userId) {
	final User user = User.find.byId(userId);
	final Form<User> fill = USER_FORM.fill(user);
	System.out.println(user.email);
	System.out.println(fill.field("email").value());
	return ok(edit_user.render(fill));
    }

    public static Result doEditUser() {
	final Form<User> filledForm = USER_FORM.bindFromRequest();
	if (filledForm.hasErrors()) {
	    // User did not fill everything properly
	    return badRequest(edit_user.render(filledForm));
	}
	return redirect(routes.Admin.userOverview());
    }
}
