Spring Boot client app that integrates with Keycloak as the Identity Provider (IdP). This will let you test Federated SSO locally. I’ll walk you through step by step.
 
🛠️ Prerequisites
- Keycloak running locally (e.g., via Docker on http://localhost:8080)
- Java 17+
- Spring Boot 3.x
- Maven or Gradle


⚙️ Step 1: Configure Keycloak
IdP: KeyCloak
Installation:
docker run --name mykeycloak -p 127.0.0.1:8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev


- Log into Keycloak Admin Console (http://localhost:8080).
- Create a Realm (e.g., demo-realm).
- Create a Client:
- Client ID: spring-client
- Client Protocol: openid-connect
- Access Type: confidential
- Valid Redirect URIs: http://localhost:8081/*
- Save and copy the Client Secret.
- Create a test User in the realm and set a password.


📦 Step 2: Spring Boot Dependencies
Add these to your pom.xml:
spring-boot-starter-webspring-boot-starter-security
spring-boot-starter-oauth2-client

📝 Step 3: Configure application.yml with keycloak configuration 
server.port: 8081
spring.security.oauth2.client.registration.keycloak.client-id: YOUR_CLIENT_ID
spring.security.oauth2.client.registration.keycloak.client-secret: YOUR_CLIENT_SECRET
spring.security.oauth2.client.registration.keycloak.scope: openid,profile,email
spring.security.oauth2.client.registration.keycloak.authorization-grant-type: authorization_code
spring.security.oauth2.client.registration.keycloak.redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
spring.security.oauth2.client.provider.keycloak.issuer-uri: http://localhost:8080/realms/<your-realm-name>


🔐 Step 4: Security Configuration
Create a SecurityConfig class:
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/public").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(); // Enable OAuth2 login with Keycloak
        return http.build();
    }
}

🌐 Step 5: Controller
    @GetMapping("/")
    public String home() {
        return "Welcome! <a href='/private'>Go to private page</a>";
    }

    @GetMapping("/private")
    public String privatePage(@AuthenticationPrincipal OidcUser user) {
        return "Hello " + user.getFullName() + "! Your email: " + user.getEmail();
    }

▶️ Step 6: Run & Test
- Start your Spring Boot app (mvn spring-boot:run).
- Visit http://localhost:8081/private.
- You’ll be redirected to Keycloak login.
- After login, you’ll see your profile info returned by Keycloak





