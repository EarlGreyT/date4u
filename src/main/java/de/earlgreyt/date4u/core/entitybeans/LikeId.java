package de.earlgreyt.date4u.core.entitybeans;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LikeId implements Serializable {
    private static final long serialVersionUID = 4113696590874158345L;
    @Column(name = "LIKER_FK", nullable = false)
    private Long likerFk;

    @Column(name = "LIKEE_FK", nullable = false)
    private Long likeeFk;

    public Long getLikerFk() {
        return likerFk;
    }

    public void setLikerFk(Long likerFk) {
        this.likerFk = likerFk;
    }

    public Long getLikeeFk() {
        return likeeFk;
    }

    public void setLikeeFk(Long likeeFk) {
        this.likeeFk = likeeFk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LikeId entity = (LikeId) o;
        return Objects.equals(this.likerFk, entity.likerFk) &&
                Objects.equals(this.likeeFk, entity.likeeFk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(likerFk, likeeFk);
    }

}