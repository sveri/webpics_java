package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import models.pic.Album;
import models.pic.Photo;

import play.Play;
import play.db.ebean.Transactional;

public class PictureService {

    @Transactional
    public static boolean storePicture(File file, Long albumId, String qqfile) {
	Path pathOrigin = Paths.get(file.getAbsolutePath());
	String path = Play.application().configuration()
		.getString("picture.path");

	Path pathTarget = Paths.get(path);

	if (!Files.exists(pathTarget)) {
	    try {
		Files.createDirectory(pathTarget);
	    } catch (IOException e) {
		e.printStackTrace();
		return false;
	    }
	}

	Path pathTargetFile = Paths.get(path + qqfile);

	try {
	    Files.move(pathOrigin, pathTargetFile,
		    StandardCopyOption.REPLACE_EXISTING);
	} catch (IOException e) {
	    System.out.println("error");
	    e.printStackTrace();
	}

	Photo photo = new Photo();
	photo.name = qqfile;
	photo.path = pathTargetFile.toString();
	photo.albumId = Album.find.byId(albumId);
	photo.save();

	return true;
    }
}
