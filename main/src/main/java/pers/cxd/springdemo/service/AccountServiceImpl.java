package pers.cxd.springdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import pers.cxd.springdemo.bean.account.AccountInfo;
import pers.cxd.springdemo.mapper.AccountMapper;
import pers.cxd.springdemo.mapper.TokenMapper;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountMapper mAccountMapper;

    @Autowired
    TokenMapper mTokenMapper;

    @Override
    public void registerAccount(String accountName, String password) throws DuplicateKeyException {
        mAccountMapper.register(accountName, password);
    }

    @Override
    public AccountInfo getUserInfoByAccountName(String accountName) {
        return mAccountMapper.getUserInfoByAccountName(accountName);
    }

    @Override
    public AccountInfo getUserInfoByAccountId(int accountId) {
        return mAccountMapper.getUserInfoByAccountId(accountId);
    }

    @Override
    public AccountInfo getAccountInfoByToken(String token) {
        return getUserInfoByAccountId(getAccountIdByToken(token));
    }

    @Override
    public String generateTokenWithAccount(AccountInfo accountInfo) {
        String uuid = UUID.nameUUIDFromBytes(accountInfo.getAccountName().getBytes(StandardCharsets.UTF_8)).toString();
        mTokenMapper.insertTokenWithAccount(uuid, accountInfo.getId());
        return uuid;
    }

    @Override
    public int getAccountIdByToken(String token) {
        return mTokenMapper.getAccountIdByToken(token);
    }

    @Override
    public String getTokenByAccountId(int id) {
        return mTokenMapper.getTokenByAccountId(id);
    }
}
