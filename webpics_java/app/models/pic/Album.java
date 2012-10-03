package models.pic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import models.SecurityRole;
import play.db.ebean.Model;

@Entity
public class Album extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String name;

    @ManyToMany
    private List<SecurityRole> roles;

    @ManyToMany
    private List<Photo> photos;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<SecurityRole> getRoles() {
	return new ArrayList<>(roles);
    }

    public void setRoles(List<SecurityRole> roles) {
	this.roles = new ArrayList<>(roles);
    }

    public List<Photo> getPhotos() {
	return new ArrayList<>(photos);
    }

    public void setPhotos(List<Photo> photos) {
	this.photos = new ArrayList<>(photos);
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

}
