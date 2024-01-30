package com.skyapi.weatherforecast.location;

import com.skyapi.weatherforecast.common.Location;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody @Valid Location location){
        Location addedLocation = locationService.add(location);
        URI uri = URI.create("/v1/locations/" + location.getCode());
        return ResponseEntity.created(uri).body(addedLocation);
    }

    @GetMapping
    public ResponseEntity<?> listLocations(){
        List<Location> locations = locationService.list();

        if(locations.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getLocationByCode(@PathVariable("code") String code){
        Location location = locationService.getByCode(code);

        if(location == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(location);
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid Location location){
        try {
            Location updatedLocation = locationService.updateLocation(location);
            return ResponseEntity.ok(updatedLocation);
        }catch(LocationNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> trashLocation(@PathVariable String code){
        try{
            locationService.trashLocation(code);
            return ResponseEntity.noContent().build();
        }catch(LocationNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

}
