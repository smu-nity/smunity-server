package com.smunity.server.domain.account.entity;

import com.smunity.server.global.common.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "account_login_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginStatus {

    @Id
    @Column(name = "login_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime loginAt;

    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    public void setMember(Member member) {
        this.member = member;
    }
}
