package com.pbpoints.service.dto.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "POSITION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionDTO {

    @JacksonXmlProperty(localName = "CATEGORY_ID")
    @NotNull
    private Long categoryId;

    @JacksonXmlProperty(localName = "CATEGORY_NAME")
    @NotNull
    private String category;

    @JacksonXmlProperty(localName = "NRO")
    @NotNull
    private Integer position;

    @JacksonXmlProperty(localName = "TEAM_ID")
    @NotNull
    private Long teamId;

    @JacksonXmlProperty(localName = "POINTS")
    @NotNull
    private Float points;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Float getPoints() {
        return points;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return (
            "PositionDTO{" +
            "categoryId=" +
            categoryId +
            ", category='" +
            category +
            '\'' +
            ", position=" +
            position +
            ", teamId=" +
            teamId +
            ", points=" +
            points +
            '}'
        );
    }
}
