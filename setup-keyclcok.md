# Keycloak realm setup (quick reference)

Short: this file shows the exact realm, client, and mapper settings to configure Keycloak so the services in this repo can use it. It assumes Keycloak is already installed and reachable (example: http://localhost:8080).

---

## Assumptions
- Keycloak is running and you can access the Admin Console (e.g. http://localhost:8080).
- You'll create a realm named `blog-realm`.
- Services will validate JWTs using the realm's issuer: `http://<KEYCLOAK_HOST>:8080/realms/blog-realm`.
- Example client ids/secrets in this doc are for local testing only. Replace with strong secrets in prod.

## Quick checklist
- [ ] Create realm `blog-realm`
- [ ] Create client `api-gateway` (confidential)
- [ ] Create client `sb-internal-client` (client credentials)
- [ ] Configure roles and mapper for `aud` / audience if needed
- [ ] Use the example property values below in service `application.properties` files or env vars

## Realm creation
1. Login to Keycloak Admin Console.
2. Click `Add realm` → enter `blog-realm` → Save.

## Clients
Below are example client configurations. Use the Admin Console → Clients → Create.

### api-gateway (OIDC login / gateway client)
- Client ID: `api-gateway`
- Client Protocol: `openid-connect`
- Access Type: `confidential`
- Standard Flow Enabled: `ON` (if you use browser login)
- Direct Access Grants: `OFF` (unless you need Resource Owner Password creds)
- Valid Redirect URIs: `http://localhost:8084/*` (adjust to your gateway host)
- Web Origins: `+` (or restrict to gateway origin)
- Credentials → note `Secret` (example below: `gateway-secret`)

### sb-internal-client (service-to-service)
- Client ID: `sb-internal-client`
- Client Protocol: `openid-connect`
- Access Type: `confidential`
- Service Accounts Enabled: `ON`
- Authorization Settings: leave defaults
- Credentials → note `Secret` (example below: `sb-internal-secret`)

## Optional: audience / resource configuration
Spring resource-server checks `iss` and optionally `aud`. If you require `aud` to match, add a client or configure token mappers:
1. In `Clients` → select a client (e.g. `api-gateway`) → `Client Scopes` or `Mappers`.
2. Add a mapper that sets `aud` (audience) claim if required by your services.
   - Mapper Type: `User Property` or `Hardcoded Claim` depending on your Keycloak version
   - Token Claim Name: `aud`
   - Claim Value: `account` or custom value your services expect

## Roles & Scopes (simple)
- Define realm roles if you need role-based access. Add roles under `Roles` and map them to clients or users.

## Protocol mappers (example: include email and roles)
- Clients → select client → `Mappers` → `Create`:
  - Name: `email`
  - Mapper Type: `User Property`
  - Property: `email`
  - Token Claim Name: `email`
  - Add to ID token / access token: ON

  - Name: `realm-roles`
  - Mapper Type: `Realm Role`
  - Token Claim Name: `roles`
  - Add to access token: ON

## Example credentials (local dev only)
- Keycloak host: `http://localhost:8080`
- Realm: `blog-realm`
- api-gateway client id/secret: `api-gateway` / `gateway-secret`
- sb-internal-client id/secret: `sb-internal-client` / `sb-internal-secret`

## Exact Spring Boot properties to paste
Use these lines in each service `src/main/resources/application.properties` or set them via environment variables.

For resource servers (all services validating tokens):

spring.security.oauth2.resourceserver.jwt.issuer-uri=http://192.168.1.2:8080/realms/blog-realm

For optional internal client (service→service client credentials):

spring.security.oauth2.client.registration.my-internal-client.provider=keycloak
spring.security.oauth2.client.registration.my-internal-client.authorization-grant-type=client_credentials
spring.security.oauth2.client.registration.my-internal-client.scope=internal
spring.security.oauth2.client.registration.my-internal-client.client-id=sb-internal-client
spring.security.oauth2.client.registration.my-internal-client.client-secret=sb-internal-secret
spring.security.oauth2.provider.keycloak.issuer-uri=http://192.168.1.2:8080/realms/blog-realm

For API Gateway client registration (if gateway needs to act as an OAuth client):

spring.security.oauth2.client.registration.gateway-client.client-id=api-gateway
spring.security.oauth2.client.registration.gateway-client.client-secret=gateway-secret
spring.security.oauth2.client.registration.gateway-client.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.gateway-client.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
spring.security.oauth2.client.provider.keycloak.issuer-uri=http://192.168.1.2:8080/realms/blog-realm

> Prefer environment variables in production, for example:
> SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER-URI, SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_MY-INTERNAL-CLIENT_CLIENT_ID, etc.

## How to get a token (client credentials)
Replace client id/secret if you used different values.

Example curl (get access token for sb-internal-client):

```bash
curl -s -X POST \
  "http://192.168.1.2:8080/realms/blog-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&client_id=sb-internal-client&client_secret=sb-internal-secret"
```

Response will include `access_token`; use it as `Authorization: Bearer <token>`.

## Verifying tokens
- Inspect token at https://jwt.io using the realm's public keys (JWKs) endpoint:
  - JWKs URL: `http://192.168.1.2:8080/realms/blog-realm/protocol/openid-connect/certs`
- Or call Keycloak userinfo: `http://192.168.1.2:8080/realms/blog-realm/protocol/openid-connect/userinfo` with the token.

## Troubleshooting quick tips
- 401 from service: check `issuer-uri` and clock skew between Keycloak and services.
- `aud` mismatch: adjust token mapper to add expected audience claim or disable audience check in service config.
- Gateway login not working: ensure `redirect_uri` registered in client and correct `spring.security.oauth2.client.registration.gateway-client.redirect-uri` matches.

## Security notes
- Do NOT keep client secrets in source control. Use environment variables or a secrets manager.
- Use HTTPS for Keycloak in production and a secure secret rotation policy.

## Next steps I can do for you
- Convert these hardcoded example values to environment-variable-based properties across the repo.
- Add a minimal `docker-compose` service to start a quick local Keycloak with the realm preconfigured.
- Run `mvn package` and fix any compile issues introduced by the config changes.

---

If you want I can now: (A) replace the example secrets with env-var references across all services, or (B) generate a small `docker-compose.keycloak.yml` that preloads the `blog-realm` using Keycloak import. Tell me which. 
