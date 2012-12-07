package controllers;

import java.util.List;
import java.util.Map;

import models.SecurityRole;
import models.User;
import play.data.Form;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthUser;
import views.html.user.add_user;
import views.html.user.edit_user;
import views.html.user.user_overview;

import com.avaje.ebean.Ebean;

public class Admin extends Controller {

    public static class PasswordForm {

	@Required
	@MinLength(6)
	public String password;

	@Required
	@MinLength(6)
	public String repeatPassword;

	public String validate() {
	    if (this.password == null || !this.password.equals(this.repeatPassword)) {
		return Messages.get("playauthenticate.password.signup.error.passwords_not_same");
	    }
	    return null;
	}
    }

    // public static final Form<UserForm> USER_FORM = form(UserForm.class);
    public static final Form<User> USER_FORM = form(User.class);

    public static final Form<PasswordForm> PASSWORD_FORM = form(PasswordForm.class);

    public static Result userOverview() {
	final List<User> users = User.find.order("name").findList();
	return ok(user_overview.render(users));
    }

    public static Result addUser() {
	return ok(add_user.render());
    }

    public static Result editUser(final Long userId) {
	final User user = User.find.byId(userId);
	final List<SecurityRole> userRoles = user.roles;
	final Form<User> userForm = USER_FORM.fill(user);
	return ok(edit_user.render(userForm, PASSWORD_FORM, SecurityRole.findAllOrderByName(), userRoles));
    }

    public static Result doEditUser() {
	final Form<User> filledForm = USER_FORM.bindFromRequest();
	if (filledForm.hasErrors()) {
	    final User user = User.find.byId(Long.parseLong(filledForm.data().get("id")));
	    // User did not fill everything properly
	    return badRequest(edit_user
		    .render(filledForm, PASSWORD_FORM, SecurityRole.findAllOrderByName(), user.roles));
	}

	final User user = filledForm.get();

	if (user.roles != null) {
	    user.roles.clear();
	}
	final Map<String, String[]> formEncoded = request().body().asFormUrlEncoded();
	for (final String key : formEncoded.keySet()) {
	    if (key.equals("userRole")) {
		final String[] roles = formEncoded.get(key);
		for (final String role : roles) {
		    User.addRole(user, Long.parseLong(role));
		}
	    }
	}

	Ebean.deleteManyToManyAssociations(user, "roles");
	user.saveManyToManyAssociations("roles");
	user.update();

	return redirect(routes.Admin.userOverview());
    }

    public static Result doChangePassword() {
	final Form<PasswordForm> passwordForm = PASSWORD_FORM.bindFromRequest();
	final long userId = Long.parseLong(passwordForm.data().get("user_id_for_password"));
	final User user = User.find.byId(userId);

	if (passwordForm.hasErrors()) {
	    return badRequest(edit_user.render(USER_FORM.fill(user), passwordForm, SecurityRole.findAllOrderByName(),
		    user.roles));
	}

	final String newPassword = passwordForm.get().password;
	user.changePassword(new MyUsernamePasswordAuthUser(newPassword), true);

	return redirect(routes.Admin.editUser(userId));
    }
}
