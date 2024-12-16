package com.smunity.server.domain.account.entity;

import com.smunity.server.global.common.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "account_login_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginStatus extends TimeEntity {

    @Id
    @Column(name = "login_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    public void setMember(Member member) {
        this.member = member;
    }
}
