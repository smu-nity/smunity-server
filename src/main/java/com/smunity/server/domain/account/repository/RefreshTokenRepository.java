package com.smunity.server.domain.account.repository;

import com.smunity.server.domain.account.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
