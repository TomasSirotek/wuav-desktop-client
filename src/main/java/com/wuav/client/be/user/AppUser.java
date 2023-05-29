package com.wuav.client.be.user;

import com.wuav.client.be.Project;

import java.util.Date;
import java.util.List;


/**
 * Class for AppUser
 */
public class AppUser {

    private int id;
    private String name;
    private String email;

    private String passwordHash;
    private Date createdAt;

    private List<AppRole> roles;

    private List<Project> projects;

    /**
     * Method to get the user id
     *
     * @return user id
     */
    public int getId() {
        return id;
    }

    /**
     * Method to set the user id
     *
     * @param id user id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method to get the name
     *
     * @return name id
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to get the email
     *
     * @return String user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to set the email
     *
     * @param email user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Method to get the password hash
     *
     * @return String password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Method to set the password hash
     *
     * @param passwordHash password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Method to get the roles
     *
     * @return List of roles
     */
    public List<AppRole> getRoles() {
        return roles;
    }

    /**
     * Method to set the roles
     *
     * @param roles List of roles
     */
    public void setRoles(List<AppRole> roles) {
        this.roles = roles;
    }


    /**
     * Method to get the created at date
     *
     * @return Date created at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Method to set the created at date
     *
     * @param createdAt Date created at
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Method to get the projects
     *
     * @return List of projects
     */

    public List<Project> getProjects() {
        return projects;
    }

    /**
     * Method to set the projects
     *
     * @param projects List of projects
     */
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }


    /**
     * Method to get the string representation of the object
     *
     * @return
     */
    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", createdAt=" + createdAt +
                ", roles=" + roles +
                ", projects=" + projects +
                '}';
    }
}