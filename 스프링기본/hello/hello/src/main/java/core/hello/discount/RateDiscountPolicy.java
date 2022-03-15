package core.hello.discount;

import core.hello.member.Grade;
import core.hello.member.Member;

public class RateDiscountPolicy  implements DiscountPolicy{

    private int discountPercent = 10; //10프로 정률 할인

    @Override
    public int discount(Member member, int price) {

        if(member.getGrade()== Grade.VIP){
            return price*discountPercent/100;
        }
        else
            return 0;
    }
}
