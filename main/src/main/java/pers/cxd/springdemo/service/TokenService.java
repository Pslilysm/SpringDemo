package pers.cxd.springdemo.service;

import pers.cxd.springdemo.bean.account.AccountInfo;

public interface TokenService {

    /**
     * Generate a random token and insert to table
     * @param accountInfo will bind the token
     * @return token generated
     */
    String generateTokenWithAccount(AccountInfo accountInfo);

    int getAccountIdByToken(String token);

    String getTokenByAccountId(int id);

}
