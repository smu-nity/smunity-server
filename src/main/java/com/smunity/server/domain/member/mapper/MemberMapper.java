package com.smunity.server.domain.member.mapper;

import com.smunity.server.domain.member.dto.MemberCountDto;
import com.smunity.server.domain.member.dto.MemberInfoResponseDto;
import com.smunity.server.domain.member.dto.MemberResponseDto;
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
    MemberInfoResponseDto toDto(Member member);

    @Mapping(target = "department", source = "member.department.name")
    @Mapping(target = "memberRole", source = "member.role")
    MemberResponseDto toResponse(Member member);

    default Page<MemberResponseDto> toResponse(Page<Member> memberPage) {
        return memberPage.map(this::toResponse);
    }

    default MemberCountDto toDto(long count) {
        return new MemberCountDto(count);
    }
}
