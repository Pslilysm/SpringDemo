package pers.cxd.springdemo.controller;

import android.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.cxd.springdemo.Version;
import pers.cxd.springdemo.bean.CommonResp;
import pers.cxd.springdemo.bean.account.AccountInfo;
import pers.cxd.springdemo.config.HttpCode;
import pers.cxd.springdemo.mapper.AccountMapper;
import pers.cxd.springdemo.service.AccountService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RestController
@RequestMapping(Version.NAME + "/account")
public class AccountController {

    private final String TAG = this.getClass().getSimpleName();

    @Autowired
    private AccountService mAccountService;

    private final ScheduledExecutorService mExecutorService = Executors.newScheduledThreadPool(100);

    @RequestMapping("/login")
    public CommonResp<AccountInfo> login(@RequestParam("accountName") String accountName,
                                         @RequestParam("password") String password){
        Log.i(TAG, "login() called with: accountName = [" + accountName + "], password = [" + password + "]");
        AccountInfo accountInfo = mAccountService.getUserInfoByAccountName(accountName);
//        for (int i = 1; i <= 30; i++) {
//            final int k = i;
//            mExecutorService.scheduleAtFixedRate(new Runnable() {
//                @Override
//                public void run() {
//                    Log.i(TAG, "run: ready to run sql_" + k);
//                    List<AccountInfo> accountInfoList = mAccountMapper.getAllUserInfoTemp(k);
//                    Log.i(TAG, "run: run sql_" + k + " finish, size = " + accountInfoList.size());
//                }
//            }, 0, 2, TimeUnit.MILLISECONDS);
//        }
        if (accountInfo != null){
            if (!accountInfo.getPassword().equals(password)){
                return CommonResp.create(HttpCode.Account.PASSWORD_INCORRECT, "password incorrect", null);
            }
            accountInfo.setToken(mAccountService.getTokenByAccountId(accountInfo.getId()));
            return CommonResp.createWithOk("login success", accountInfo);
        }else {
            return CommonResp.create(HttpCode.Account.ACCOUNT_NOT_EXIST, "account not exits", null);
        }
    }

    @RequestMapping("/register")
    public CommonResp<AccountInfo> register(@RequestParam("accountName") String accountName,
                                            @RequestParam("password") String password){
        Log.d(TAG, "register() called with: accountName = [" + accountName + "], password = [" + password + "]");
        try {
            mAccountService.registerAccount(accountName, password);
            AccountInfo accountInfo = mAccountService.getUserInfoByAccountName(accountName);
            accountInfo.setToken(mAccountService.generateTokenWithAccount(accountInfo));
            return CommonResp.createWithOk("register success", accountInfo);
        }catch (DuplicateKeyException ex){
            Log.e(TAG, "register: account " + accountName + " already exits");
            return CommonResp.create(HttpCode.Account.ACCOUNT_ALREADY_EXIST, "account already exits", null);
        }
    }

}
