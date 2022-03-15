package core.hello.member;

import core.hello.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    AppConfig appConfig = new AppConfig();
    MemberService memberService = appConfig.memberService();

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