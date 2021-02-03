package pers.cxd.springdemo.controller;

import android.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.cxd.springdemo.Version;
import pers.cxd.springdemo.bean.CommonResp;
import pers.cxd.springdemo.bean.ErrorResp;
import pers.cxd.springdemo.bean.account.AccountInfo;
import pers.cxd.springdemo.config.HttpCode;
import pers.cxd.springdemo.exception.http.HttpException;
import pers.cxd.springdemo.mapper.AccountMapper;
import pers.cxd.springdemo.service.TokenService;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(Version.NAME + "/account")
public class AccountController {

    private final String TAG = Log.TAG + this.getClass().getSimpleName();

    @Autowired
    private AccountMapper mAccountMapper;

    @Autowired
    private TokenService mTokenService;

    private final ScheduledExecutorService mExecutorService = Executors.newScheduledThreadPool(100);

    @RequestMapping("/login")
    public CommonResp<AccountInfo> login(@RequestParam("accountName") String accountName,
                                         @RequestParam("password") String password){
        Log.i(TAG, "login() called with: accountName = [" + accountName + "], password = [" + password + "]");
        Log.i(TAG, "login() called with: accountName = [" + accountName + "], password = [" + password + "]");
        AccountInfo accountInfo = mAccountMapper.getUserInfoByAccountName(accountName);
        for (int i = 1; i <= 30; i++) {
            final int k = i;
            mExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run: ready to run sql_" + k);
                    List<AccountInfo> accountInfoList = mAccountMapper.getAllUserInfoTemp(k);
//                    try {
//                        accountInfoList = DatabaseManager.getInstance().getAllUserInfoTemp(k);
//                    } catch (SQLException throwables) {
//                        Log.e(TAG, "run: ", throwables);
//                    }
                    Log.i(TAG, "run: run sql_" + k + " finish, size = " + accountInfoList.size());
                }
            }, 1, TimeUnit.SECONDS);
        }
        if (accountInfo != null){
            if (!accountInfo.getPassword().equals(password)){
                throw HttpException.create(HttpStatus.UNAUTHORIZED,
                        ErrorResp.create(HttpCode.Account.PASSWORD_INCORRECT, "password incorrect", null));
            }
            accountInfo.setToken(mTokenService.getTokenByAccountId(accountInfo.getId()));
            return CommonResp.create(HttpCode.Common.OK, "login success", accountInfo);
        }else {
            throw HttpException.create(HttpStatus.UNAUTHORIZED,
                    ErrorResp.create(HttpCode.Account.ACCOUNT_NOT_EXIST, "account not exits", null));
        }
    }

    @RequestMapping("/register")
    public CommonResp<AccountInfo> register(@RequestParam("accountName") String accountName,
                                            @RequestParam("password") String password){
        Log.i(TAG, "register() called with: accountName = [" + accountName + "], password = [" + password + "]");
        try {
            mAccountMapper.register(accountName, password);
            AccountInfo accountInfo = mAccountMapper.getUserInfoByAccountName(accountName);
            accountInfo.setToken(mTokenService.generateTokenWithAccount(accountInfo));
            return CommonResp.create(HttpCode.Common.OK, "register success", accountInfo);
        }catch (DuplicateKeyException ex){
            Log.e(TAG, "register: account " + accountName + " already exits");
            throw HttpException.create(HttpStatus.FORBIDDEN,
                    ErrorResp.create(HttpCode.Account.ACCOUNT_ALREADY_EXIST, "account already exits", null));
        }
    }

//    @RequestMapping("/setPermissionFlags")
//    public CommonResp<Void>  setPermissionFlags(@RequestParam("token") String token,
//                                                @RequestParam("permissionFlags") int permissionFlags){
//        int accountId = mTokenService.getAccountIdByToken(token);
//        if (accountId !)
//    }



}
