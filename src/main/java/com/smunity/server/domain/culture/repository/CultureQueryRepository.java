package com.smunity.server.domain.culture.repository;

import com.smunity.server.domain.culture.entity.Culture;
import com.smunity.server.global.common.entity.enums.SubDomain;

import java.util.List;

public interface CultureQueryRepository {

    List<Culture> findBySubDomain(SubDomain subDomain);
}
