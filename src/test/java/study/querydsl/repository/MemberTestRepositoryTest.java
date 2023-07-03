package study.querydsl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberTestRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberTestRepository memberTestRepository;

    @Test
    public void basicSelect() {
        Member member1 = new Member("member1", 1, null);
        em.persist(member1);

        List<Member> members = memberTestRepository.basicSelect();
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void basicSelectFrom() {
        Member member1 = new Member("member1", 1, null);
        em.persist(member1);

        List<Member> members = memberTestRepository.basicSelectFrom();
        assertThat(members.size()).isEqualTo(1);
    }
}