package service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import models.pic.Album;
import models.pic.Photo;

import static org.imgscalr.Scalr.*;

import play.Logger;
import play.Play;

public class PictureService {

    public static final String ALBUM_PATH = "album";

    public static final String BIG_PATH = "big";

    public static final String THUMBS_PATH = "thumbs";

    public static boolean storePicture(final File fileOrigin, final Long albumId, final String qqfile) {

	final Album album = Album.find.byId(albumId);
	final Path pathTargetFile;

	try {
	    pathTargetFile = checkPreConditionsForPhotoSave(fileOrigin, album, qqfile);
	} catch (IOException | NullPointerException e) {
	    Logger.error("Error creating target folder or moving file, or maybe album does not exist.", e);
	    return false;
	}

	storePhotoInDb(qqfile, album, pathTargetFile);

	return true;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
	// Create quickly, then smooth and brighten it.
	return resize(img, Method.SPEED, 60, OP_ANTIALIAS, OP_BRIGHTER);
    }

    public static BufferedImage createNormalImage(BufferedImage img) {
	// Create quickly, then smooth and brighten it.
	return resize(img, Method.SPEED, 700, OP_ANTIALIAS, OP_BRIGHTER);
    }

    private static void storePhotoInDb(final String qqfile, final Album album, Path pathTargetFile) {
	final Photo photo = new Photo();
	photo.name = qqfile;
	photo.pathNormal = pathTargetFile.toString();
	photo.albumId = album;

	// if photo exists overwrite it
	final Photo existing = Photo.findPhotoByPath(pathTargetFile.toString(), qqfile);
	if (existing != null) {
	    photo.id = existing.id;
	    photo.update();
	} else {
	    photo.save();
	}
    }

    private static Path checkPreConditionsForPhotoSave(final File fileOrigin, final Album album, final String qqfile)
	    throws IOException, NullPointerException {

	final BufferedImage imageBig = ImageIO.read(fileOrigin);
	final BufferedImage thumbnail = createThumbnail(imageBig);
	final BufferedImage normal = createNormalImage(imageBig);

	// final Path pathOrigin = Paths.get(fileOrigin.getAbsolutePath());

	if (album == null) {
	    Logger.error("Album does not exist, exiting.");
	    throw new NullPointerException();
	}

	final Path pathTargetFolderNormal = Paths.get(buildImageStorePathNormal(qqfile, album));
	final Path pathTargetFolderBig = Paths.get(pathTargetFolderNormal + File.separator + "big");
	final Path pathTargetFolderThumb = Paths.get(pathTargetFolderNormal + File.separator + "thumbs");

	// if (!Files.exists(pathTargetFolderNormal.getParent())) {
	if (!Files.exists(pathTargetFolderNormal)) {
	    Files.createDirectories(pathTargetFolderNormal);
	    Files.createDirectories(pathTargetFolderBig);
	    Files.createDirectories(pathTargetFolderThumb);
	}

	// Files.move(pathOrigin, pathTargetFolderBig,
	// StandardCopyOption.REPLACE_EXISTING);
	File output = new File(pathTargetFolderBig.toString() + File.separatorChar + qqfile);
	output.createNewFile();
	ImageIO.write(imageBig, "jpg", output);
	ImageIO.write(thumbnail, "jpg", new File(pathTargetFolderThumb.toString() + File.separatorChar + qqfile));
	ImageIO.write(normal, "jpg", new File(pathTargetFolderNormal.toString() + File.separatorChar + qqfile));
	// Files.move(pathOrigin, pathTargetFolderNormal,
	// StandardCopyOption.REPLACE_EXISTING);

	return pathTargetFolderNormal;
    }

    private static String buildImageStorePathNormal(final String qqfile, final Album album) {
	final String pathNormal = Play.application().configuration().getString("picture.path");
	return buildImageStorePath(qqfile, album, pathNormal);
    }

    private static String buildImageStorePath(final String qqfile, final Album album, final String path) {
	// return path + File.separatorChar + ALBUM_PATH + File.separatorChar +
	// album.id.toString() + "/" + qqfile;
	return path + File.separatorChar + ALBUM_PATH + File.separatorChar + album.id.toString() + File.separatorChar;

    }
}
