package pl.edu.wszib.springsklepkoszyk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.edu.wszib.springsklepkoszyk.exception.ResourceNotFound;
import pl.edu.wszib.springsklepkoszyk.model.Order;
import pl.edu.wszib.springsklepkoszyk.model.OrderItem;
import pl.edu.wszib.springsklepkoszyk.model.Product;
import pl.edu.wszib.springsklepkoszyk.repository.OrderRepo;
import pl.edu.wszib.springsklepkoszyk.repository.ProductRepo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSklepKoszykApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	ObjectMapper objectMapper;

	////////// PRODUCT TESTS ////////////////
	@Test
	void getAllProducts() throws Exception {
		// arrange
		// 10 product is added when app starts

		//act and assert
		mockMvc.perform(get("/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(10)))
				.andExpect(jsonPath("$[1].name", is("Product 1")))
				.andExpect(jsonPath("$[9].name", is("Product 9")));
	}

	@Test
	void getProdById() throws Exception {
		// arrange
		// chech id= 2l, name= "Product 1", price= 11.99

		// act and assert
		mockMvc.perform(get("/products/{id}", 2L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Product 1"))
				.andExpect(jsonPath("$.price").value(11.99));
	}

	@Test
	void deleteProductById() throws Exception{
		// arrange
		// deleting product with id= 1L

		// act and assert
		mockMvc.perform(delete("/products/{id}", 1L))
				.andExpect(status().isNoContent());
	}

	@Test
	void updateProduct() throws Exception{
		// arrange
		// make new Product with existing id and modified name and price
		Product updateProduct = new Product(1L,"updatedProduct", BigDecimal.TEN);

		// convert java object to JSON
		String updateProductJSON = objectMapper.writeValueAsString(updateProduct);

		// act and assert
		mockMvc.perform(put("/products/{id}", updateProduct.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateProductJSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.name").value("updatedProduct"))
				.andExpect(jsonPath("$.price").value(BigDecimal.TEN));


	}
	@Test
	void addNewProduct() throws Exception{
		// arrange
		// make newProduct
		Product newProduct = new Product();
		newProduct.setName("newProduct");
		newProduct.setPrice(BigDecimal.TEN);

		// convert newProduct to JSON
		String newProductJSON = objectMapper.writeValueAsString(newProduct);

		//act and assert
		mockMvc.perform(post("/products/add-new-product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newProductJSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("newProduct"))
				.andExpect(jsonPath("$.price").value(BigDecimal.TEN));

	}

	//////////// ORDER TESTS /////////////

	@Test
	void createNewOrder() throws Exception{
		// arrange
		Product product2 = productRepo.findById(2L).orElseThrow(()->
													new ResourceNotFound("ID not found in db "+ 2L));

		// act and assert
		mockMvc.perform(post("/orders/new-order/{id}", 2L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.createDate").isNotEmpty())
				.andExpect(jsonPath("$.modifyDate").isNotEmpty())
				.andExpect(jsonPath("$.lines[0].id").isNotEmpty())
				.andExpect(jsonPath("$.lines[0].product.name").value(product2.getName()))
				.andExpect(jsonPath("$.lines[0].productValue").value(product2.getPrice()))
				.andExpect(jsonPath("$.lines[0].quantity").value(1))
				.andExpect(jsonPath("$.orderValue").value(product2.getPrice()));

	}
	@Test
	void addToExistingOrderTheSameProduct() throws Exception{
		// arrange
		Product product2 = productRepo.findById(2L).orElseThrow(()->
				new ResourceNotFound("ID not found in db "+ 2L));
		OrderItem testOrderItem = new OrderItem();
		testOrderItem.setProduct(product2);
		testOrderItem.setQuantity(1);
		testOrderItem.setProductValue(product2.getPrice());

		Set<OrderItem> testOrderItems = new HashSet<>();
		testOrderItems.add(testOrderItem);

		Order testOrder = new Order(1L, Instant.now(), Instant.now(), testOrderItems,product2.getPrice());
		orderRepo.save(testOrder);



		// act and assert
		mockMvc.perform(put("/orders/{orderId}/add-product/{productId}"
				                       , testOrder.getId(), product2.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.createDate").isNotEmpty())
				.andExpect(jsonPath("$.modifyDate").isNotEmpty())
				.andExpect(jsonPath("$.lines[0].id").isNotEmpty())
				.andExpect(jsonPath("$.lines[0].product.name").value(product2.getName()))
				.andExpect(jsonPath("$.lines[0].productValue").value(product2.getPrice().multiply(BigDecimal.valueOf(2))))
				.andExpect(jsonPath("$.lines[0].quantity").value(2))
				.andExpect(jsonPath("$.orderValue").value(product2.getPrice().multiply(BigDecimal.valueOf(2))));
	}
	@Test
	void addToExistingOrderDiffrentProduct() throws Exception{
		// arrange
		Product product2 = productRepo.findById(2L).orElseThrow(()->
				new ResourceNotFound("ID not found in db "+ 2L));
		OrderItem testOrderItem = new OrderItem();
		testOrderItem.setProduct(product2);
		testOrderItem.setQuantity(1);
		testOrderItem.setProductValue(product2.getPrice());

		Set<OrderItem> testOrderItems = new HashSet<>();
		testOrderItems.add(testOrderItem);

		Order testOrder = new Order(1L, Instant.now(), Instant.now(), testOrderItems,product2.getPrice());
		orderRepo.save(testOrder);

		Product product3 = productRepo.findById(3L).orElseThrow(()->
				new ResourceNotFound("ID not found in db "+ 3L));


		// act and assert
		mockMvc.perform(put("/orders/{orderId}/add-product/{productId}"
						, testOrder.getId(), product3.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.createDate").isNotEmpty())
				.andExpect(jsonPath("$.modifyDate").isNotEmpty())
				.andExpect(jsonPath("$.lines[1].product.name").value(product2.getName()))
				.andExpect(jsonPath("$.lines[1].productValue").value(product2.getPrice()))
				.andExpect(jsonPath("$.lines[1].quantity").value(1))
				.andExpect(jsonPath("$.lines[0].product.name").value(product3.getName()))
				.andExpect(jsonPath("$.lines[0].productValue").value(product3.getPrice()))
				.andExpect(jsonPath("$.lines[0].quantity").value(1))
				.andExpect(jsonPath("$.orderValue").value(product2.getPrice().add(product3.getPrice())));
	}

	@Test
	void IncrementProductInOrder() throws Exception{
		// arrange
		Product product2 = productRepo.findById(2L).orElseThrow(()->
				new ResourceNotFound("ID not found in db "+ 2L));
		OrderItem testOrderItem = new OrderItem();
		testOrderItem.setProduct(product2);
		testOrderItem.setQuantity(1);
		testOrderItem.setProductValue(product2.getPrice());

		Set<OrderItem> testOrderItems = new HashSet<>();
		testOrderItems.add(testOrderItem);

		Order testOrder = new Order(1L, Instant.now(), Instant.now(), testOrderItems,product2.getPrice());
		orderRepo.save(testOrder);

		// act and assert
		mockMvc.perform(put("/orders/{orderId}/increment/{productId}"
						, testOrder.getId(), product2.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.createDate").isNotEmpty())
				.andExpect(jsonPath("$.modifyDate").isNotEmpty())
				.andExpect(jsonPath("$.lines[0].product.name").value(product2.getName()))
				.andExpect(jsonPath("$.lines[0].productValue").value(product2.getPrice().multiply(BigDecimal.valueOf(2))))
				.andExpect(jsonPath("$.lines[0].quantity").value(2))
				.andExpect(jsonPath("$.orderValue").value(product2.getPrice().multiply(BigDecimal.valueOf(2))));
	}
	@Test
	void DerementProductInOrder() throws Exception{
		// arrange
		Product product2 = productRepo.findById(2L).orElseThrow(()->
				new ResourceNotFound("ID not found in db "+ 2L));
		OrderItem testOrderItem = new OrderItem();
		testOrderItem.setProduct(product2);
		testOrderItem.setQuantity(2);
		testOrderItem.setProductValue(product2.getPrice().multiply(BigDecimal.valueOf(2)));

		Set<OrderItem> testOrderItems = new HashSet<>();
		testOrderItems.add(testOrderItem);

		Order testOrder = new Order(1L, Instant.now(), Instant.now(), testOrderItems,product2.getPrice());
		orderRepo.save(testOrder);

		// act and assert
		mockMvc.perform(put("/orders/{orderId}/decrement/{productId}"
						, testOrder.getId(), product2.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.createDate").isNotEmpty())
				.andExpect(jsonPath("$.modifyDate").isNotEmpty())
				.andExpect(jsonPath("$.lines[0].product.name").value(product2.getName()))
				.andExpect(jsonPath("$.lines[0].productValue").value(product2.getPrice()))
				.andExpect(jsonPath("$.lines[0].quantity").value(1))
				.andExpect(jsonPath("$.orderValue").value(product2.getPrice()));
	}
	@Test
	void ChangeQuantityProductInOrder() throws Exception{
		// arrange
		Product product2 = productRepo.findById(2L).orElseThrow(()->
				new ResourceNotFound("ID not found in db "+ 2L));
		OrderItem testOrderItem = new OrderItem();
		testOrderItem.setProduct(product2);
		testOrderItem.setQuantity(1);
		testOrderItem.setProductValue(product2.getPrice());

		Set<OrderItem> testOrderItems = new HashSet<>();
		testOrderItems.add(testOrderItem);

		Order testOrder = new Order(1L, Instant.now(), Instant.now(), testOrderItems,product2.getPrice());
		orderRepo.save(testOrder);

		// act and assert
		mockMvc.perform(put("/orders/{orderId}/change-quantity/{productId}/{newQuantity}"
						, testOrder.getId(), product2.getId(), 2))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.createDate").isNotEmpty())
				.andExpect(jsonPath("$.modifyDate").isNotEmpty())
				.andExpect(jsonPath("$.lines[0].product.name").value(product2.getName()))
				.andExpect(jsonPath("$.lines[0].productValue").value(product2.getPrice().multiply(BigDecimal.valueOf(2))))
				.andExpect(jsonPath("$.lines[0].quantity").value(2))
				.andExpect(jsonPath("$.orderValue").value(product2.getPrice().multiply(BigDecimal.valueOf(2))));
	}
	@Test
	void RemoveProductFromOrder() throws Exception{
		// arrange
		Product product2 = productRepo.findById(2L).orElseThrow(()->
				new ResourceNotFound("ID not found in db "+ 2L));
		OrderItem testOrderItem = new OrderItem();
		testOrderItem.setProduct(product2);
		testOrderItem.setQuantity(1);
		testOrderItem.setProductValue(product2.getPrice());

		Product product3 = productRepo.findById(3L).orElseThrow(()->
				new ResourceNotFound("ID not found in db "+ 3L));
		OrderItem test2OrderItem = new OrderItem();
		test2OrderItem.setProduct(product3);
		test2OrderItem.setQuantity(1);
		test2OrderItem.setProductValue(product3.getPrice());

		Set<OrderItem> testOrderItems = new HashSet<>();
		testOrderItems.add(testOrderItem);
		testOrderItems.add(test2OrderItem);

		Order testOrder = new Order(1L, Instant.now(), Instant.now(), testOrderItems,product2.getPrice());
		orderRepo.save(testOrder);

		// act and assert
		mockMvc.perform(put("/orders/{orderId}/remove-product/{productId}"
						, testOrder.getId(), product3.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.createDate").isNotEmpty())
				.andExpect(jsonPath("$.modifyDate").isNotEmpty())
				.andExpect(jsonPath("$.lines[0].product.name").value(product2.getName()))
				.andExpect(jsonPath("$.lines[0].productValue").value(product2.getPrice()))
				.andExpect(jsonPath("$.lines[0].quantity").value(1))
				.andExpect(jsonPath("$.orderValue").value(product2.getPrice()));
	}


}
