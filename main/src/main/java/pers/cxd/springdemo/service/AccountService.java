package pers.cxd.springdemo.service;

import org.springframework.dao.DuplicateKeyException;
import pers.cxd.springdemo.bean.account.AccountInfo;


public interface AccountService {

    void registerAccount(String accountName, String password) throws DuplicateKeyException;

    AccountInfo getUserInfoByAccountName(String accountName);

    AccountInfo getUserInfoByAccountId(int accountId);

    AccountInfo getAccountInfoByToken(String token);

    /**
     * Generate a token by accountName and insert to table
     * @param accountInfo will bind the token
     * @return token generated
     */
    String generateTokenWithAccount(AccountInfo accountInfo);

    int getAccountIdByToken(String token);

    String getTokenByAccountId(int id);

}
