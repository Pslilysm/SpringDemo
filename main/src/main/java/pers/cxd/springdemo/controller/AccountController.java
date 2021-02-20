package pers.cxd.springdemo.controller;

import android.util.Log;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import pers.cxd.springdemo.util.refrection.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(Version.NAME + "/account")
public class AccountController {

    private final String TAG = this.getClass().getSimpleName();

    @Autowired
    private AccountMapper mAccountMapper;

    @Autowired
    private TokenService mTokenService;

    private final ScheduledExecutorService mExecutorService = Executors.newScheduledThreadPool(100);

    @RequestMapping("/login")
    public CommonResp<AccountInfo> login(@RequestParam("accountName") String accountName,
                                         @RequestParam("password") String password){
        Log.i(TAG, "login() called with: accountName = [" + accountName + "], password = [" + password + "]");
        AccountInfo accountInfo = mAccountMapper.getUserInfoByAccountName(accountName);
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
            accountInfo.setToken(mTokenService.getTokenByAccountId(accountInfo.getId()));
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
            mAccountMapper.register(accountName, password);
            AccountInfo accountInfo = mAccountMapper.getUserInfoByAccountName(accountName);
            accountInfo.setToken(mTokenService.generateTokenWithAccount(accountInfo));
            return CommonResp.createWithOk("register success", accountInfo);
        }catch (DuplicateKeyException ex){
            Log.e(TAG, "register: account " + accountName + " already exits");
            return CommonResp.create(HttpCode.Account.ACCOUNT_ALREADY_EXIST, "account already exits", null);
        }
    }

}
