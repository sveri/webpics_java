package models.pic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Photo extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @Required
    public String name;

    @Required
    @Column(unique = true)
    public String path;

    @Required
    @ManyToOne
    public Album albumId;

    public static Finder<Long, Photo> find = new Finder<Long, Photo>(
	    Long.class, Photo.class);

    public static boolean photoExists(String path) {
	return find.where().ilike("path", path).findRowCount() > 0;
    }

    public static Photo findPhotoByPath(String path) {
	return find.where().ilike("path", path).findUnique();
    }

}
