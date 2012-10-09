package models.pic;

import javax.persistence.Entity;
import javax.persistence.Id;

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
    public String path;

    @Required
    public Album albumId;

    public static Finder<Long, Photo> find = new Finder<Long, Photo>(
	    Long.class, Photo.class);

}
