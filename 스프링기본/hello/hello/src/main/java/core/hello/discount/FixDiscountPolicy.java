package core.hello.discount;

import core.hello.member.Grade;
import core.hello.member.Member;

public class FixDiscountPolicy implements DiscountPolicy{

    private int discountFixAmount = 1000;   //정량할인 - vip

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade()== Grade.VIP){
            return discountFixAmount;
        }
        else
            return 0;
    }
}
