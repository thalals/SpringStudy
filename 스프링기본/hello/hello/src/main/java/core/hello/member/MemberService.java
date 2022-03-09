package core.hello.member;

//역할
public interface MemberService {
    void join(Member member);
    Member findMember(Long memberId);
}
