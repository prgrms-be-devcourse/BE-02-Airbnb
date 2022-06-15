package com.prgrms.airbnb.common.jpa;

import com.prgrms.airbnb.common.model.Email;

import javax.persistence.AttributeConverter;

public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email == null ? null : email.getEmail();
    }

    @Override
    public Email convertToEntityAttribute(String email) {
        return email == null ? null : new Email(email);
    }
}