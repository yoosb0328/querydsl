package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.repository.MemberJpaRepository;
import study.querydsl.repository.MemberRepository;
import study.querydsl.repository.MemberTestRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberRepository memberRepository;
    private final MemberTestRepository memberTestRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJpaRepository.search(condition);
    }

    @GetMapping("/v2/members")
    public Page<MemberTeamDto> searchMemberV2(MemberSearchCondition condition, Pageable pageable) {
        return memberRepository.searchPageSimple(condition, pageable);
    }

    @GetMapping("/v3/members")
    public Page<MemberTeamDto> searchMemberV3(MemberSearchCondition condition, Pageable pageable) {
        return memberRepository.searchPageComplex(condition, pageable);
    }

    @GetMapping("/sup/v1/members")
    public Page<MemberDto> searchPageByApplPage(MemberSearchCondition condition, Pageable pageable) {
        return memberTestRepository.searchPageByApplPage(condition, pageable);
    }
    @GetMapping("/sup/v2/members")
    public Page<MemberDto> applyPagination(MemberSearchCondition condition, Pageable pageable) {
        return memberTestRepository.applyPagination(condition, pageable);
    }
    @GetMapping("/sup/v3/members")
    public Page<MemberDto> applyPagination2(MemberSearchCondition condition, Pageable pageable) {
        return memberTestRepository.applyPagination2(condition, pageable);
    }
}
