package com.shoes.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shoes.global.GlobalData;
import com.shoes.model.CustomUserDetail;
import com.shoes.model.User;
import com.shoes.repository.RoleRepository;
import com.shoes.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	@Autowired
	UserRepository userRepository;
	
	
	@Autowired
	RoleRepository roleRepository;
	
	
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,ServletException 
	{
		
		Object principal = authentication.getPrincipal();
		
		if(principal instanceof CustomUserDetail) {
			String userEmail = ((CustomUserDetail) principal).getUsername();
			GlobalData.LOGINUSER = new User(userRepository.findUserByEmail(userEmail).get());
			
			System.out.println(GlobalData.LOGINUSER.toString());
			System.out.println(authentication.getAuthorities());
		}else {
			System.out.println("Principal: "+principal.toString());
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	

}
