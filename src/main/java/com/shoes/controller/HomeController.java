package com.shoes.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shoes.global.GlobalData;
import com.shoes.model.Product;
import com.shoes.model.Role;
import com.shoes.model.User;
import com.shoes.repository.RoleRepository;
import com.shoes.repository.UserRepository;
import com.shoes.service.CategoryService;
import com.shoes.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;



@Controller
public class HomeController {
	
	
	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;
	
	public String home(Model model) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "index";
	}
	
	
	@GetMapping("/shop")
	public String shop(Model model) {
		model.addAttribute("categories", categoryService.getAllCategory());
		model.addAttribute("products", productService.getAllProducts());
		model.addAttribute("cartCount", GlobalData.cart.size());
	    return "shop";
	}
	
	  @GetMapping("shop/category/{id}")
	    public String shopByCategory(@PathVariable int id, Model model){
	        List<Product> products = productService.getAllProductByCategoryId(id);
	        model.addAttribute("categories", categoryService.getAllCategory());
	        model.addAttribute("products", products);
	        model.addAttribute("cartCount", GlobalData.cart.size());

	        return "shop";
	    }

	
	
	  @GetMapping("shop/viewproduct/{id}")
	    public String viewProduct(@PathVariable int id, Model model){
	        model.addAttribute("product", productService.getProductById(id).get());
	        model.addAttribute("cartCount", GlobalData.cart.size());
	        return "viewProduct";


	    }
	
	
	
	  @PostMapping("/register")
	  public String registerPost(@ModelAttribute("user") User user, HttpServletRequest request, Model model) throws ServletException {
	      String password = user.getPassword();
	      user.setPassword(bCryptPasswordEncoder.encode(password));
	      List<Role> roles = new ArrayList<>();
	      Role role = roleRepository.findById(2).orElse(null);
	      if (role != null) {
	          roles.add(role);
	      }
	      user.setRoles(roles);

	      try {
	          userRepository.save(user);
	          request.login(user.getEmail(), password);
	          return "redirect:/";
	      } catch (DataIntegrityViolationException e) {
	          // Handle unique constraint violation (email already exists)
	          model.addAttribute("error", "Email already registered.");
	          return "redirect:/register"; 
	      } catch (ServletException ex) {
	          // Handle login exception
	          model.addAttribute("error", "Error logging in.");
	          return "redirect:/register"; // Replace with the appropriate view name for the login form
	      }
	  }



}
