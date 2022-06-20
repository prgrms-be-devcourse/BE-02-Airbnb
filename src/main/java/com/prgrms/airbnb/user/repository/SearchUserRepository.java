package com.prgrms.airbnb.user.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchUserRepository implements UserCustomRepository {

  private final JPAQueryFactory queryFactory;


}
