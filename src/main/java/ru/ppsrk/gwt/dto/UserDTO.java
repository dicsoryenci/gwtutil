package ru.ppsrk.gwt.dto;

import java.io.Serializable;
import java.util.Set;

import ru.ppsrk.gwt.client.Hierarchic;


public class UserDTO extends Hierarchic implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6557702564218055053L;
    Long id;
    String username;
    String password;
    String salt;
    Set<RoleDTO> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public Hierarchic getParent() {
        // TODO Auto-generated method stub
        return null;
    }

}