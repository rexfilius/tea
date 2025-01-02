package github.com.rexfilius.tea.modules.security.jwt;

public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    @Override
    public String toString() {
        return "JwtAuthResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
