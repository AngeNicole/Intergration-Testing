package rw.ac.rca.termOneExam.controllerIntergrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityControllerIntegrationTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final ObjectMapper om = new ObjectMapper();


    @Test
    public void getAll_Success() throws JSONException {
        String response = this.testRestTemplate.getForObject("/products",String.class);
        JSONAssert.assertEquals("[{\"id\":1,\"name\":\"New York\",\"weather\":43.0},{\"id\":2,\"name\":\"Kigali City\",\"weather\":27.0},{\"id\":3,\"name\":\"Ottawa\",\"weather\":34.0}]", response,false);
    }

    @Test
    public void getById__success() throws Exception {
        String expected = "{\"status\":true,\"message\":\"\",\"data\":{\"id\":1,\"name\":\"New York\",\"weather\":43.0}}";
        ResponseEntity<String> response =this.testRestTemplate.withBasicAuth("spring","secret").getForEntity("/id/{id}", String.class);
//                this.testRestTemplate.getForEntity("/products/1",String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void getById__404() throws Exception{
        String expected = "{\"status\":false,\"message\":\"City not found\",\"data\":null}";
        ResponseEntity<String>  response = this.testRestTemplate.getForEntity("/id/{10}",String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void saveCity__success() throws Exception{
        CreateCityDTO createCityDTO = new CreateCityDTO();
        City city = new City("New York",45);
        ResponseEntity<City> responseEntity = this.testRestTemplate.postForEntity("/all", city, City.class);

        assertEquals(201, responseEntity.getStatusCodeValue());
//        assertEquals(300.0, responseEntity.getBody().getPrice());
    }

    @Test
    public void saveProduct__BadRequest(){
        CreateCityDTO cityDTO = new CreateCityDTO();
        City city = new City("New York",35);
        ResponseEntity<City> responseEntity = this.testRestTemplate.postForEntity("/all", city, City.class);
        assertEquals(400, responseEntity.getStatusCodeValue());
//        assertEquals("Product name already exists", Objects.requireNonNull(responseEntity.getBody()).getMessage());

    }

    @Test
    public void updateProduct__success() throws JsonProcessingException {

        City UpdatedCity = new City(1L,"Canvas", 20.0, 50);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(UpdatedCity), headers);
        ResponseEntity<String> response = this.testRestTemplate.exchange("/id/{1}", HttpMethod.PUT,entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void deleteProduct__success(){
        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = this.testRestTemplate.exchange("/id/{1}", HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

}
