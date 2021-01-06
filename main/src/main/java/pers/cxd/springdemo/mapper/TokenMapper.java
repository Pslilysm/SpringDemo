package pers.cxd.springdemo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * +-----------+-------------+------+-----+---------+-------+
 * | Field     | Type        | Null | Key | Default | Extra |
 * +-----------+-------------+------+-----+---------+-------+
 * | token     | varchar(32) | NO   |     | NULL    |       |
 * | accountId | int         | NO   |     | NULL    |       |
 * +-----------+-------------+------+-----+---------+-------+
 */
@Mapper
public interface TokenMapper {

    @Insert("insert into token(token, accountId) values(#{token}, #{accountId})")
    void insertTokenWithAccount(String token, int accountId);

    @Select("select id from token where token = #{token}")
    int getAccountIdByToken(String token);

    @Select("select token from token where accountId = #{accountId}")
    String getTokenByAccountId(int accountId);

}
