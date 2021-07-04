package com.crowd.funding.manager.dao;



import com.crowd.funding.bean.Dictionary;

import java.util.List;

public interface DictionaryMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Dictionary record);

    Dictionary selectByPrimaryKey(Integer id);

    List<Dictionary> selectAll();

    int updateByPrimaryKey(Dictionary record);
}
