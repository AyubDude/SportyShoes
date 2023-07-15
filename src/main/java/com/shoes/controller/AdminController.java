package com.shoes.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shoes.dto.OrdersDto;
import com.shoes.dto.ProductDTO;
import com.shoes.model.Category;
import com.shoes.model.CheckOut;
import com.shoes.model.Product;
import com.shoes.model.User;
import com.shoes.service.CategoryService;
import com.shoes.service.CheckOutService;
import com.shoes.service.ProductService;
import com.shoes.service.UserService;

@Controller
public class AdminController {
	
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CheckOutService checkOutService;
	
	
	private static String uploadDir = System.getProperty("user.dir")+"/src/main/resources/static/productImages/";
	
	@GetMapping("/admin")
	public String adminHome() {
		return "adminHome";
	}
	
	
	@GetMapping("/admin/categories")
	public String getCat(Model model) {
		model.addAttribute("categories", categoryService.getAllCategory());
		return "categories";
	}
	
	
	@GetMapping("/admin/categories/add")
	public String getCatAdd(Model model) {
		model.addAttribute("category", new Category());
		return "categoriesAdd";
	}
	
	
	@PostMapping("/admin/categories/add")
	public String postCatAdd(@ModelAttribute("category") Category category) {
		categoryService.addCategory(category);
		return"redirect:/admin/categories";
	}
	
	
	@GetMapping("/admin/categories/delete/{id}")
	public String deleteCat(@PathVariable int id) {
		categoryService.removeCategoryById(id);
		return"redirect:/admin/categories";
	}
	
	@GetMapping("/admin/categories/update/{id}")
	public String updateCat(@PathVariable int id, Model model) {
		Optional<Category> categoryById = categoryService.getCategoryById(id);
		model.addAttribute("category", categoryById.get());
		return "categoriesAdd";
	}
	
	//Product
	
	@GetMapping("/admin/products")
	public String products(Model model) {
		model.addAttribute("products", productService.getAllProducts());
		return"products";
	}
	
	
	@GetMapping("/admin/products/add")
	public String productAddGet(Model model) {
		model.addAttribute("productDTO", new ProductDTO());
		model.addAttribute("categories", categoryService.getAllCategory());
		return"productAdd";
	}
	
	@PostMapping("/admin/products/add")
	public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
			@RequestParam("productImage")MultipartFile file,
			@RequestParam("imgName") String imgName) throws IOException{
		
		
		Product product = new Product();
		product.setId(productDTO.getId());
		product.setName(productDTO.getName());
		product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
		product.setWeight(productDTO.getWeight());
		product.setImageName(imgName);
		
		String imageUUID;
		
		if(!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
			Path fileNameandPath = Paths.get(uploadDir,imageUUID);
			Files.write(fileNameandPath, file.getBytes());
		}else{
			imageUUID = imgName;
		}
		product.setImageName(imageUUID);
		productService.addProduct(product);
		return"redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/delete/{id}")
	public String deleteProduct(@PathVariable int id) {
		Product product = productService.getProductById(id).get();
		File productImage = new File(uploadDir+product.getImageName());
		productImage.delete();
		productService.removeProductById(id);
		
		return"redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/update/{id}")
	public String updateProduct(@PathVariable int id, Model model) {
		Product product = productService.getProductById(id).get();
		ProductDTO productDTO =  new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setCategoryId(product.getCategory().getId());
		productDTO.setPrice(product.getPrice());
		productDTO.setWeight(product.getWeight());
		productDTO.setDescription(product.getDescription());
		productDTO.setImageName(product.getImageName());
		
		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("productDTO", productDTO);
		
		return "productAdd";
	}
	
	
	@GetMapping("/admin/users")
	public String displayUser(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		return"users";
	}
	
	@GetMapping("/admin/orders")
	public String displayOrders(Model model) {
		List<CheckOut> orders = checkOutService.getAllOrders();
		List<OrdersDto> ordersDtos = new ArrayList<>();
		
		for(CheckOut x: orders) {
			String productIds[] = x.getProductIds().split(",");
			List<Product> products = new ArrayList<>();
			
			for(String id: productIds) {
				products.add(productService.getProductById(Integer.parseInt(id)).get());
			}
			
			String fullName = "";
			if(x.getUserId()!=null) {
				User user = userService.getUserByEmail(x.getUserId()).get();
				fullName = user.getFirstName()+" "+user.getLastName();
				ordersDtos.add(new OrdersDto(x, products,fullName));
			}
			
		}
		model.addAttribute("orders", ordersDtos);
		return"ordersList";
	}
	
	

}
