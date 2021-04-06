package com.pbpoints.service.dto.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "GAME")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDTO {

    @JacksonXmlProperty(localName = "ID")
    @NotNull
    private Long id;

    @JacksonXmlProperty(localName = "SD_ID")
    @NotNull
    private Integer splitDeckNum;

    @JacksonXmlProperty(localName = "CLASIF")
    @NotNull
    private String clasification;

    @JacksonXmlProperty(localName = "TEAM_A")
    @NotBlank
    private String teamA;

    @JacksonXmlProperty(localName = "POINTS_A")
    @NotNull
    private Integer pointsA;

    @JacksonXmlProperty(localName = "OVERTIME_A")
    @NotNull
    private Integer overtimeA;

    @JacksonXmlProperty(localName = "UVU_A")
    @NotNull
    private Integer uvuA;

    @JacksonXmlProperty(localName = "TEAM_B")
    @NotBlank
    private String teamB;

    @JacksonXmlProperty(localName = "POINTS_B")
    private Integer pointsB;

    @JacksonXmlProperty(localName = "OVERTIME_B")
    private Integer overtimeB;

    @JacksonXmlProperty(localName = "UVU_B")
    @NotNull
    private Integer uvuB;

    @JacksonXmlProperty(localName = "TIME_LEFT")
    @NotNull
    private Integer timeLeft;

    @JacksonXmlProperty(localName = "GROUP")
    @NotNull
    private Integer group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSplitDeckNum() {
        return splitDeckNum;
    }

    public void setSplitDeckNum(Integer splitDeckNum) {
        this.splitDeckNum = splitDeckNum;
    }

    public String getClasification() {
        return clasification;
    }

    public void setClasification(String clasification) {
        this.clasification = clasification;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public Integer getPointsA() {
        return pointsA;
    }

    public void setPointsA(Integer pointsA) {
        this.pointsA = pointsA;
    }

    public Integer getOvertimeA() {
        return overtimeA;
    }

    public void setOvertimeA(Integer overtimeA) {
        this.overtimeA = overtimeA;
    }

    public Integer getUvuA() {
        return uvuA;
    }

    public void setUvuA(Integer uvuA) {
        this.uvuA = uvuA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public Integer getPointsB() {
        return pointsB;
    }

    public void setPointsB(Integer pointsB) {
        this.pointsB = pointsB;
    }

    public Integer getOvertimeB() {
        return overtimeB;
    }

    public void setOvertimeB(Integer overtimeB) {
        this.overtimeB = overtimeB;
    }

    public Integer getUvuB() {
        return uvuB;
    }

    public void setUvuB(Integer uvuB) {
        this.uvuB = uvuB;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return (
            "GameDTO{" +
            "id=" +
            id +
            ", splitDeckNum=" +
            splitDeckNum +
            ", clasification='" +
            clasification +
            '\'' +
            ", teamA='" +
            teamA +
            '\'' +
            ", pointsA=" +
            pointsA +
            ", overtimeA=" +
            overtimeA +
            ", uvuA=" +
            uvuA +
            ", teamB='" +
            teamB +
            '\'' +
            ", pointsB=" +
            pointsB +
            ", overtimeB=" +
            overtimeB +
            ", uvuB=" +
            uvuB +
            ", timeLeft=" +
            timeLeft +
            ", group=" +
            group +
            '}'
        );
    }
}
