package com.skyapi.weatherforecast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationController;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
public class LocationApiControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LocationService locationService;

    private static final String END_POINT_PATH = "/v1/locations";

    @Test
    public void testAddReturn400BadRequest() throws Exception {
        Location location = new Location();

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testAddReturn201Created() throws Exception {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("USA");

        Mockito.when(locationService.add(location)).thenReturn(location);

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    public void testListShouldReturn204NoContent() throws Exception {
        Mockito.when(locationService.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testListShouldReturn201OK() throws Exception {
        List<Location> locations = new ArrayList<>();

        Location location1 = new Location();
        location1.setCode("NYC_USA");
        location1.setCityName("New York City");
        location1.setRegionName("New York");
        location1.setCountryCode("US");
        location1.setCountryName("USA");

        Location location2 = new Location();
        location2.setCode("DEL_IN");
        location2.setCityName("New Delhi");
        location2.setRegionName("Delhi");
        location2.setCountryCode("IN");
        location2.setCountryName("INDIA");

        locations.add(location1);
        locations.add(location2);

        Mockito.when(locationService.list()).thenReturn(locations);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn405MethodNotAllowed() throws Exception {
        String requestURI = END_POINT_PATH + "/ABCDEF";

        mockMvc.perform(post(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {
        String requestURI = END_POINT_PATH + "/ABCDEF";

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn200OK() throws Exception {
        String code = "NYC_USA";
        String requestURI = END_POINT_PATH + "/" + code;

        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("USA");

        Mockito.when(locationService.getByCode(code)).thenReturn(location);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws LocationNotFoundException, Exception {
        Location location = new Location();
        location.setCode("ABCDEF");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("USA");

        Mockito.when(locationService.updateLocation(location)).thenThrow(new LocationNotFoundException("Location not found"));

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception, LocationNotFoundException {
        Location location = new Location();
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("USA");

        Mockito.when(locationService.updateLocation(location)).thenThrow(new LocationNotFoundException("Location not found"));

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception, LocationNotFoundException {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("USA");

        Mockito.when(locationService.updateLocation(location)).thenThrow(new LocationNotFoundException("Location not found"));

        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn404NotFound() throws LocationNotFoundException, Exception {
        String code = "NYC_USA";
        String requestURI = END_POINT_PATH + "/" + code;

        Mockito.doThrow(LocationNotFoundException.class).when(locationService).trashLocation(code);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn204NoContent() throws LocationNotFoundException, Exception {
        String code = "NYC_AMA";
        String requestURI = END_POINT_PATH + "/" + code;

        Mockito.doNothing().when(locationService).trashLocation(code);
        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
