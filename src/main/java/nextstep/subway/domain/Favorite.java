package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.exception.FavoriteIsNotYoursException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long sourceId;

    private Long targetId;

    private Long memberId;

    public Favorite(final Long sourceId, final Long targetId, final Long memberId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.memberId = memberId;
    }

    public void validateRemove(final Member member) {
        if (!memberId.equals(member.getId())) {
            throw new FavoriteIsNotYoursException();
        }
    }
}