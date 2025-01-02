package github.com.rexfilius.tea.modules.auth.model;

public class VerifyUserDto {
    private String email;
    private String verificationCode;

    public VerifyUserDto(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public VerifyUserDto() {
    }

    @Override
    public String toString() {
        return "VerifyUserDto{" +
                "email='" + email + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
