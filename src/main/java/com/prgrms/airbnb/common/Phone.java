package com.prgrms.airbnb.common;

import javax.persistence.Embeddable;

@Embeddable
public class Phone {
    private String phoneNumber;

    protected Phone() {
    }

    public Phone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
