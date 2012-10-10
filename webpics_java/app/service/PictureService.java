package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import models.pic.Album;
import models.pic.Photo;
import play.Logger;
import play.Play;

public class PictureService {

    public static String ALBUM_PATH = "album";

    public static boolean storePicture(File file, Long albumId, String qqfile) {

	Path pathOrigin = Paths.get(file.getAbsolutePath());
	String path = Play.application().configuration()
		.getString("picture.path");

	Path pathTarget = Paths.get(path);
	Album album = Album.find.byId(albumId);

	if (album == null) {
	    Logger.error("Album does not exist, exiting.");
	    return false;
	}

	Path pathTargetFile = Paths.get(buildFilePath(path, qqfile, album));

	if (!Files.exists(pathTarget)) {
	    try {
		Files.createDirectory(pathTarget);
	    } catch (IOException e) {
		Logger.error("Error creating target folder.", e);
		return false;
	    }
	}

	try {
	    Files.move(pathOrigin, pathTargetFile,
		    StandardCopyOption.REPLACE_EXISTING);
	} catch (IOException e) {
	    Logger.error("Error while moving file.", e);
	    e.printStackTrace();
	}

	Photo photo = new Photo();
	photo.name = qqfile;
	photo.path = pathTargetFile.toString();
	photo.albumId = album;

	// if photo exists overwrite it
	Photo existing = Photo.findPhotoByPath(pathTargetFile.toString());
	if (existing != null) {
	    photo.id = existing.id;
	    photo.update();
	} else {
	    photo.save();
	}

	return true;
    }

    private static String buildFilePath(String path, String qqfile, Album album) {
	return path + "/" + ALBUM_PATH + "/" + album.id.toString() + "/"
	// return path + "/" + ALBUM_PATH + "/1/"
		+ qqfile;
    }
}
