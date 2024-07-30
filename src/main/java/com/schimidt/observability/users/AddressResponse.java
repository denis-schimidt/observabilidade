package com.schimidt.observability.users;

record AddressResponse(Long id, String street, String city, String state, String zipCode, String country, Short number) {

    public static AddressResponse from(Address address) {
        return new AddressResponse(address.getId(), address.getStreet(), address.getCity(), address.getState(), address.getZipCode(), address.getCountry(), address.getNumber());
    }
}
