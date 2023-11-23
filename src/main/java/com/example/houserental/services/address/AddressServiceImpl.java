package com.example.houserental.services.address;


import com.example.houserental.internal.repositories.address.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{
    private final AddressRepository addressRepository;
    @Override
    public String[] getAllCities() {
        String[] cities = addressRepository.getAllCities();
        return cities;
    }

    @Override
    public String[] getAllDistricts(String city) {
        String[] districts = addressRepository.getAllDistricts(city);
        return districts;
    }

    @Override
    public String[] getAllWards(String city, String district) {
        String[] wards = addressRepository.getAllWards(city, district);
        return wards;
    }
}
