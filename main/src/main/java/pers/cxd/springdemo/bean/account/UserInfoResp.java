package pers.cxd.springdemo.bean.account;

public class UserInfoResp {

    int id;
    String token;
    int permissionFlags;

    public UserInfoResp(int id, String token, int permissionFlags) {
        this.id = id;
        this.token = token;
        this.permissionFlags = permissionFlags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPermissionFlags() {
        return permissionFlags;
    }

    public void setPermissionFlags(int permissionFlags) {
        this.permissionFlags = permissionFlags;
    }
}
