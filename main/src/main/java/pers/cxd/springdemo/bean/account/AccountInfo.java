package pers.cxd.springdemo.bean.account;

public class AccountInfo {

    int id;
    String accountName;
    String password;
    int permissionFlags;
    String registerTime;

    String token;

    public AccountInfo(int id, String accountName, String password, int permissionFlags, String registerTime) {
        this.id = id;
        this.accountName = accountName;
        this.password = password;
        this.permissionFlags = permissionFlags;
        this.registerTime = registerTime;
    }

    public int getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getPassword() {
        return password;
    }

    public int getPermissionFlags() {
        return permissionFlags;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
