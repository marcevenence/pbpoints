package com.pbpoints.service.dto.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "CATEGORY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDTO {

    @JacksonXmlProperty(localName = "ID")
    @NotBlank
    private Long id;

    @JacksonXmlProperty(localName = "NAME")
    @NotBlank
    private String name;

    @JacksonXmlProperty(localName = "GAMES")
    @Valid
    @NotNull
    private List<GameDTO> games;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GameDTO> getGames() {
        return games;
    }

    public void setGames(List<GameDTO> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" + "id=" + id + ", name='" + name + '\'' + ", games=" + games + '}';
    }
}
