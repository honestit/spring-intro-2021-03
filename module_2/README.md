[< Powrót](../README.md)

[Wstecz](../module_1/README.md) | [Dalej](../module_3/README.md)

## Moduł 2. Proces rejestracji i logowania

Mamy podstawową konfigurację projektu i udało nam się go uruchomić. Będziemy realizować teraz kolejne funkcjonalności i zaczniemy od rejestracji użytkownika oraz procesu logowania/wylogowania.

Potrzebować będziemy:
- klasy reprezentującej użytkownika, np. `User`,
- klasy kontrolera Spring MVC obsługującej proces rejestracji, np. `RegistrationController`,
- strony html z widokiem formularza rejestracji,
- strony html z widokiem formularza logowania i jej obsługi w kontrolerze.

Dodatkowo potrzebne nam będzie sporo zmian w konfiguracji Spring Security!

---

### Zadanie 2.1. Klasa użytkownika

Nasza klasa `User` powinna spełniać następujące wymagania:
- jest zmapowana do tabeli `users`,
- jest encją,
- posiada pojedyncze pole klucza głównego typu `Long`, którego wartość jest automatycznie generowana według mechanizmu bazodanowego (np. `AUTO INCREMENT`),
- posiada pole `username` reprezentujące nazwę użytkownika, które musi być unikalne w bazie i nie może być puste,
- posiada pole `password` reprezentujące hasło, które nie może być puste,
- posiada pole `firstName` reprezentujące imię użytkownika, które nie może być puste, zmapowane na kolumnę `first_name`,
- posiada pole `lastName` reprezentujące nazwisko użytkownika, które nie może być puste, zmapowane na kolumnę `last_name`,
- posiada pole `active` reprezentujące informację czy użytkownik jest aktywny, które nie może być puste.

> Pamiętajmy również o metodach `get/set` dla każdego z pól oraz o metodach `equals`, `hashCode` i `toString`.

> Uwaga! Do wygenerowania metod `get/set` możesz skorzystać z adnotacji `@Getter` oraz `@Setter` z biblioteki `Lombok`. Metod `equals`, `hashCode` oraz `toString` nie twórz w ten sposób.

Klasę `User` należy stworzyć w pakiecie `pl.honestit.spring.demo.model.domain`.

Po utworzeniu klasy możemy uruchomić aplikację. Ze względu na opcję `spring.jpa.hibernate.ddl-auto=update` w pliku `application.properties` Hibernate automatycznie utworzy tabele dla naszej encji oraz wszystkie wymagane kolumny z ich regułami. Możesz to zweryfikować wchodząc na bazę danych i sprawdzając czy taka tabela powstała. Informacja o jej tworzeniu (SQL) powinna być również widoczna w logach aplikacji przy jej starcie.


---

### Zadanie 2.2. Szkielet kontrolera Spring MVC

Kontrolery Spring MVC są lżejszymi odpowiednikami Servletów. Podobnie jak Servlety są zmapowane do konkretnych ścieżek, ale umożliwiają bardziej dokładne mapowanie, zależne od wielu więcej elementów niż sama ścieżka.

Każdy kontroler w Spring MVC musi posiadać adnotację `@Controller`, która wskazuje go jako komponent typu kontroler. Dzięki tej adnotacji klasa naszego kontrolera zostanie automatycznie wykryta, a jej obiekt będzie zarządzany przez Spring'a.

Obsługa konkretnych żądań realizowana jest przez metody z adnotacją `@RequestMapping` albo dedykowanymi adnotacjami `@PostMapping`,`@GetMapping`, `@PutMapping` itd. Adnotacja `@RequestMapping` może pojawić się również nad całą klasą kontrolera, wówczas każda metoda jest połączeniem ścieżki określonej w adnotacji nad klasę kontrolera i ścieżki określonej w adnotacji nad konkretną metodą.

W naszym przypadku będziemy chcieli mieć kontroler, któy obsługuję proces rejestracji, a więc klasę `RegistrationController`. Klasę umieścimy w pakiecie `pl.honestit.spring.demo.controllers`. Kontroler zmapujemy na ścieżkę `/register`, a w nim dostarczymy dwie metody.  Pierwsza z nich będzie obsługiwała żądanie typu `GET` na ścieżkę kontrolera, a druga żądanie typu `POST` na tą samą ścieżkę.

Przykładowy szkielet naszego kontrolera:

```java
@Controller
@RequestMapping("/register")
public class RegistrationController {

    @GetMapping
    public String prepareRegistrationPage() {
        // TODO
        return "";
    }

    @PostMapping
    public String processRegistrationPage() {
        // TODO
        return "";
    }
}
``` 

Obie metody zwracają wartość typu `String`, która ma reprezentować identyfikator widoku. Widok będzie naszym szablonem Thymeleaf, który posłuży do wygenerowania kodu HTML.

> Stronę html tworzymy w kolejnym zadaniu

---

### Zadanie 2.3. Widok formularza rejestracji

Nasz formularz rejestracji będzie prostą stroną html (szablon Thymeleaf), zawierającą pojedynczy formularz. W formularzu znajdą się wszystkie pola wymagane w procesie rejestracji, a więc:
- nazwa użytkownika,
- hasło,
- imię,
- nazwisko.

Formularz można przygotować samodzielnie albo skorzystać z kodu poniżej. Sam plik nazwijmy `registration-form.html`.

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Rejestracja użytkownika</title>
</head>
<body>
<h1>Zarejestruj nowe konto!</h1>
<div>
    <form method="post">
        <p>
            <label>Nazwa użytkownika:
                <input type="text" name="username" id="username" value=""/>
            </label>
        </p>
        <p>
            <label>Imię:
                <input type="text" name="firstName"/>
            </label>
        </p>

        <p>
            <label>Nazwisko:
                <input type="text" name="lastName"/>
            </label>
        </p>
        <p>
            <label>Hasło:
                <input type="password" name="password"/>
            </label>
        </p>
        <p>
            <input type="hidden"
                   th:name="${_csrf.parameterName}"
                   th:value="${_csrf.token}"/>
            <button type="submit">Zarejestruj</button>
        </p>
    </form>
</div>
</body>
</html>
```

Plik tworzymy w katalogu `/templates`.

Możemy teraz wykorzystać stronę rejestracji, tak aby zwracać jej identyfikator w metodzie `prepareRegistrationPage` kontrolera `RegistrationController`:

```java
    @GetMapping
    public String prepareRegistrationPage() {
        return "registration-form";
    }
```

Powinniśmy teraz móc wejść na ścieżkę [localhost:8080/register](http://localhost:8080/register) i zobaczyć formularz rejestracji. Oczywiście Spring Security będzie od nas wymagał, aby się wcześniej zalogować...

> Trochę dziwnie logować się, aby się zarejestrować ale już niedługo to naprawimy!

---

### Zadanie 2.4. Obsługa rejestracji

W naszym kontrolerze potrzebujemy teraz obsłużyć dane z formularza rejestracji. Wartość każdego pola z formularza przesyłana jest w formie parametrów żądania. W metodach kontrolera możemy przyjąć dowolne parametry żądania, a Spring automatycznie nam uzupełni w nich wartości. Nie musimy robić nic więcej poza podaniem typów i nazw parametrów w metodzie. Jeżeli na podstawie nazwy parametru metody uda się dopasować parametr żądania oraz typ tego parametru będzie zgodny, to wartość parametru metody zostanie automatycznie uzupełniona:

```java
    @PostMapping
    public String processRegistrationPage(String username, 
                                            String password, 
                                            String firstName, 
                                            String lastName) {
        // TODO
        return "";
    }
```

> Bardziej precyzyjne mapowania możemy osiągnać stosując adnotację `@RequestParam` przy każdym z parametrów. Przykładowo wtedy, gdy dany parametr ma być nieobowiązkowy albo gdy parametr żądania nazywa się inaczej niż parametr metody.

Na podstawie wartości przekazanych z formularza, a więc naszych parametrów metody, należy utworzyć i uzupełnić nowy obiekt klasy `User`. Wartość pola `active` ustawiamy przy rejstracji na `true`.

Kiedy obiekt będzie gotowy pojawia się pytanie jak zapisać go do bazy danych? Wykorzystamy w tym celu  _repozytorium_. Repozytoria pochodzą z projektu Spring Data, a dokładniej z jego modułu Spring Data JPA. Pozwalają nam automatycznie dostarczyć implementację dość rozbudowanego DAO w standardzie JPA dla konkretnej encji. Aby stworzyć repozytorium należy utworzyć nowy interfejs, który będzie rozszerzał sparametryzowany interfejs `JpaRepository`. Repozytoria będziemy tworzyć w pakiecie `pl.honestit.spring.demo.model.repositories`.

Nasze pierwsze repozytorium, a więc interfejs `UserRepository` będzie wyglądać jak niżej. 

```java
public interface UserRepository extends JpaRepository<User, Long> {
    
}
```

Nasz interfejs nie posiada żadnych metod, a mimo to będzie za chwilę czynił niemal cuda! Warto na tym etapie zajrzeć do kodu interfejsu `JpaRepository` i prześledzić jakie metody są w nim dostępne.

W kontrolerze `RegistrationController` możemy wykorzystać obiekt tego interfejsu korzystając z mechanizmu wstrzyknięć. Jest to realizacja wzorca _Dependency Injection_ i odbywa się na podstawie trzech wariantów:
- użycia adnotacji `@Autowired` bezpośrednio nad polem reprezentującym obiekt, który chcemy wstrzyknąć (**niezalecane**),
- użycia adnotacji `@Autowired` bezpośrednio nad metodą typu `set` dla pola, które chcemy wstrzyknąć,
- użycia adnotacji `@Autowired` bezpośrednio nad konstruktorem przyjmującym parametr dla pola, które chcemy wstrzyknąć (**zalecane**).

Użycie repozytorium w naszym kontrolerze będzie wyglądało następująco (w opcji wstrzyknięcia przez konstruktor):

```java
@Controller
@RequestMapping("/register")
public class RegistrationController {
    
    private final UserRepository userRepository;
    
    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ...
}
```

Każde repozytorium dziedziczące po `JpaRepository` posiada metodę `save`, którą możemy wykorzystać do zapisania encji. Po poprawnym zapisaniu encji w bazie, możemy przekierować dalsze żądanie na naszą tymczasową stronę główną, a więc zrobić przekierowanie w metodzie `processRegistrationPage`:

```java
        return "redirect:/";
```

Spróbujmy uruchomić aplikację i zarejestrować nowego użytkownika.

Jeżeli chcemy sprawdzić czy operacja się powiodła, to możemy:
- sprawdzić w bazie danych czy użytkownik został zapisany,
- dodać do klasy pole obiektu `Logger` i po zapisaniu użytkownika, a przed zakończeniem metody `processRegistrationPage`, _zalogować_ informację o zapisanym użytkowniku:

  ```java
  // Konfiguracja Logger'a
  private static final Logger log = LoggerFactory.getLogger(RegistrationController.java);

  // ...

  @PostMapping
  public String processRegistrationPage(String username, String password, String firstName, String lastName) {  
      // ...
      userRepository.save(user);
      log.info("Zapisany użytkownik: " + user);
    
      return "redirect:/index.html";
  }
  ```
  
> Zamiast dodawać ręcznie pole `log` możesz użyć adnotacji `@SLF4J` z biblioteki Lombok. Wtedy automatycznie będziesz miał w klasie pole `log`, które jest _loggerem_ bazującym na biblioteke _Simple Logging Facade For Java_
  
---
  
### Zadanie 2.5. Konfiguracja bezpieczeństwa dla procesu rejestracji

Domyślna konfiguracja bezpieczeństwa zakłada, że każda strona wymaga uwierzytelnienia, a więc cała nasza aplikacja jest dostępna tylko dla zalogowanych użytkowników. Jest to właściwe podejście, w którym zakładamy, że domyślne konfiguracje powinny być maksymalnie bezpieczne, a zmiany powinny polegać na świadomym ograniczaniu tego bezpieczeństwa. W naszym przypadku świadomie nie chcemy, aby strona rejestracji wymagała logowania się użytkownika ponieważ nie ma to większego sensu. Nasz użytkownik jest anonimowy i dopiero chce nawiązać współpracę z naszym serwisem.

> Konfiguracja Spring Security tylko w minimalnym stopniu odbywa się na podstawie globalnego pliku konfiguracyjnego `application.properties`. Głównie dlatego, że jest zbyt rozbudowana.

Wprowadzenie własnej konfiguracji bezpieczeństwa wymaga od nas dostarczenia nowej klasy, która spełni dwa założenia:
- będzie oznaczona adnotacją `@Configuration`, aby Spring mógł ją automatycznie wykryć i obsłużyć,
- będzie rozszerzać klasę `WebSecurityConfigurerAdapter`.

Naszą klasę konfiguracyjną nazwijmy `SecurityConfiguration` i umieśćmy w pakiecie `pl.honestit.spring.demo.config`. Nadpiszemy w niej metodę `void configure(HttpSecurity)`. W tej metodzie wprowadzimy następującą konfigurację bezpieczeństwa:

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .defaultSuccessUrl("/")
                .and()
            .logout()
                .logoutSuccessUrl("/");
    }
```

Taka podstawowa konfiguracja oznacza:
- umożliwienie wejścia na ścieżkę `/register` wszystkim użytkownikom strony (zalogowanym i niezalogowanym),
- oczekiwanie zalogowania przy wejściu na wszystkie inne strony,
- aktywowaniu logowania (pod adresem `/login`) poprzez automatyczny formularz logowania Spring Security z opcją domyślnej strony sukcesu `/`,)
- aktywowanie wylogowania (pod adresem `/logout`) automatycznie przeprowadzanego przez Spring Security.

Teraz przy przetestowaniu strony `/register` nasza aplikacja nie będzie od nas już oczekiwać zalogowania się.

[Rozwiązanie zadania](resolutions/2.5.md)

---

### Zadanie 2.6. Szyfrowanie hasła

Dzisiejsze wymogi bezpieczeństwa nie dopuszczają sytuacji kiedy hasła użytkowników zapisane są w bazie w postaci czystego tekstu, bez szyfrowania. Dzięki Spring Security szyfrowanie hasła możemy bardzo łatwo osiągnąć. 

Spring Security dostarcza implementację wielu algorytmów szyfrowania, jako domyślny przyjmując `bcrypt`. Algorytm `bcrypt` reprezentowany jest przez klasę `BCryptPasswordEncoder`. Nie musimy z tej klasy korzystać wprost ponieważ istnieje również klasa `DelegatingPasswordEncoder`, która automatycznie dostosowuje odpowiednią implementację algorytmu do zapisanego hasła.

Szyfrowania potrzebować będziemy nie tylko w klasie `SecurityConfiguration`, ale również w naszym kontrolerze procesu rejestracji `RegistrationController`. Ponownie więc skorzystamy z mechanizmu _Dependency Injection_, ale tym razem nie stworzymy nowej klasy czy interfejsu z odpowiednią adnotacją (np. `@Component`), a dostarczymy metodę w klasię `SecurityConfiguration`, która sama tworzy nowy obiekt i zwraca go. To, co uczyni naszą metodę wyjątkową, to adnotacja `@Bean`. Adnotacja ta wskazuje, że dana metoda tworzy obiekt bean'a, a więc obiekt zarządzany przez Spring'a. Gdy ten odnajdzie tego typu metodę, to jej wynik bierze dla siebie i potem umożliwia wstrzykiwanie tak dostarczonego obiektu.

Nasza metoda w klasie `SecurityConfiguration` udostępniająca bean'a będzie wyglądała jak niżej:

```java

@Bean
public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```

Teraz, w kontrolerze rejestracji możemy dorzucić pole typu `PasswordEncoder` i zażądać jego dostarczenia przez Springa (spójrz do wcześniejszych zadań jak to osiągnąć). Na dostarczonym obiekcie możemy wywołać metodę `String encdoedPassword = passwordEncoder.encode(password)`, która stworzy zaszyfrowaną wersję hasła. Dopiero takie hasło ustawiamy obiektowi klasy `User` przed jego zapisaniem.

Możemy przetestować tą funkcjonalność rejestrując nowego użytkownika i weryfikując w bazie danych lub w logach aplikacji jak zostało zapisane jego hasło.

[Rozwiązanie zadania](resolutions/2.6.md)

---

### Zadanie 2.7. Logowanie na zarejestrowanych użytkowników

Przedostatnim krokiem jest połączenie procesu rejestracji z procesem logowania. Obecnie korzystamy z testowego użytkownika Spring Security, który niewiele ma wspólnego z użytkownikami, których rejestrujemy.

W celu wykorzystania naszych rejestrowanych użytkowników w procesie logowania musimy dostarczyć własną implementację drugiego elementu bezpieczeństwa, a więc zasad autentykacji (uwierzytelniania) użytkownika. Wybierzemy tutaj jeden z domyślnych mechanizmów Spring Security oparty na zapytaniach SQL i technologii JDBC.

Jest nam potrzebne:
- określenie źródła danych (bazy danych), w którym będziemy szukać naszych użytkowników,
- określenie mechanizmu szyfrowania hasła,
- określenie zapytania, które zwróci nazwę użytkownika, hasło użytkownika i flagę aktywności na podstawie parametru nazwy użytkownika,
- określenie zapytania, które zwróci zestaw par nazwa użytkownika-rola użytkownika na podstawie parametru nazwy użytkownika.

> Nie mamy w aplikacji zarządzania rolami użytkowników, więc ostatnie zapytanie będzie lekko sfabrykowane i nie będzie odczytywać faktycznych danych z bazy.

Wszystkie wspomniane elementy dostarczymy ponownie w klasie `SecurityConfiguration`, ale tym razem nadpisując metodę `void configure(AuthenticationManagerBuilder)`, jak niżej:

```java
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT username, password, active FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, 'ROLE_USER' FROM users WHERE username = ?");
    }
```

Aby powyższa metoda zadziałała potrzebujemy jeszcze w naszej klasie `SecurityConfig` otrzymać obiekt typu `javax.sql.DataSource` . Ze względu na to, że ten obiekt jest już skonfigurowany w `application.properties`, to możemy go po prostu wstrzyknąć (zażądać od Spring'a), np. w oparciu o konstruktor klasy `SecurityConfiguration:

```java
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
```

Przetestujmy teraz formularz logowania, aby zweryfikować czy możliwe jest już logowanie na naszych własnych użytkowników, a nie na użytkownika testowego.

> Użytkownika testowego możemy usunąć z konfiguracji `application.properties` albo pozostawić na potrzeby ewentualnych testów.

---

### Zadanie 2.8. Własny formularz logowania

Zamienimy teraz domyślny formularz logowania dostarczany przez Spring Security na wersję bardziej przyjazną i spójną z formularzem rejestracji. Taki formularz w _naszym_ stylu, a nie w _ich_ stylu! ;)

Będziemy potrzebować:
- kontrolera obsługującego ścieżkę `/login`,
- metody do obsługi żądania typu `GET` w kontrolerze, która zwróci identyfikator widoku z formularzem logowania,
- wskazania w konfiguracji Spring Security, we fragmencie odpowiedzialnym za konfigurację formularza logowania, ścieżki do naszego kontrolera,
- dostarczenia strony html z formularzem logowania.

Pierwszy i drugi punkt pozostawiam do samodzielnej realizacji na podstawie wcześniejszej pracy z kontrolerem rejestracyjnym. Sugeruję tylko, aby kontroler nazwać `LoginControler` oraz umieścić go w tym samym pakiecie, co kontroler `RegistrationController`.

Przejście na własny formularz logowania wymaga od nas zmodyfikowania sekcji konfigurującej logowanie w klasie `SecurityConfiguration` w następujący sposób:

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and() 
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .and()
            .logout()
                .logoutSuccessUrl("/");
    }
```

Zwróćmy tutaj uwagę na wykorzystanie metody `loginPage(String)` i wskazanie w niej ścieżki jaką obsługuje nasz kontroler logowania oraz zabezpieczenia ścieżki `/login` tak, aby mógł na nią wejść tylko zalogowany użytkownik.

Ostatni element to formularz logowania czyli strona html. Możemy stworzyć stronę `login-page.html` w katalogu `templates`. Poniższy kod może być propozycją dla takiej strony logowania:

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Logowanie</title>
</head>
<body>
<h1>Zaloguj się na swoje konto</h1>
<div>
    <form method="post">
        <p>
            <label>Nazwa użytkownika:
                <input type="text" name="username" id="username" value=""/>
            </label>
        </p>
        <p>
            <label>Hasło:
                <input type="password" name="password"/>
            </label>
        </p>
        <p>
            <input type="hidden"
                   th:name="${_csrf.parameterName}"
                   th:value="${_csrf.token}"/>
            <button type="submit">Zaloguj</button>
        </p>
    </form>
</div>
</body>
</html>
```

> Przeanalizuj poszczególne elementy na tej stronie zanim przejdziesz do testów dla całego zadania.

Testujmy i cieszmy się działaniem!

---

### Podsumowanie

Wprowadziliśmy własny proces rejestracji i logowania. W realny sposób wykorzystaliśmy technologie, które miały się pojawić w projekcie.
1. Dzięki Spring Data JPA i Spring Boot niezwykle szybko udało nam się skonfigurować połączenie z bazą danych, dostarczyć encję i jej obsługę poprzez repozytorium `UserRepository`. 
1. Spring MVC pozwolił nam dostarczyć obsługę widoków strony rejestracji oraz strony logowania zgodnie ze wzrocem MVC. Jednocześnie wproadziliśmy w kontrolerach ideę _Dependency Injection_ - kluczową dla wszystkich aplikacji. Połączenie kodu obsługującego żądania z zapisywaniem danych do bazy było niezwykle proste i bezproblemowe.
1. Skonfigurowaliśmy Spring Security bazując na użytkownikach z naszej bazy danych oraz określając potrzebne nam wymogi bezpieczeństwa.

Zestaw zadań w module 3. uatrakcyjni naszą aplikację umożliwiając użytkownikowi nieco więcej działań. Wprowadzimy funkcjonalność dodawania ogłoszeń i ich przeglądania na stronie głównej. Jednak już teraz uzyskałeś pewien szablon aplikacji, który jest uniwersalny dla naszego dalszego projektu jak i dla każdego kolejnego.

**GRATULUJĘ!**

---

[Wstecz](../module_1/README.md) | [Dalej](../module_3/README.md)
