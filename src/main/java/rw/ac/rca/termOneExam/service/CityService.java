package rw.ac.rca.termOneExam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.repository.ICityRepository;
import rw.ac.rca.termOneExam.utils.APICustomResponse;

@Service
public class CityService {

	@Autowired
	private ICityRepository cityRepository;
	
	public Optional<City> getById(long id) {
		
		return cityRepository.findById(id);
	}

	public List<City> getAll() {
		
		return cityRepository.findAll();
	}

	public boolean existsByName(String name) {
		
		return cityRepository.existsByName(name);
	}

	public City save(CreateCityDTO dto) {
		City city =  new City(dto.getName(), dto.getWeather());
		return cityRepository.save(city);
	}

	public ResponseEntity<APICustomResponse> create(CreateCityDTO createCityDTO){

		City city = new City(createCityDTO);
		if(cityRepository.existsByName(city.getName())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APICustomResponse(false,"Product name already exists",city));
		}
		City savedCity = cityRepository.save(city);
		return ResponseEntity.status(HttpStatus.CREATED).body(new APICustomResponse(true, "Product created successfully", savedCity));
	}

	public ResponseEntity<APICustomResponse> update(Long id,CreateCityDTO createCityDTO ){
		Optional<City> cityFoundById = cityRepository.findById(id);
		if(cityFoundById.isPresent()){
			City city = cityFoundById.get();
			if(cityRepository.existsByName(createCityDTO.getName()) && !(createCityDTO.getName().equalsIgnoreCase(city.getName()))){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APICustomResponse(false, "Product name already exists"));
			}
			city.setName(createCityDTO.getName());
			city.setWeather(createCityDTO.getWeather());
			cityRepository.save(city);
			return ResponseEntity.ok(new APICustomResponse(true,"Product updated successfully", city));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APICustomResponse(false,"Product Not found"));
	}

	public ResponseEntity<APICustomResponse> delete(Long id){
		Optional<City> cityFoundById  = cityRepository.findById(id);
		if(cityFoundById.isPresent()){

			cityRepository.deleteById(id);
			return ResponseEntity.ok(new APICustomResponse(true,"Product Deleted Successfully"));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APICustomResponse(false, "Product not found"));
	}
}
