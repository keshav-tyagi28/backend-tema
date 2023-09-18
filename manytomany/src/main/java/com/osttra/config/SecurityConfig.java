//
//package com.osttra.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.osttra.helper.AuthEntryPointJwt;
//import com.osttra.service.CustomUserDetailsService;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig  {   //extends WebSecurityConfigurerAdapter
//	
//	@Autowired
//	CustomUserDetailsService customUserDetailsService;
//	
//	@Autowired
//	JWTAuthenticationFilter jwtAuthenticationFilter;
//	
//	@Autowired
//	private AuthEntryPointJwt unauthorizedHandler;
//
//	  @Bean
//	  public JWTAuthenticationFilter authenticationJwtTokenFilter() {
//	    return new JWTAuthenticationFilter();
//	  }
//	  
//	  @Bean
//	  public DaoAuthenticationProvider authenticationProvider() {
//	      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//	       
//	      authProvider.setUserDetailsService(customUserDetailsService);
//	      authProvider.setPasswordEncoder(passwordEncoder());
//	   
//	      return authProvider;
//	  }
//
//	  
//	  @Bean
//	  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//	    return authConfig.getAuthenticationManager();
//	  }
//
//	
////	protected void configure(HttpSecurity http) throws Exception {
////		System.out.println("inside first configurer...");
////		http
////			.csrf().disable()
////				.authorizeHttpRequests()
////				.antMatchers("/check").hasRole("Admin")
////				.antMatchers("/registeruser").permitAll()
////				.antMatchers("/token").permitAll()
////					.anyRequest()
////						.authenticated();
////							
////								
////							http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
////	}
////	
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		System.out.println("inside second configurer...1");
//		auth.userDetailsService(customUserDetailsService).passwordEncoder(this.passwordEncoder());
//
//	}
//	
//
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		
//		System.out.println("inside Bcrypt password Encoder...");
//		
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean
//	public AuthenticationManager authenticationManager() throws Exception {
//		return super.authenticationManager();
//	}
//	
//	
//		
//	
//	  @Bean
//	  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//	    http.csrf(csrf -> csrf.disable())
//	        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//	        .authorizeHttpRequests(auth -> 
//	          auth.antMatchers("/api/auth/**").permitAll()
//	              .antMatchers("/api/test/**").permitAll()
//	              .antMatchers("/check").hasRole("Admin")
//	              .anyRequest().authenticated()
//	        );
//	   
//	    
//	    http.authenticationProvider(authenticationProvider());
//
//	    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//	    
//	    return http.build();
//	  }
//	}
//	
//	

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
package com.osttra.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osttra.helper.AuthEntryPointJwt;
import com.osttra.service.CustomUserDetailsService;


@Configuration
@EnableMethodSecurity
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class SecurityConfig { // extends WebSecurityConfigurerAdapter {
  @Autowired
  CustomUserDetailsService userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public JWTAuthenticationFilter authenticationJwtTokenFilter() {
    return new JWTAuthenticationFilter();
  }

//  @Override
//  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//  }
  
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
      return (request, response, e) -> {
          response.setContentType("application/json");
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);

          // Customize the JSON response as needed
          Map<String, String> responseBody = new HashMap<>();
          responseBody.put("message", "Access denied. You do not have the required role to access this resource.");

          ObjectMapper objectMapper = new ObjectMapper();
          objectMapper.writeValue(response.getWriter(), responseBody);
      };
  }
  
//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

 @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return NoOpPasswordEncoder.getInstance();
//  }

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.cors().and().csrf().disable()
//      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//      .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//      .antMatchers("/api/test/**").permitAll()
//      .anyRequest().authenticated();
//
//    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//  }
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
    .exceptionHandling(exception -> exception
    	    .authenticationEntryPoint(unauthorizedHandler)
    	    .accessDeniedHandler(accessDeniedHandler())
    	)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> 
          auth.antMatchers("/api/auth/**").permitAll()
              .antMatchers("/api/test/**").permitAll()
              .antMatchers("/signin").permitAll()
              .antMatchers("/registeruser").permitAll()
              .antMatchers("/all").hasRole("Customer")
              .antMatchers("/update/**").permitAll()
              .anyRequest().authenticated()
        );
    
//	http
//	.csrf().disable()
//		.authorizeHttpRequests()
//		.antMatchers("/token").permitAll()
//			.anyRequest()
//				.authenticated()
//					.and()
//						.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
    
    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
}


//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//	
//	@Autowired
//	CustomUserDetailsService customUserDetailsService;
//	
//	@Autowired
//	JWTAuthenticationFilter jwtAuthenticationFilter;
//	
//	@Autowired
//	private AuthEntryPointJwt unauthorizedHandler;
//	
//
//	
//	protected void configure(HttpSecurity http) throws Exception {
//		System.out.println("inside first configurer...");
//		http
//			.csrf().disable()
//				.authorizeHttpRequests()
//				.antMatchers("/token").permitAll()
//					.anyRequest()
//						.authenticated()
//							.and()
//								.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		
//		http.authenticationProvider(entryPointHelper);
//		
//		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//	}
//	
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		System.out.println("inside second configurer...1");
//
////		auth.inMemoryAuthentication().withUser("Rishika").password(this.passwordEncoder().encode("123")).roles("Admin");
////		auth.inMemoryAuthentication().withUser("Parv").password(this.passwordEncoder().encode("234")).roles("Student");
//		
//		auth.userDetailsService(customUserDetailsService).passwordEncoder(this.passwordEncoder());
//
//	}
//	
////	@Bean
////	public NoOpPasswordEncoder passwordEncoder() {
////		
////		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
////	}
//	
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		
//		System.out.println("inside Bcrypt password Encoder...");
//		
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean
//	public AuthenticationManager authenticationManager() throws Exception {
//		return super.authenticationManager();
//	}
//	
////	public BCryptPasswordEncoder passwordEncoder() {
////		
////		System.out.println("isndie Bcrypt...");
////		
////		return new BCryptPasswordEncoder();
////	}
//
//}
//
//
//
//
////String suhailPasswordEncoded = this.passwordEncoder().encode("Ali");
////System.out.println("suhailPasswordEncoded " + suhailPasswordEncoded);
////
////String shaliniEncodedPassword = this.passwordEncoder().encode("Singh");
////System.out.println("shaliniEncodedPassword "+shaliniEncodedPassword);
//
////auth.userDetailsService(customUserDetailsService).passwordEncoder(this.passwordEncoder());
////System.out.println("inside configurer...2");
//
//
////<------------------------
//
////@Configuration
////@EnableWebSecurity
////public class SecurityConfig extends WebSecurityConfigurerAdapter {
////	
////	@Autowired
////	CustomUserDetailsService customUserDetailsService;
////	
//////	protected void configure(HttpSecurity http) throws Exception {
//////		System.out.println("isnide first configurer...");
//////		http
//////			.authorizeHttpRequests()
////////				.antMatchers("/home", "/welcome").permitAll()
////////					.antMatchers("/registration_page").hasRole("Admin")
//////			.antMatchers("/user/**").permitAll()
//////			
//////					.anyRequest()
//////						.authenticated()
//////							.and()
//////								.httpBasic();
//////	}
////	
////	protected void configure(HttpSecurity http) throws Exception {
////		System.out.println("inside first configurer...");
////		http
////			.csrf().disable()
////				.authorizeHttpRequests()
////				.antMatchers("/signin", "/registration_page", "/register").permitAll()
////					.anyRequest()
////						.authenticated()
////							.and()
////								.formLogin()
////									.loginPage("/signin")
////										.loginProcessingUrl("/dologin")
////											.defaultSuccessUrl("/welcomePage", true);
////	}
////	
////	
////	
////	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////		System.out.println("inside second configurer...1");
////
//////		auth.inMemoryAuthentication().withUser("Rishika").password(this.passwordEncoder().encode("123")).roles("Admin");
//////		auth.inMemoryAuthentication().withUser("Parv").password(this.passwordEncoder().encode("234")).roles("Student");
////		
////		auth.userDetailsService(customUserDetailsService).passwordEncoder(this.passwordEncoder());
////
////	}
////	
//////	@Bean
//////	public NoOpPasswordEncoder passwordEncoder() {
//////		
//////		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//////	}
////	
////	@Bean
////	public BCryptPasswordEncoder passwordEncoder() {
////		
////		System.out.println("inside Bcrypt password Encoder...");
////		
////		return new BCryptPasswordEncoder();
////	}
////	
////	@Bean
////	public AuthenticationManager authenticationManager() throws Exception {
////		return super.authenticationManager();
////	}
////	
//////	public BCryptPasswordEncoder passwordEncoder() {
//////		
//////		System.out.println("isndie Bcrypt...");
//////		
//////		return new BCryptPasswordEncoder();
//////	}
////
////}
////
////
////
////
//////String suhailPasswordEncoded = this.passwordEncoder().encode("Ali");
//////System.out.println("suhailPasswordEncoded " + suhailPasswordEncoded);
//////
//////String shaliniEncodedPassword = this.passwordEncoder().encode("Singh");
//////System.out.println("shaliniEncodedPassword "+shaliniEncodedPassword);
////
//////auth.userDetailsService(customUserDetailsService).passwordEncoder(this.passwordEncoder());
//////System.out.println("inside configurer...2");




