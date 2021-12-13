package com.slcollege.educenter.mapper;

import com.slcollege.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author 一片孤影
 * @since 2021-02-22
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    // 查询某一天的注册人数-用于生成统计图表
    Integer registerNumberDay(String day);
}
