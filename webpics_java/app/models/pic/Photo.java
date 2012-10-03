package models.pic;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Photo extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String name;

    private String path;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

}
