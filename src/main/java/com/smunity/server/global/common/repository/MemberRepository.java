package com.smunity.server.global.common.repository;

import com.smunity.server.global.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
