package core.hello.discount;

import core.hello.member.Grade;
import core.hello.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10프로 할인 적용")
    void vip_o() {
        //given
        Member member = new Member(1L, "memverVIP", Grade.VIP);
        //when
        int discount = rateDiscountPolicy.discount(member,10000);
        //then
        assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("None VIP는 10프로 할인 적용X")
    void vip_x() {
        //given
        Member member = new Member(2L, "memverBASIC", Grade.BASIC);
        //when
        int discount = rateDiscountPolicy.discount(member,10000);
        //then
        assertThat(discount).isEqualTo(0);
    }
}