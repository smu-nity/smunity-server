package com.smunity.server.domain.member.mapper;

import com.smunity.server.domain.member.dto.MemberCountResponse;
import com.smunity.server.domain.member.dto.MemberInfoResponse;
import com.smunity.server.domain.member.dto.MemberResponse;
import com.smunity.server.global.common.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "department", source = "member.department.name")
    @Mapping(target = "deptCode", source = "member.department.code")
    @Mapping(target = "deptEditable", source = "member.department.editable")
    @Mapping(target = "secondDepartment", source = "member.secondDepartment.name")
    @Mapping(target = "secondDeptCode", source = "member.secondDepartment.code")
    @Mapping(target = "yearId", source = "member.year.id")
    MemberInfoResponse toResponse(Member member);

    @Mapping(target = "department", source = "member.department.name")
    @Mapping(target = "secondDepartment", source = "member.secondDepartment.name")
    @Mapping(target = "memberRole", source = "member.role")
    MemberResponse toMemberResponse(Member member);

    default Page<MemberResponse> toResponse(Page<Member> members) {
        return members.map(this::toMemberResponse);
    }

    default MemberCountResponse toResponse(long count) {
        return new MemberCountResponse(count);
    }
}
