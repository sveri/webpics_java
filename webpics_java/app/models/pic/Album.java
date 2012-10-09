package models.pic;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import models.SecurityRole;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Album extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @Required
    public String name;

    @ManyToMany
    public List<SecurityRole> roles;

    @ManyToMany
    public List<Photo> photos;

    public static Finder<Long, Album> find = new Finder<Long, Album>(
	    Long.class, Album.class);

}
