package com.shoes.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.shoes.service.CustomUserDetailService;

@Configuration

public class SecurityConfig {
	
	@Autowired
	CustomUserDetailService customUserDetailService;
	
	@Autowired
	CustomLoginHandler customLoginHandler;
	
	
	@Autowired
	GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
		
		httpSecurity.csrf().disable().authorizeRequests()
		//.requestMatchers("/home/public")
		.requestMatchers("h2-console","/","/shop/**","/register","/admin")
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.permitAll()
		.failureUrl("/login?error=true")
		.defaultSuccessUrl("/")
		.usernameParameter("email")
		.passwordParameter("password")
		.successHandler( customLoginHandler)
		.and()
		.oauth2Login()
		.loginPage("/login")
		.successHandler(googleOAuth2SuccessHandler)
		.and()
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login")
		.invalidateHttpSession(true)
		.deleteCookies("JSESSIONID")
		.and()
		.exceptionHandling()
		.and()
		.csrf()
		.disable();
		
		httpSecurity.headers().frameOptions().disable();
		
		return httpSecurity.build();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(customUserDetailService);
	}
	
	
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().requestMatchers("/resources/**", "/static/**","/images/**","/productImages/**","/css/**","/js/**");
	}
	
	
	
	

}
