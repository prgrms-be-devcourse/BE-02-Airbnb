package com.prgrms.airbnb.domain.common.util;


import com.prgrms.airbnb.domain.common.entity.Email;

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