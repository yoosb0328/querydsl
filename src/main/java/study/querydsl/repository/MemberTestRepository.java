package study.querydsl.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.QMemberDto;
import study.querydsl.entity.Member;
import study.querydsl.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@Repository
public class MemberTestRepository extends QuerydslRepositorySupport {

    public MemberTestRepository() {
        super(Member.class);
    }

    public List<Member> basicSelect() {
        return select(member)
                .from(member)
                .fetch();
    }

    public List<Member> basicSelectFrom() {
        return selectFrom(member)
                .fetch();
    }
    /*
    참고 : Sort조건은 pageable에 포함해서 던지면 됨. 별도로 orderBy에 포함할 필요 없음.
     */
    public Page<MemberDto> searchPageByApplPage(MemberSearchCondition condition, Pageable pageable){
        JPAQuery<MemberDto> query = select(new QMemberDto(
                member.username,
                member.age
        ))
                .from(member)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );
        List<MemberDto> content = getQuerydsl().applyPagination(pageable, query)
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> query.fetchCount());
    }

    public Page<MemberDto> applyPagination(MemberSearchCondition condition, Pageable pageable) {
        //searchPageByApplPage()를 더 최적화.
        Page<MemberDto> results = applyPagination(pageable, query -> query
                .select(new QMemberDto(
                        member.username,
                        member.age
                ))
                .from(member)
                .leftJoin(member.team, team)
                        .where(usernameEq(condition.getUsername()),
                                teamNameEq(condition.getTeamName()),
                                ageGoe(condition.getAgeGoe()),
                                ageLoe(condition.getAgeLoe())
                        )
        );

        return results;
    }

    public Page<MemberDto> applyPagination2(MemberSearchCondition condition, Pageable pageable) {
        // countQuery 직접 설정
        Page<MemberDto> results = applyPagination(pageable, contentQuery -> contentQuery
                .select(new QMemberDto(
                        member.username,
                        member.age
                        ))
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                ), countQuery -> countQuery
                .select(member.id)
                .from(member)
                .leftJoin(member.team, team)
                .where(usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
        );

        return results;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression usernameEq(String username) {
        return hasText(username) ? member.username.eq(username) : null;
    }
}
