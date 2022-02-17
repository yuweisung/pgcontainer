package solutions.datanerd.pgcontainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class PgcontainerApplication {	
	public static void main(String[] args) {
		SpringApplication.run(PgcontainerApplication.class, args);
	}

}

@Entity
class Coffee {
	@Id
	private String id;
	private String name;
	public Coffee(){

	}
	public Coffee(String name) {
		this(UUID.randomUUID().toString(), name);
	}
	public Coffee(String id, String name){
		this.id = id;
		this.setName(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
}

@RestController
@RequestMapping("/coffees")
class RestCoffeeController{
	private final CoffeeRepository coffeeRepository;
	public RestCoffeeController(CoffeeRepository coffeeRepository){
		this.coffeeRepository = coffeeRepository;
		
	}
	@GetMapping
	Iterable<Coffee> getCoffees(){
		return coffeeRepository.findAll();
	}

	@GetMapping("/coffees/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id) {
		return coffeeRepository.findById(id);
	}

	@PostMapping("/coffees")
	Coffee postCoffee(@RequestBody Coffee coffee){
		return coffeeRepository.save(coffee);
	}

	@PutMapping("/coffees/{id}")
	Coffee putCoffee(@PathVariable String id, @RequestBody Coffee newCoffee){
		return coffeeRepository.findById(id)
			.map(coffee -> {
				coffee.setName(newCoffee.getName());
				return coffeeRepository.save(coffee);
			})
			.orElseGet(() -> {
				return coffeeRepository.save(newCoffee);
			});
	}

	@DeleteMapping("/coffees/{id}")
	void deleteCoffee(@PathVariable String id){
		coffeeRepository.deleteById(id);
	}
}

interface CoffeeRepository extends CrudRepository<Coffee, String> {}

@Component
class DataLoader{
	private final CoffeeRepository coffeeRepository;

	public DataLoader(CoffeeRepository coffeeRepository) {
		this.coffeeRepository = coffeeRepository;
	}
	@PostConstruct
	private void loadData(){
		coffeeRepository.saveAll(List.of(
			new Coffee("Gazza"),
			new Coffee("Lareno"),
			new Coffee("Tres Pontas")
		));
	}
}
