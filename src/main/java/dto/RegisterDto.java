package dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    public RegisterDto(String firstName, String lastName, String email, String password){
        this.firstName = firstName;
        this.lastName =lastName;
        this.password = password;
        this.email = email;
    }
}
