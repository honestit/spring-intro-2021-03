package com.projects.ams.requests;

import com.projects.ams.validation.constraints.StrongPassword;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.pl.PESEL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class RegisterUserRequest {

    /*
    Etapy weryfikacji wartości na przykładzie przelewu bankowego:

    1. Czy wartość przelewu jest liczbą? -> konwersja z wartości tekstowej

    2. Czy wartość ma poprawny format? Czy wartość jest poprawna poza kontekstem?
       Czy kwota przelewu jest co najmniej 1 zł i nie więcej niż 1 000 000 zł -> adnotacje walidacyjne

    3. Czy wartość jest poprawna w kontekście? Czy kwota przelewu zawiera się w dostępnych środkach na koncie?
       -> weryfikacja wewnątrz kodu, np. w zderzeniu z danymi w bazie danych
     */

    @NotBlank
    @Size(min = 3, max = 12)
    @Pattern(regexp = "[a-z][0-9a-z]{2,11}")
    private String username;
    @StrongPassword
    private String password;
    @Size(min = 3)
    private String firstName;
    @Size(min = 3)
    private String lastName;
    @Email
    private String email;
    @PESEL
    private String pesel;
    @Past
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd") // konwertowanie tekstu do daty
    private LocalDate birthDate;
    @Range(min = 100, max = 220)
    private Integer height;
}
