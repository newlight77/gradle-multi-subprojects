package io.github.newlight77.core.security

//import org.keycloak.adapters.KeycloakConfigResolver
//import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@KeycloakConfiguration
@Profile(value = ["prod", "dev"])
class KeycloakConfiguration: KeycloakWebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var oktaJwtVerifier: OktaJwtVerifier
//
//    @Bean
//    fun keycloakConfigResolver(): KeycloakConfigResolver {
//        return KeycloakSpringBootConfigResolver()
//    }

    @Autowired
    @Throws(java.lang.Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(keycloakAuthenticationProvider())
    }

    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy? {
        return NullAuthenticatedSessionStrategy()
//        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    @Bean
    fun keycloakAuthenticationProcessingFilterRegistrationBean(
            filter: KeycloakAuthenticationProcessingFilter?): FilterRegistrationBean<*>? {
        val registrationBean: FilterRegistrationBean<*> = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    @Bean
    fun keycloakPreAuthActionsFilterRegistrationBean(filter: KeycloakPreAuthActionsFilter?): FilterRegistrationBean<*>? {
        val registrationBean: FilterRegistrationBean<*> = FilterRegistrationBean(filter)
        registrationBean.isEnabled = false
        return registrationBean
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)

        //@formatter:off
        http
                .cors() //
                .configurationSource { //
                    CorsConfiguration().applyPermitDefaultValues() //
                } //
                .and() //
                .anonymous().disable() //
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy()) //

                // delegate logout endpoint to spring security
                .and()
                .logout().addLogoutHandler(keycloakLogoutHandler())
                .logoutUrl("/logout")

                .and() //
                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter::class.java) //
                .addFilterBefore(keycloakAuthenticationProcessingFilter(), X509AuthenticationFilter::class.java) //
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()) //

                .and().authorizeRequests() //
                .antMatchers("/**").authenticated()
                .antMatchers("/users*").hasRole("USER") //
                .antMatchers("/admin*").hasRole("ADMIN") //
                .anyRequest().denyAll() //


        http.requiresChannel()
                .requestMatchers(RequestMatcher {
                    r -> r.getHeader("XSRF-TOKEN") != null
                }).requiresSecure()

//                .csrf().disable() //
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        http.addFilterAfter(CsrfHeaderFilter(), UsernamePasswordAuthenticationFilter::class.java)

        http.addFilterAfter(JwtAuthorizationFilter(oktaJwtVerifier), UsernamePasswordAuthenticationFilter::class.java)
        //@formatter:on
    }


}