package study.querydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "username", "age"})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    /*
    설계 단계에서 모든 경우의 생성자를 미리 만들어 둘 필요는 없다. 필요할 때 만들면 됨.
    여기서는 예제에서 필요해서 만들었다.
     */
    public Member(String username, int age) {
        this(username, age, null);
    }

    public Member(String username) {
        this(username, 0, null);
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
