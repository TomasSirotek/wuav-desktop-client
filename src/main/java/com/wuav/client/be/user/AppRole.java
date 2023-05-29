package com.wuav.client.be.user;

/**
 * Class for AppRole
 */
public class AppRole {

    private int id;
    private String name;

    /**
     * Constructor
     *
     * @param id   role id
     * @param name role name
     */
    public AppRole(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructor
     */
    public AppRole() {
    }

    /**
     * Method to get the role id
     *
     * @return role id
     */
    public int getId() {
        return id;
    }

    /**
     * Method to set the role id
     *
     * @param id role id
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method to get the role name
     *
     * @return role name
     */

    public String getName() {
        return name;
    }

    /**
     * Method to set the role name
     *
     * @param name role name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to get the role name
     *
     * @return role name
     */
    @Override
    public String toString() {
        return "AppRole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}