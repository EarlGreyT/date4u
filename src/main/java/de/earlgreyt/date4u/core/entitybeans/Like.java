package de.earlgreyt.date4u.core.entitybeans;

import javax.persistence.*;

@Entity
@Table(name = "LIKES")
public class Like {
    @EmbeddedId
    private LikeId id;

    @MapsId("likerFk")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LIKER_FK", nullable = false)
    private Profile likerFk;

    @MapsId("likeeFk")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LIKEE_FK", nullable = false)
    private Profile likeeFk;

    public LikeId getId() {
        return id;
    }

    public void setId(LikeId id) {
        this.id = id;
    }

    public Profile getLikerFk() {
        return likerFk;
    }

    public void setLikerFk(Profile likerFk) {
        this.likerFk = likerFk;
    }

    public Profile getLikeeFk() {
        return likeeFk;
    }

    public void setLikeeFk(Profile likeeFk) {
        this.likeeFk = likeeFk;
    }

}