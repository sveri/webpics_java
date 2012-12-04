package models.pic;

import java.io.File;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import service.PictureService;

@Entity
public class Photo extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@Required
	public String name;

	@Required
	public String pathNormal;
	//
	// @Required
	// @Column(unique = true)
	// public String pathBig;
	//
	// @Required
	// @Column(unique = true)
	// public String pathThumb;

	@Required
	@ManyToOne
	public Album albumId;

	public static Finder<Long, Photo> find = new Finder<Long, Photo>(
			Long.class, Photo.class);

	// public static boolean photoExists(String path) {
	// return find.where().ilike("path", path).ilike("name", qq)findRowCount() >
	// 0;
	// }

	public static Photo findPhotoByPath(String pathNormal, String qqfile) {
		return find.where().ilike("path_normal", pathNormal)
				.ilike("name", qqfile).findUnique();
	}

	public static List<Photo> findPhotosByAlbumId(Long albumId) {
		return find.where().eq("album_id_id", albumId).findList();
	}

	public String getPathThumb() {
		return pathNormal + File.separator + PictureService.THUMBS_PATH;
	}

	public String getPathBig() {
		return pathNormal + File.separator + PictureService.BIG_PATH;
	}

}
