export const authConfig = {
    clientId: 'fitness-oauth2-client',
    authorizationEndpoint: 'http://localhost:8084/realms/fitness-oauth2/protocol/openid-connect/auth',
    tokenEndpoint: 'http://localhost:8084/realms/fitness-oauth2/protocol/openid-connect/token',
    redirectUri: 'http://localhost:8085',
    scope: 'openid profile email offline_access',
    onRefreshTokenExpire: (event) => event.logIn(),
}