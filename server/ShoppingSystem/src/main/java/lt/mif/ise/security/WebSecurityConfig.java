package lt.mif.ise.security;

import lt.mif.ise.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EnableGlobalMethodSecurity(prePostEnabled = true) //using global method security we can authorize requests using @PreAuthorize annotation. Example: @PreAuthorize("hasAnyRole('ADMIN')"
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Value("${ill.admin.password}")
    private String adminPassword;

    @Value("${ill.admin.email}")
    private String adminEmail;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
        .authorizeRequests()
        	.antMatchers("**/secured/**").authenticated()
        	.anyRequest().permitAll()
        	.and()
        .formLogin()
    		.successHandler(new RestAuthenticationSuccessHandler())
    		.failureHandler(new RestAuthenticationFailureHandler())
        	.loginProcessingUrl("/login")
        	.permitAll()
        	.and()
        .logout()
        	.logoutUrl("/logout")
    		.logoutSuccessHandler(new RestLogoutSuccessHandler())
        	.permitAll()
        	.and()
        .csrf().disable();
        	
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        auth.inMemoryAuthentication().withUser(adminEmail).password(bCryptPasswordEncoder.encode(adminPassword)).roles("ADMIN");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
