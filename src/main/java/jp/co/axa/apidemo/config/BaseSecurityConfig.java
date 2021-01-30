package jp.co.axa.apidemo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class BaseSecurityConfig  extends WebSecurityConfigurerAdapter {

    /**
     * global Security configuration goes here. Currently no security
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated() // any request allowed if user is authenticated
                .and()
                .httpBasic() // use basic auth instead of any token based authentication methods
                .and()
                .antMatcher("/api/*")
                .csrf()
                .disable(); // disable csrf to enable simple api calls

    }
}
