package com.ltp.contacts.security;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
public class SecurityConfiguration {

    // Basic Auhentication

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((sec)->sec.disable())
                .authorizeHttpRequests((authz) ->
                        authz.requestMatchers(HttpMethod.DELETE,"/contact/admin/*").hasRole("ADMIN").
                                requestMatchers(HttpMethod.POST).hasAnyRole("USER","ADMIN").
                                requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest().
                        authenticated()
                )
                .httpBasic(Customizer.withDefaults()).
                sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


    @Bean
    public UserDetailsService users(){

        UserDetails admin=User.
                builder().
                username("admin").
                password(bCryptPasswordEncoder.encode("pass")).
                roles("ADMIN").build();

        UserDetails user=User.
                builder().
                username("user").
                password(bCryptPasswordEncoder.encode("pass")).
                roles("USER").build();

        return new InMemoryUserDetailsManager(admin,user);

    }



}
