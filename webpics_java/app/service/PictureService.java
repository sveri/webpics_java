package service;

import static org.imgscalr.Scalr.OP_ANTIALIAS;
import static org.imgscalr.Scalr.OP_BRIGHTER;
import static org.imgscalr.Scalr.resize;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import models.pic.Album;
import models.pic.Photo;

import org.imgscalr.Scalr.Method;

import play.Logger;
import play.Play;

public class PictureService {

    public static final String ALBUM_PATH = "album";

    public static final String BIG_PATH = "big";

    public static final String THUMBS_PATH = "thumbs";

    private static String buildImageStorePath(final String qqfile, final Album album, final String path) {
	// return path + File.separatorChar + ALBUM_PATH + File.separatorChar +
	// album.id.toString() + "/" + qqfile;
	return path + File.separatorChar + ALBUM_PATH + File.separatorChar + album.id.toString() + File.separatorChar;

    }

    private static String buildImageStorePathNormal(final String qqfile, final Album album) {
	final String pathNormal = Play.application().configuration().getString("picture.path");
	return buildImageStorePath(qqfile, album, pathNormal);
    }

    private static Path checkPreConditionsAndSaveToDisk(final File fileOrigin, final Album album, final String qqfile)
	    throws IOException, NullPointerException {

	final BufferedImage imageOriginal = ImageIO.read(fileOrigin);
	final BufferedImage thumbnail = createThumbnail(imageOriginal);
	final BufferedImage normal = createNormalImage(imageOriginal);
	final BufferedImage imageBig = createBig(imageOriginal);

	// final Path pathOrigin = Paths.get(fileOrigin.getAbsolutePath());

	if (album == null) {
	    Logger.error("Album does not exist, exiting.");
	    throw new NullPointerException();
	}

	final Path pathTargetFolderNormal = Paths.get(buildImageStorePathNormal(qqfile, album));
	final Path pathTargetFolderBig = Paths.get(pathTargetFolderNormal + File.separator + "big");
	final Path pathTargetFolderThumb = Paths.get(pathTargetFolderNormal + File.separator + "thumbs");

	if (!Files.exists(pathTargetFolderNormal)) {
	    Files.createDirectories(pathTargetFolderNormal);
	    Files.createDirectories(pathTargetFolderBig);
	    Files.createDirectories(pathTargetFolderThumb);
	}

	final File output = new File(pathTargetFolderBig.toString() + File.separatorChar + qqfile);
	output.createNewFile();
	ImageIO.write(imageBig, "jpg", output);
	ImageIO.write(thumbnail, "jpg", new File(pathTargetFolderThumb.toString() + File.separatorChar + qqfile));
	ImageIO.write(normal, "jpg", new File(pathTargetFolderNormal.toString() + File.separatorChar + qqfile));

	return pathTargetFolderNormal;
    }

    public static BufferedImage createBig(final BufferedImage img) {
	// Create quickly, then smooth and brighten it.
	return resize(img, Method.SPEED, 1920, OP_ANTIALIAS, OP_BRIGHTER);
    }

    public static BufferedImage createNormalImage(final BufferedImage img) {
	// Create quickly, then smooth and brighten it.
	return resize(img, Method.SPEED, 700, OP_ANTIALIAS, OP_BRIGHTER);
    }

    public static BufferedImage createThumbnail(final BufferedImage img) {
	// Create quickly, then smooth and brighten it.
	return resize(img, Method.SPEED, 60, OP_ANTIALIAS, OP_BRIGHTER);
    }

    private static boolean extractEntry(final ZipFile zf, final ZipEntry entry, final Long albumId) throws IOException {
	File fileOutput = null;

	if (!entry.isDirectory()) {
	    InputStream is = null;
	    OutputStream os = null;
	    final byte buf[] = new byte[1024];
	    int len;

	    try {
		fileOutput = Files.createTempFile(entry.getName(), "webpics").toFile();
		is = zf.getInputStream(entry);
		os = new FileOutputStream(fileOutput);

		while ((len = is.read(buf)) > 0) {
		    os.write(buf, 0, len);
		}

		if (os != null) {
		    os.close();
		}
		if (is != null) {
		    is.close();
		}
	    } catch (final IOException e) {
		Logger.error("Error extracting Zip File.", e);
		throw new IOException();
	    }
	}

	return storePicture(fileOutput, albumId, entry.getName());
    }

    private static void storePhotoInDb(final String qqfile, final Album album, final Path pathTargetFile) {
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

    public static boolean storePicture(final File fileOrigin, final Long albumId, final String qqfile) {

	final Album album = Album.find.byId(albumId);
	final Path pathTargetFile;

	try {
	    pathTargetFile = checkPreConditionsAndSaveToDisk(fileOrigin, album, qqfile);
	} catch (IOException | NullPointerException e) {
	    Logger.error("Error creating target folder or moving file, or maybe album does not exist.", e);
	    return false;
	}

	storePhotoInDb(qqfile, album, pathTargetFile);

	return true;
    }

    public static boolean storeZip(final File fileOrigin, final Long albumId) {

	try {
	    final ZipFile zf = new ZipFile(fileOrigin);
	    for (final ZipEntry entry : Collections.list(zf.entries())) {
		extractEntry(zf, entry, albumId);
	    }
	} catch (final IOException e) {
	    Logger.error("Error extracting zip file: " + fileOrigin.getName(), e);
	    return false;
	}

	return true;
    }
}
