package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.EventCategory} entity.
 */
public class EventCategoryDTO implements Serializable {

    private Long id;

    private Boolean splitDeck;

    private EventDTO event;

    private CategoryDTO category;

    private FormatDTO format;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSplitDeck() {
        return splitDeck;
    }

    public void setSplitDeck(Boolean splitDeck) {
        this.splitDeck = splitDeck;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public FormatDTO getFormat() {
        return format;
    }

    public void setFormat(FormatDTO format) {
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCategoryDTO)) {
            return false;
        }

        EventCategoryDTO eventCategoryDTO = (EventCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCategoryDTO{" +
            "id=" + getId() +
            ", splitDeck='" + getSplitDeck() + "'" +
            ", event=" + getEvent() +
            ", category=" + getCategory() +
            ", format=" + getFormat() +
            "}";
    }
}
