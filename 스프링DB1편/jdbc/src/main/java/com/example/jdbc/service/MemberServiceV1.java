package com.example.jdbc.service;

import com.example.jdbc.domain.Member;
import com.example.jdbc.repository.MemberRepositoryV1;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepositoryV1;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepositoryV1.findById(fromId);
        Member toMember = memberRepositoryV1.findById(toId);

        memberRepositoryV1.update(fromId, fromMember.getMoney() - money);

        validation(toMember);
        memberRepositoryV1.update(toId, toMember.getMoney() - money);


    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("error");
        }
    }
}
