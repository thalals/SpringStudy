package core.hello.member;

//인터페이스 - 역할 (다형성)
public interface MemberRepository {
    void save(Member member);

    Member findById(Long memberId);
}
