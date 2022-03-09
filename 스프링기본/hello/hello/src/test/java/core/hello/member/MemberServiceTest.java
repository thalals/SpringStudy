package core.hello.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    MemberService memberService = new MemberServiceImpl();

    @Test
    void join() {
        //given
        Member member = new Member(1L,"memberA",Grade.VIP);
        
        //when
        memberService.join(member);
        Member findmember = memberService.findMember(1L);
        
        //then
        Assertions.assertThat(member).isEqualTo(findmember);
    }
}