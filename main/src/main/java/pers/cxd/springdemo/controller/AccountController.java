package pers.cxd.springdemo.controller;

import android.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import pers.cxd.springdemo.Version;
import pers.cxd.springdemo.bean.CommonResp;
import pers.cxd.springdemo.bean.account.AccountInfo;
import pers.cxd.springdemo.config.HttpCode;
import pers.cxd.springdemo.service.TokenService;
import pers.cxd.springdemo.mapper.AccountMapper;

@RestController
@RequestMapping(Version.NAME + "/account")
public class AccountController {

    private final String TAG = Log.TAG + this.getClass().getSimpleName();

    @Autowired
    private AccountMapper mAccountMapper;

    @Autowired
    private TokenService mTokenService;

    @RequestMapping("/login")
    public CommonResp<AccountInfo> login(@RequestParam("accountName") String accountName,
                                         @RequestParam("password") String password){
        Log.i(TAG, "login() called with: accountName = [" + accountName + "], password = [" + password + "]");
        AccountInfo accountInfo = mAccountMapper.getUserInfoByAccountName(accountName);
        if (accountInfo != null){
            if (!accountInfo.getPassword().equals(password)){
                throw HttpClientErrorException.create("password incorrect",
                        HttpStatus.FORBIDDEN, "",
                        new HttpHeaders(),
                        null, null);
            }
            accountInfo.setToken(mTokenService.getTokenByAccountId(accountInfo.getId()));
            return new CommonResp<>(HttpCode.OK, "login success", accountInfo);
        }else {
            throw HttpClientErrorException.create("account not exits",
                    HttpStatus.FORBIDDEN, "",
                    new HttpHeaders(),
                    null, null);
        }
    }

    @RequestMapping("/register")
    public CommonResp<AccountInfo> register(@RequestParam("accountName") String accountName,
                                            @RequestParam("password") String password){
        Log.i(TAG, "register() called with: accountName = [" + accountName + "], password = [" + password + "]");
        try {
            mAccountMapper.register(accountName, password);
            AccountInfo accountInfo = mAccountMapper.getUserInfoByAccountName(accountName);
            String token = mTokenService.generateTokenWithAccount(accountInfo);
            accountInfo.setToken(token);
            return new CommonResp<>(HttpCode.OK, "register success", accountInfo);
        }catch (DuplicateKeyException ex){
            Log.e(TAG, "register: account " + accountName + " already exits");
            throw HttpClientErrorException.create("account already exits",
                    HttpStatus.FORBIDDEN, "",
                    new HttpHeaders(),
                    null, null);
        }
    }



}
