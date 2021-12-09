package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sponsor.
 */
@Entity
@Table(name = "sponsor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sponsor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "logo", nullable = false)
    private byte[] logo;

    @Column(name = "logo_content_type", nullable = false)
    private String logoContentType;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties(value = { "events", "owner" }, allowSetters = true)
    private Tournament tournament;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sponsor id(Long id) {
        this.id = id;
        return this;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Sponsor logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Sponsor logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public String getName() {
        return this.name;
    }

    public Sponsor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Sponsor active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public Sponsor tournament(Tournament tournament) {
        this.setTournament(tournament);
        return this;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sponsor)) {
            return false;
        }
        return id != null && id.equals(((Sponsor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sponsor{" +
            "id=" + getId() +
            ", logo=''" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", name='" + getName() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
