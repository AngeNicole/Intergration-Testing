package rw.ac.rca.termOneExam.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.repository.ICityRepository;
import rw.ac.rca.termOneExam.service.CityService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityUtilTest {
    @InjectMocks
    private CityService cityService;

    @Mock
    private ICityRepository cityRepositoryMock;

    @Test
    public void cityNoWeatherMoreThan40(){
        List<City> city = Arrays.asList(new City(1L,"New York",42.0,68.7),new City(2L,"Yohana",23.8,50));
        when(cityRepositoryMock.findAll()).thenReturn(city);
        assertEquals(cityService.getAll().get(0), city.get(0)), cityService.<40;
        assertTrue(cityService.getAll().size() == city.size());

    }

    @Test
    public void  cityNoWeatherLessThan10(){
        City city = new City(1L,"New York",42.0,68.7);
        when(cityRepositoryMock.findById(10L)).thenReturn(Optional.of(city));
        assertEquals(cityService.getById(10L).getBody().getData(), city);
    }

    @Test
    public void cityInMusanzeKigali(){
        Optional emptyOptional = Optional.empty();
        when(cityRepositoryMock.findById(10L)).thenReturn(emptyOptional);
        assertEquals(cityService.getById(10L).getBody().isStatus(), false);
        assertEquals(cityService.getById(10L).getStatusCode() == HttpStatus.NOT_FOUND,true);
    }

}
