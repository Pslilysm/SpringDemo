package pers.cxd.springdemo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DuplicateKeyException;
import pers.cxd.springdemo.bean.account.AccountInfo;

@Mapper
public interface AccountMapper {

    @Insert("insert into account(accountName, password) values(#{accountName}, #{password})")
    void register(String accountName, String password) throws DuplicateKeyException;

    @Select("SELECT * FROM account WHERE accountName = #{accountName}")
    AccountInfo getUserInfoByAccountName(String accountName);

}
