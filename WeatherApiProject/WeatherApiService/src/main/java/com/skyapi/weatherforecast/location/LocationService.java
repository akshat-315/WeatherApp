package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location add(Location location){
        return locationRepository.save(location);
    }

    public List<Location> list(){
        return locationRepository.findUntrashed();
    }

    public Location getByCode(String code){
        return locationRepository.findLocationByCode(code);
    }

    public Location updateLocation(Location locationRequest) throws LocationNotFoundException {
        String code = locationRequest.getCode();

        Location locationInDb = locationRepository.findLocationByCode(code);

        if(locationInDb == null){
            throw new LocationNotFoundException("Could not find the location with the code: " + code);
        }

        locationInDb.setCityName(locationRequest.getCityName());
        locationInDb.setRegionName(locationRequest.getRegionName());
        locationInDb.setCountryName(locationRequest.getCountryName());
        locationInDb.setCountryCode(locationRequest.getCountryCode());
        locationInDb.setEnabled(locationRequest.isEnabled());

        return locationRepository.save(locationInDb);

    }
}
