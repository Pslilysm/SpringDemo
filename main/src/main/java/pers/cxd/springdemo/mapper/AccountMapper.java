package pers.cxd.springdemo.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.dao.DuplicateKeyException;
import pers.cxd.springdemo.bean.account.AccountInfo;

import java.util.List;

/**
 * +-----------------+-------------+------+-----+-------------------+-------------------+
 * | Field           | Type        | Null | Key | Default           | Extra             |
 * +-----------------+-------------+------+-----+-------------------+-------------------+
 * | id              | int         | NO   | PRI | NULL              | auto_increment    |
 * | accountName     | varchar(32) | NO   | UNI | NULL              |                   |
 * | password        | varchar(32) | NO   |     | NULL              |                   |
 * | permissionFlags | int         | NO   |     | 0                 |                   |
 * | registerTime    | timestamp   | NO   |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
 * +-----------------+-------------+------+-----+-------------------+-------------------+
 */
@Mapper
public interface AccountMapper {

    /**
     * @throws DuplicateKeyException if this accountName already exits
     */
    @Insert("insert into account(accountName, password) values(#{accountName}, #{password})")
    void register(String accountName, String password) throws DuplicateKeyException;

    @Select("select * from account where accountName = #{accountName}")
    AccountInfo getUserInfoByAccountName(String accountName);

    @Select("select accountName, password from account_#{table_id}")
    List<AccountInfo> getAllUserInfoTemp(int table_id);

    @Update("create table account_#{table_id}(accountName varchar(32), password varchar(32));")
    int createNewAccountTable(int table_id);

}
