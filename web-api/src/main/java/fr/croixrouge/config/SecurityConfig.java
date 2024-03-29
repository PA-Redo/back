package fr.croixrouge.config;

import fr.croixrouge.domain.model.Operations;
import fr.croixrouge.domain.model.Resources;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.crypto.SecretKey;
import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtAuthorizationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        List<RequestMatcher> excludedFilterUrl = List.of(
                new AntPathRequestMatcher("/login/volunteer"),
                new AntPathRequestMatcher("/login/beneficiary"),
                new AntPathRequestMatcher("/login/sendEmailForSupport"),
                new AntPathRequestMatcher("/login/token"),
                new AntPathRequestMatcher("/volunteer/register"),
                new AntPathRequestMatcher("/volunteer/token"),
                new AntPathRequestMatcher("/beneficiaries/register"),
                new AntPathRequestMatcher("/beneficiaries/token"),
                new AntPathRequestMatcher("/v3/api-docs"),
                new AntPathRequestMatcher("/create-account/confirm"),
                new AntPathRequestMatcher("/localunit", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.name()),
                new AntPathRequestMatcher("/chat/**")
        );

        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests( auth -> {
                auth
                        .requestMatchers("/login/volunteer").permitAll()
                        .requestMatchers("/login/beneficiary").permitAll()
                        .requestMatchers("/volunteer/register").permitAll()
                        .requestMatchers("/login/sendEmailForSupport").permitAll()
                        .requestMatchers("/v3/api-docs").permitAll()
                        .requestMatchers("/beneficiaries/register").permitAll()
                        .requestMatchers("/create-account/confirm").permitAll()
                        .requestMatchers("/localunit", HttpMethod.GET.name()).permitAll()
                        .requestMatchers("/chat/**").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll() //allow CORS option calls
                    .anyRequest().authenticated();
            })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RessourceFilter(new AntPathRequestMatcher("/volunteer/**"), excludedFilterUrl, Resources.VOLUNTEER), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RessourceFilter(new AntPathRequestMatcher("/resources/**"), excludedFilterUrl, Resources.RESOURCE), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RessourceFilter(new AntPathRequestMatcher("/event/**"), excludedFilterUrl, Resources.EVENT), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RessourceFilter(new AntPathRequestMatcher("/product/**"), excludedFilterUrl, Resources.PRODUCT), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RessourceFilter(new AntPathRequestMatcher("/storage/**"), excludedFilterUrl, Resources.STORAGE), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RessourceFilter(new AntPathRequestMatcher("/role/**"), excludedFilterUrl, Resources.ROLE), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RessourceFilter(new AntPathRequestMatcher("/localunit/**"), excludedFilterUrl, Resources.LOCAL_UNIT), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RessourceFilter(new AntPathRequestMatcher("/beneficiaries/**"), excludedFilterUrl, Resources.BENEFICIARY), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new RessourceFilter(new AntPathRequestMatcher("/product-limit/**"), excludedFilterUrl, Resources.PRODUCT_LIMIT), UsernamePasswordAuthenticationFilter.class)

            .addFilterAfter(new OperationFilter( new AntPathRequestMatcher("/**", HttpMethod.GET.name()), excludedFilterUrl, Operations.READ), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new OperationFilter( new AntPathRequestMatcher("/**", HttpMethod.DELETE.name()), excludedFilterUrl, Operations.DELETE), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new OperationFilter( new AntPathRequestMatcher("/**", HttpMethod.PUT.name()), excludedFilterUrl, Operations.UPDATE), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new OperationFilter( new AntPathRequestMatcher("/**", HttpMethod.PATCH.name()), excludedFilterUrl, Operations.UPDATE), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new OperationFilter( new AntPathRequestMatcher("/**", HttpMethod.POST.name()), excludedFilterUrl, Operations.CREATE), UsernamePasswordAuthenticationFilter.class);

//            .logout()
//            .logoutUrl("/logout")
//            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
        return http.build();
    }
}
