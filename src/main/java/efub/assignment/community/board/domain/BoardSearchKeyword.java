package efub.assignment.community.board.domain;

import efub.assignment.community.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardSearchKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String keyword;

    @Builder
    public BoardSearchKeyword(String keyword) {
        if(keyword == null){
            throw new IllegalArgumentException("키워드가 null이면 안됩니다.");
        }
        if(keyword.isEmpty()){
            throw new IllegalArgumentException("키워드가 빈문자열이면 안됩니다");
        }
    }
}
