package hello.hellospring.domain;

import jakarta.persistence.*;

@Entity // 이제부터 이거는 JPA가 관리하는 엔티티.
public class Member  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //@Column(name="username") //만약에 DB의 Colume이 username이면 애노테이션으로 매핑
    //이렇게 애노테이션을 가지로 DB랑 매핑
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
