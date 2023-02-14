package com.exam.config;

import com.exam.service.impl.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//port org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;

@Configuration
@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)
public class MySecurityConfig{
	
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	  @Bean
	  public DaoAuthenticationProvider authenticationProvider() {
	      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	
	      System.out.println("testing");
	      authProvider.setUserDetailsService(userDetailsServiceImpl);
	      authProvider.setPasswordEncoder(passwordEncoder());
	   
	      return authProvider;
	  }
	
  @Bean
  public JwtAuthenticationFilter authenticationJwtTokenFilter() {
    return new JwtAuthenticationFilter();
  }
	
//	@Bean
//	protected AuthenticationManager authenticationManagerBean() throws Exception {
//		//return null;
//		return super.authenticationManagerBean();
//	}
	@Bean
	protected AuthenticationManager authenticationManager(AuthenticationConfiguration AuthenticationConfiguration) throws Exception {
		return AuthenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		System.out.println("Configure ----------");
//		http
//		     .csrf()
//		     .disable()
//		     .cors()
//		     .disable()
//		     .authorizeHttpRequests()
//		     .requestMatchers("/generate-token","/user").permitAll()
//		     .requestMatchers(HttpMethod.OPTIONS).permitAll()
//		     .anyRequest().authenticated()
//		     .and()
//		     .exceptionHandling().authenticationEntryPoint(unauthorizedHandler) 
//		     .and()
//		     .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		 http.cors().and().csrf().disable()
	        .exceptionHandling().authenticationEn+tryPoint(unauthorizedHandler).and()
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	        .authorizeRequests().requestMatchers("/user").permitAll()
	        .requestMatchers("/api/test/**").permitAll()
	        .anyRequest().authenticated();
		
		http.authenticationProvider(authenticationProvider());
		
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	
		return http.build(); 
	}
	
//	protected void configure(AuthenticationManager auth) throws Exception {
//		((AuthenticationManagerBuilder) auth).userDetailsService(this.userDetailsServiceImpl).passwordEncoder(passwordEncoder());
//	}
	
}
