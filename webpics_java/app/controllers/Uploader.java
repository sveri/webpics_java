package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

public class Uploader extends Controller {

    public static Result files(String qqfile) {

	File file = request().body().asRaw().asFile();
	Path pathOrigin = Paths.get(file.getAbsolutePath());
	Path pathTarget = Paths.get(Play.application().configuration().getString("picture.path") + "\\" + qqfile);
	try {
	    Files.move(pathOrigin, pathTarget, StandardCopyOption.REPLACE_EXISTING);
	} catch (IOException e) {
	    System.out.println("error");
	    e.printStackTrace();
	    return ok("{\"error\": Something went wrong.}");
	}
	return ok("{\"success\": true}");
    }

}
