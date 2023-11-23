package com.example.houserental.services.address;

public interface AddressService {
    String[] getAllCities();

    String[] getAllDistricts(String city);

    String[] getAllWards(String city, String district);
}
