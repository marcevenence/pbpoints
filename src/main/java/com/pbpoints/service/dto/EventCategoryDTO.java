package com.pbpoints.service.dto;

import com.pbpoints.domain.Event;
import com.pbpoints.domain.Format;
import java.io.Serializable;
import java.util.Objects;

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

    public Boolean isSplitDeck() {
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventCategoryDTO eventCategoryDTO = (EventCategoryDTO) o;
        if (eventCategoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eventCategoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "EventCategoryDTO{" +
            "id=" +
            getId() +
            ", splitDeck='" +
            isSplitDeck() +
            "'" +
            ", eventId=" +
            getEvent() +
            "'" +
            ", categoryId" +
            getCategory() +
            "'" +
            ", formatId=" +
            getFormat() +
            "'" +
            "}"
        );
    }
}
