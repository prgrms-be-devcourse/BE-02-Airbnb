package com.prgrms.airbnb.common;

import javax.persistence.Embeddable;

@Embeddable
public class Email {
    private String email;

    protected Email() {
    }

    public Email(String email) {
        this.email = email;
    }
}
