package pers.cxd.springdemo.config;

public interface HttpCode {

    interface Common {

        int OK = 200;

    }

    interface Account {

        int PASSWORD_INCORRECT = 100;
        int ACCOUNT_NOT_EXIST = 101;
        int ACCOUNT_ALREADY_EXIST = 102;

    }

}
