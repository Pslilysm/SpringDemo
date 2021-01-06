package pers.cxd.springdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.cxd.core.util.MD5Util;
import pers.cxd.springdemo.bean.account.AccountInfo;
import pers.cxd.springdemo.mapper.TokenMapper;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private TokenMapper mTokenMapper;

    @Override
    public String generateTokenWithAccount(AccountInfo accountInfo){
        String token = MD5Util.md5(UUID.randomUUID().toString());
        mTokenMapper.insertTokenWithAccount(token, accountInfo.getId());
        return token;
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
