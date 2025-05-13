package com.hcmute.myanime.security;

import com.hcmute.myanime.auth.ApplicationUserService;
import com.hcmute.myanime.filter.CORSFilter;
import com.hcmute.myanime.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.hcmute.myanime.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
public class  ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationUserService applicationUserService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/", "/authentication/**",
                            "/movie-and-series/**", "/episode/**", "/category/**", "/category-movie/**", "/statistics/**",
                            "/comment/**",
                            "/payment/**",
                            //Các API từ đây sẽ bị xóa và chuyển về has role khi xong
                            "/test/api/videoUpload",
                            "/subscription-package/**",
                            "/account/**",
                            //swagger UI doc
                            "/csrf",
                            "/v3/api-docs",
                            "/configuration/ui",
                            "/swagger-resources",
                            "/swagger-resources/configuration/security",
                            "/swagger-resources/configuration/ui",
                            "/configuration/security",
                            "/swagger-ui/**",
                            "/webjars/**",
                            //Test API
                            "/test/**").permitAll()
                    .antMatchers("/user/**").hasAnyRole(ADMIN.name(), USER.name()) //Các API cần đăng nhập bằng tk admin, user
                    .antMatchers(
                            "/admin/**"
                    ).hasRole(ADMIN.name()) //Các API cần đăng nhập bằng tk admin
                    .antMatchers().hasRole(USER.name()) //Các API cần đăng nhập bằng tk user
                .anyRequest()
                    .authenticated() //Other API need authorized
                .and()
                    .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }
}
