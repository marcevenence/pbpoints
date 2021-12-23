package com.pbpoints.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Table(name = "`roster_events`")
@Subselect("select uuid() as id, hs.* from roster_events hs")
public class RosterEvent implements Serializable {

    @Id
    private String id;

    @Column(insertable = false, updatable = false, name = "event_id")
    private Long eventId;

    @Column(insertable = false, updatable = false, name = "event_name")
    private String eventName;

    @Column(insertable = false, updatable = false, name = "evcat_id")
    private Long evCatId;

    @Column(insertable = false, updatable = false, name = "category_name")
    private String categoryName;

    @Column(insertable = false, updatable = false, name = "team_id")
    private Long teamId;

    @Column(insertable = false, updatable = false, name = "team_name")
    private String teamName;

    @Lob
    @Column(insertable = false, updatable = false, name = "team_logo")
    private byte[] teamLogo;

    @Column(insertable = false, updatable = false, name = "team_logo_content_type")
    private String teamLogoContentType;

    @Column(insertable = false, updatable = false, name = "pbpointid")
    private Long pbPointId;

    @Column(insertable = false, updatable = false, name = "player_name")
    private String playerName;

    @Lob
    @Column(insertable = false, updatable = false, name = "player_picture")
    private byte[] playerPicture;

    @Column(insertable = false, updatable = false, name = "player_picture_content_type")
    private String playerPictureContentType;

    @Column(insertable = false, updatable = false, name = "player_category")
    private String playerCategory;

    @Column(insertable = false, updatable = false, name = "player_profile")
    private String playerProfile;

    @Column(insertable = false, updatable = false, name = "player_doc")
    private String playerDoc;

    public String getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public Long getEvCatId() {
        return evCatId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public byte[] getTeamLogo() {
        return teamLogo;
    }

    public String getTeamLogoContentType() {
        return teamLogoContentType;
    }

    public Long getPbPointId() {
        return pbPointId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public byte[] getPlayerPicture() {
        return playerPicture;
    }

    public String getPlayerPictureContentType() {
        return playerPictureContentType;
    }

    public String getPlayerCategory() {
        return playerCategory;
    }

    public String getPlayerProfile() {
        return playerProfile;
    }

    public String getPlayerDoc() {
        return playerDoc;
    }

    @Override
    public String toString() {
        return (
            "RosterEvent{" +
            "id=" +
            id +
            ", eventId=" +
            eventId +
            ", eventName='" +
            eventName +
            '\'' +
            ", evCatId=" +
            evCatId +
            ", categoryName='" +
            categoryName +
            '\'' +
            ", teamId='" +
            teamId +
            '\'' +
            ", teamName='" +
            teamName +
            '\'' +
            ", pbPointId=" +
            pbPointId +
            ", playerName='" +
            playerName +
            '\'' +
            ", playerCategory='" +
            playerCategory +
            '\'' +
            '}'
        );
    }
}
