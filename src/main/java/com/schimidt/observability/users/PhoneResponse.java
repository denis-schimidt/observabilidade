package com.schimidt.observability.users;

record PhoneResponse(Long id, String number) {

    public static PhoneResponse from(Phone p) {
        return new PhoneResponse(p.getId(), p.getNumber());
    }
}
