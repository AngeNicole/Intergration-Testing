package rw.ac.rca.termOneExam.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.repository.ICityRepository;
import rw.ac.rca.termOneExam.utils.APICustomResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

    @InjectMocks
    private CityService cityService;

    @Mock
    private ICityRepository cityRepositoryMock;

    @Test
    public void getAll(){
        List<City> city = Arrays.asList(new City(1L,"New York",42.0,68.7),new City(2L,"Yohana",56.0,90));
        when(cityRepositoryMock.findAll()).thenReturn(city);
        assertEquals(cityService.getAll().get(0), city.get(0));
        assertTrue(cityService.getAll().size() == city.size());

    }

    @Test
    public void  getOneById__success(){
        City city = new City(1L,"New York",42.0,68.7);
        when(cityRepositoryMock.findById(10L)).thenReturn(Optional.of(city));
        assertEquals(cityService.getById(10L).getBody().getData(), city);
    }

    @Test
    public void getOneById__failure(){
        Optional emptyOptional = Optional.empty();
        when(cityRepositoryMock.findById(10L)).thenReturn(emptyOptional);
        assertEquals(cityService.getById(10L).getBody().isStatus(), false);
        assertEquals(cityService.getById(10L).getStatusCode() == HttpStatus.NOT_FOUND,true);
    }

    @Test
    public void create__success(){
        CreateCityDTO createCityDTO = new CreateCityDTO();
        City city = new City("Mouse",45);
        when(cityRepositoryMock.save(any(City.class))).thenReturn(city);
        assertNotNull(cityService.create(createCityDTO).getBody().getData().getId());
        assertEquals(createCityDTO.getQuantity(), cityService.create(createCityDTO).getBody().getData().getQuantity());
        assertTrue(cityService.create(createCityDTO).getStatusCodeValue() == 201);
    }

    @Test
    public void create__failure(){
        CreateCityDTO cityDTO = new CreateCityDTO();
        City city = new City("New York", 45.6);
        when(cityRepositoryMock.save(any(City.class))).thenReturn(city);
        when(cityRepositoryMock.existsByName(city.getName())).thenReturn(true);
        assertTrue(cityService.create(cityDTO).getBody().isStatus() == false);
        assertFalse(cityService.create(cityDTO).getStatusCode().is2xxSuccessful());
    }

    @Test
    public void update__success(){
        CreateCityDTO cityDTO = new CreateCityDTO();

        City city = new City(10L,"Ottawa",30,10);
        when(cityRepositoryMock.findById(city.getId())).thenReturn(Optional.of(city));
        when(cityRepositoryMock.existsByName(city.getName())).thenReturn(false);
        when(cityRepositoryMock.save(city)).thenReturn(city);
        ResponseEntity<APICustomResponse> updatedCity = cityService.update(10L,cityDTO);
        assertTrue(updatedCity.getBody().getData().getName() == cityDTO.getName());
        assertFalse(updatedCity.getBody().getData().getName() != cityDTO.getName());
        assertEquals(updatedCity.getBody().isStatus() , true);

    }

    @Test(expected = NullPointerException.class)
    public void update__failure(){
        CreateCityDTO cityDTO = new CreateCityDTO();
        City city = new City(10L,"Ottawa",30,10);
        when(cityRepositoryMock.findById(10L)).thenReturn(Optional.of(city));
        when(cityRepositoryMock.existsByName(cityDTO.getName())).thenReturn(true);
//        when(productRepositoryMock.save(product)).thenReturn(product);
        ResponseEntity<APICustomResponse> updatedCity = cityService.update(10L, cityDTO);

        assertFalse(updatedCity.getBody().getData().getName() == cityDTO.getName());
        assertEquals(updatedCity.getBody().isStatus(), false);
        assertTrue(updatedCity.getStatusCodeValue()==400);

    }

    @Test
    public void delete__success(){

        City city = new City(10L,"Ottawa",30,10);
        when(cityRepositoryMock.findById(city.getId())).thenReturn(Optional.of(city));
        cityService.delete(city.getId());
        verify(cityRepositoryMock).deleteById(city.getId());
    }
}

