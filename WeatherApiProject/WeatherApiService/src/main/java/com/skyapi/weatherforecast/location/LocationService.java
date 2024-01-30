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

}
