package com.pbpoints.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Table(name = "`roster_events`")
@Subselect("select uuid() as id, hs.* from roster_events hs")
public class RosterEvent implements Serializable {

    @Id
    private String id;

    @Column(insertable = false, updatable = false, name = "tournament_id")
    private Long tournamentId;

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

    @Column(insertable = false, updatable = false, name = "anio")
    private Integer anio;

    @Lob
    @Column(insertable = false, updatable = false, name = "team_logo")
    private byte[] teamLogo;

    @Column(insertable = false, updatable = false, name = "team_logo_content_type")
    private String teamLogoContentType;

    @Column(insertable = false, updatable = false, name = "player_id")
    private Long playerId;

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

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
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

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
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
            ", tournamentId=" +
            tournamentId +
            ", eventId=" +
            eventId +
            ", eventName='" +
            eventName +
            ", anio='" +
            anio +
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
            ", playerId=" +
            playerId +
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
