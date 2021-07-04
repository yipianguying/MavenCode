package com.crowd.funding.manager.dao;



import com.crowd.funding.bean.AccountTypeCert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface AccountTypeCertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountTypeCert record);

    AccountTypeCert selectByPrimaryKey(Integer id);

    List<AccountTypeCert> selectAll();

    int updateByPrimaryKey(AccountTypeCert record);

    List<Map<String, Object>> queryCertAccttype();

    int deleteAcctTypeCert(Map<String, Object> paramMap);

    int insertAcctTypeCert(Map<String, Object> paramMap);
}