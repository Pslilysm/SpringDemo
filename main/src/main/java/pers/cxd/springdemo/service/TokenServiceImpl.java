package pers.cxd.springdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.cxd.springdemo.bean.account.AccountInfo;
import pers.cxd.springdemo.mapper.TokenMapper;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private TokenMapper mTokenMapper;

//    private final Object mUUIDLock = new Object();

    @Override
    public String generateTokenWithAccount(AccountInfo accountInfo){
        String uuid;
//        synchronized (mUUIDLock){
            uuid = UUID.nameUUIDFromBytes(accountInfo.getAccountName().getBytes(StandardCharsets.UTF_8)).toString();
//        }
        mTokenMapper.insertTokenWithAccount(uuid, accountInfo.getId());
        return uuid;
    }

    @Override
    public int getAccountIdByToken(String token){
        return mTokenMapper.getAccountIdByToken(token);
    }

    @Override
    public String getTokenByAccountId(int accountId){
        return mTokenMapper.getTokenByAccountId(accountId);
    }

}
