package controllers;

import java.util.ArrayList;
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
	return ok(add_user.render(USER_FORM, PASSWORD_FORM, SecurityRole.findAllOrderByName(),
		new ArrayList<SecurityRole>()));
    }

    public static Result doAddUser() {
	final Form<User> userForm = USER_FORM.bindFromRequest();
	final Form<PasswordForm> passwordForm = PASSWORD_FORM.bindFromRequest();

	if (userForm.hasErrors() || passwordForm.hasErrors()) {
	    flash(Application.FLASH_ERROR_KEY, Messages.get("pix.user.add_edit_error"));

	    return badRequest(add_user.render(userForm, PASSWORD_FORM, SecurityRole.findAllOrderByName(),
		    new ArrayList<SecurityRole>()));
	}

	final User user = userForm.get();

	getRolesFromForm(user, request().body().asFormUrlEncoded());

	user.save();
	user.saveManyToManyAssociations("roles");

	final String newPassword = passwordForm.get().password;
	user.changePassword(new MyUsernamePasswordAuthUser(newPassword), true);

	flash(Application.FLASH_MESSAGE_KEY, Messages.get("pix.user.successful_added"));
	return redirect(routes.Admin.userOverview());
    }

    /*
     * retrieves the roles from the form which was sent with the request and
     * adds it to the given user
     */
    private static void getRolesFromForm(final User user, final Map<String, String[]> formUrlEncoded) {
	final Map<String, String[]> formEncoded = formUrlEncoded;
	for (final String key : formEncoded.keySet()) {
	    if (key.equals("userRole")) {
		final String[] roles = formEncoded.get(key);
		for (final String role : roles) {
		    User.addRole(user, Long.parseLong(role));
		}
	    }
	}
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
	    flash(Application.FLASH_ERROR_KEY, Messages.get("pix.user.add_edit_error"));
	    return badRequest(edit_user
		    .render(filledForm, PASSWORD_FORM, SecurityRole.findAllOrderByName(), user.roles));
	}

	final User user = filledForm.get();

	if (user.roles != null) {
	    user.roles.clear();
	}

	getRolesFromForm(user, request().body().asFormUrlEncoded());

	Ebean.deleteManyToManyAssociations(user, "roles");
	user.saveManyToManyAssociations("roles");
	user.update();

	flash(Application.FLASH_MESSAGE_KEY, Messages.get("pix.user.successful_change"));
	return redirect(routes.Admin.editUser(user.id));
    }

    public static Result doChangePassword() {
	final Form<PasswordForm> passwordForm = PASSWORD_FORM.bindFromRequest();
	final long userId = Long.parseLong(passwordForm.data().get("user_id_for_password"));
	final User user = User.find.byId(userId);

	if (passwordForm.hasErrors()) {
	    flash(Application.FLASH_ERROR_KEY, Messages.get("pix.user.add_edit_error"));
	    return badRequest(edit_user.render(USER_FORM.fill(user), passwordForm, SecurityRole.findAllOrderByName(),
		    user.roles));
	}

	final String newPassword = passwordForm.get().password;
	user.changePassword(new MyUsernamePasswordAuthUser(newPassword), true);

	flash(Application.FLASH_MESSAGE_KEY, Messages.get("pix.user.password.successful_change"));
	return redirect(routes.Admin.editUser(userId));
    }
}
