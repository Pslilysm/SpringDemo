package pers.cxd.springdemo.controllers;

import android.util.Log;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.cxd.springdemo.Version;
import pers.cxd.springdemo.bean.CommonResult;
import pers.cxd.springdemo.bean.account.UserInfoResp;

@RestController
@RequestMapping(Version.NAME + "/account")
public class AccountController {

    private final String TAG = Log.TAG + this.getClass().getSimpleName();

    private static CommonResult<UserInfoResp> s = new CommonResult<>(200,"login success", new UserInfoResp(1, "aagwagagw", 15));

    @RequestMapping("/login")
    public CommonResult<UserInfoResp> login(@RequestParam("accountName") String accountName,
                                            @RequestParam("password") String password){
        Log.i(TAG, "login() called with: accountName = [" + accountName + "], password = [" + password + "]");
        return new CommonResult<>(200,"login success", new UserInfoResp(1, "aagwagagw", 15));
    }

    @RequestMapping("/register")
    public CommonResult<UserInfoResp> register(@RequestParam("accountName") String accountName,
                                            @RequestParam("password") String password){
        Log.i(TAG, "register() called with: accountName = [" + accountName + "], password = [" + password + "]");
        return new CommonResult<>(200,"register success", new UserInfoResp(1, "aagwagagw", 15));
    }

    @RequestMapping("/logout")
    public CommonResult<Void> logout(@RequestParam("accountName") String accountName){
        Log.i(TAG, "logout() called with: accountName = [" + accountName + "]");
        return new CommonResult<>(200,"logout success", null);
    }

}
