package com.pbpoints.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pbpoints.domain.UserExtra} entity. This class is used
 * in {@link com.pbpoints.web.rest.UserExtraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-extras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserExtraCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numDoc;

    private StringFilter phone;

    private LocalDateFilter bornDate;

    private StringFilter code;

    private LongFilter userId;

    private LongFilter docTypeId;

    public UserExtraCriteria() {}

    public UserExtraCriteria(UserExtraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.numDoc = other.numDoc == null ? null : other.numDoc.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.bornDate = other.bornDate == null ? null : other.bornDate.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.docTypeId = other.docTypeId == null ? null : other.docTypeId.copy();
    }

    @Override
    public UserExtraCriteria copy() {
        return new UserExtraCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumDoc() {
        return numDoc;
    }

    public StringFilter numDoc() {
        if (numDoc == null) {
            numDoc = new StringFilter();
        }
        return numDoc;
    }

    public void setNumDoc(StringFilter numDoc) {
        this.numDoc = numDoc;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public LocalDateFilter getBornDate() {
        return bornDate;
    }

    public LocalDateFilter bornDate() {
        if (bornDate == null) {
            bornDate = new LocalDateFilter();
        }
        return bornDate;
    }

    public void setBornDate(LocalDateFilter bornDate) {
        this.bornDate = bornDate;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getDocTypeId() {
        return docTypeId;
    }

    public LongFilter docTypeId() {
        if (docTypeId == null) {
            docTypeId = new LongFilter();
        }
        return docTypeId;
    }

    public void setDocTypeId(LongFilter docTypeId) {
        this.docTypeId = docTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserExtraCriteria that = (UserExtraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numDoc, that.numDoc) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(bornDate, that.bornDate) &&
            Objects.equals(code, that.code) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(docTypeId, that.docTypeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numDoc, phone, bornDate, code, userId, docTypeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserExtraCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (numDoc != null ? "numDoc=" + numDoc + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (bornDate != null ? "bornDate=" + bornDate + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (docTypeId != null ? "docTypeId=" + docTypeId + ", " : "") +
            "}";
    }
}
