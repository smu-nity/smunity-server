package com.smunity.server.domain.major.repository;

import com.smunity.server.domain.major.entity.Major;
import com.smunity.server.global.common.entity.Member;
import com.smunity.server.global.common.entity.enums.Category;

import java.util.List;

public interface MajorQueryRepository {

    List<Major> findByMemberAndCategory(Member member, Category category);
}
