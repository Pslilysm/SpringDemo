package pers.cxd.springdemo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DuplicateKeyException;
import pers.cxd.springdemo.bean.account.AccountInfo;

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

    @Insert("insert into account(accountName, password) values(#{accountName}, #{password})")
    void register(String accountName, String password) throws DuplicateKeyException;

    @Select("SELECT * FROM account WHERE accountName = #{accountName}")
    AccountInfo getUserInfoByAccountName(String accountName);

}
