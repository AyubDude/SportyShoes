package com.shoes.controller;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shoes.global.GlobalData;
import com.shoes.model.CheckOut;
import com.shoes.model.Product;
import com.shoes.service.CheckOutService;
import com.shoes.service.ProductService;

@Controller
public class CartController {
    @Autowired
    ProductService productService;
    @Autowired
    CheckOutService checkOutService;

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id){
        GlobalData.cart.add(productService.getProductById(id).get());

        return "redirect:/shop";

    }

    @GetMapping("/cart")
    public String cartGet(Model model){
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        model.addAttribute("cart", GlobalData.cart);

        return "cart";
    }

    @GetMapping("cart/removeItem/{index}")
    public String removeItemFromCart(@PathVariable int index){
        GlobalData.cart.remove(index);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model){

        model.addAttribute("totalBill",GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        model.addAttribute("checkOut", new CheckOut());
        model.addAttribute("productIds", GlobalData.cart.stream().map(Product::getId).toString());
        model.addAttribute("userId", GlobalData.LOGINUSER.getEmail());
        return "checkout";
    }


    @PostMapping("/checkout/payNow")
    public String payOut(@ModelAttribute("checkOut") CheckOut checkout,
                         Model model){
        Date dateOfOrder = new Date();
        Double totalBill = GlobalData.cart.stream().mapToDouble(Product::getPrice).sum();
        String randomOrderId =randomOrderId();

        checkout.setOrderId(randomOrderId);
        checkout.setUserId(GlobalData.LOGINUSER.getEmail());
        String productIds = "";
        for(Product p : GlobalData.cart){
            productIds+=String.valueOf(p.getId())+",";
        }
        checkout.setProductIds(productIds);
        checkout.setTotalBill(totalBill);
        checkout.setDateofOrder(dateOfOrder);


        model.addAttribute("dateOfOrder", dateOfOrder);
        model.addAttribute("orderId",randomOrderId);
        model.addAttribute("items", GlobalData.cart);
        model.addAttribute("totalBill", totalBill);

        System.out.println(checkout.toString());

        checkOutService.saveOrder(checkout);
        return "payment";
    }

    //Method to Genrate Order ID
    public String randomOrderId() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        //System.out.println(generatedString);
        return generatedString;
    }


}
