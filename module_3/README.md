[< Powrót](../README.md)

[Wstecz](../module_2/README.md) | Dalej

## Moduł 3. Dodawanie ogłoszeń i lista ogłoszeń

W ramach zadań zrealizujemy dalszy zestaw funkcjonalności związany z obsługą ogłoszeń użytkowników. Zaczniemy od opcji dodania ogłoszenia oraz listy wszystkich ogłoszeń. Następnie przejdziemy do listy ogłoszeń per użytkownik oraz list ogłoszeń zalogowanego użytkownika. W ramach zadań dodatkowych możemy również wprowadzić edycję ogłoszenia oraz opcję jego usunięcia.

---

### Przegląd zadań do realizacji

Zaczniemy od dostarczenia podstawowej strony głównej. Będzie ona w sobie zawierać funkcjonalność dodawania ogłoszeń (tylko dla zalogowanych użytkowników) oraz funkcjonalność listy ogłoszeń.

Potrzebować będziemy:
- encji reprezentującej ogłoszenie, np. `Advert`,
- repozytorium do obsługi encji ogłoszenia, np. `AdvertRepository`,
- kontrolera obsługującego ścieżkę strony głównej `/`,
- w kontrolerze metody do obsługi żądania typu `GET`, w której udostępnimy do widoku listę wszystkich ogłoszeń,
- w kontrolerze metody do obsługi żądania typu `POST` i ścieżki `/add-advert`, w której obsłużymy proces dodawania nowego ogłoszenia,
- strony thymeleaf zawierającej formularz dodawania nowego ogłoszenia oraz listę wszystkich ogłoszeń (formularz widoczny tylko dla zalogowanych użytkowników, a lista dla wszystkich)

Zadań mamy całkiem sporo, ale większość to elementy z którymi już się spotkaliśmy. Jeżeli czujesz się gotowy/gotowa, to możesz po prostu zrealizować te wymagania tylko na podstawie powyższej listy. Możesz też skorzystać z bardziej dokładnych opisów zadań do realizacji, które znajdziesz w dalszej treści.

Do dzieła!

---

### Zadanie 3.1. Encja reprezentująca ogłoszenie

Dla encji stwórzmy klasę `Advert` w pakiecie obok klasy `User`. Zadbajmy o to, aby posiadała pola:
- `id` dla klucza głównego, generowane automatycznie,
- `title` określające tytuł (nie może być puste),
- `description` określające treść ogłoszenia (nie może być puste i powinno być w bazie danych kolumną typu `TEXT`),
- `posted` określające dokładny czas kiedy ogłoszenie zostało dodane (pole w klasie typu `LocalDateTime`),
- `user` określające użytkownika, który dodał ogłoszenie: zakładamy, że każde ogłoszenie ma tylko jednego właściciela, a jeden użytkownik może mieć wiele ogłoszeń

> Przy polu `user` będziesz musiał/a użyć mapowania relacji między encjami, np. `@ManyToOne`.

Encja powinna być zmapowana na tabelę `adverts`. Pamiętajmy też o stworzeniu metod typu `get/set`, metod `hashCode`, `equals` oraz `toString`.

---

### Zadanie 3.2. Repozytorium do obsługi encji

Stwórzmy teraz interfejs `AdvertRepository`, który będzie rozszerzał `JpaRepository` sparametryzowany typem naszej encji oraz typem jej klucza głównego. Interfejs ten będzie anologiczny do interfejsu `UserRepository`. Umieść go w tym samym pakiecie, co wspomniany `UserRepository`.

---

### Zadanie 3.3. Kontroler obsługujący ścieżkę strony głównej

Kontroler nazwijmy `HomePageController` i dostarczmy jego globalne mapowanie `@RequestMapping` na ścieżkę `/`. Pamiętaj o wymaganych adnotacjach w kontrolerze. W kolejnych zadaniach uzupełnisz metody tego kontrolera, aby nadać mu trochę życia.

---

### Zadanie 3.4. Metoda do obsługi żądania `GET` na ścieżkę główną

Metodę w kontrolerze możemy nazwać `prepareHomePage`, tylko tym razem dorzućmy do niej następujący parametr: `Model model`. 

> Klasa `Model` reprezentuje idee modelu we wzorcu MVC, ale na poziomie relacji `Widok` <-> `Kontroler`. Klasę tą należy rozumieć jako mapę, do której możemy wstawić dowolną wartość w kontrolerze i używać tej wartości w widoku. Obiekt `Model` zastępuje nam tym samym mapę atrybutów żądania. Kluczem w mapie jest obiekt typu `String`, a wartością obiekt dowolnego typu (mapa w postaci `Map<String, Object>`). Każdy klucz, który umieścimy w modelu, będzie dostępny wewnątrz `Expression Language (EL)` tak, jak zmienna w kodzie.

Użycie obiektu `model` w kontrolerze będzie wyglądać mniej więcej tak:

```java
    @GetMapping
    public String prepareHomePage(Model model) {
        // TODO
        return "";
    }
```

Do obiektu `model` należy wstawić nowy atrybut, który będzie listą wszystkich ogłoszeń. Założmy, że ogłoszenia będą posortowane po dacie dodania, a więc polu `posted`. Potrzebujemy do tego:
- wstrzyknąć do kontrolera nasze `AdvertRepository`,
- dostarczyć w `AdvertRepository` metodę zwracającą listę ogłoszeń posortowanych po dacie dodania,
- wstawić zwróconą listę ogłoszeń do modelu, np. pod kluczem `adverts`.

Metodę w repozytorium możemy napisać następującą:

```java
List<Advert> findAllByOrderByPostedDesc();
```

Nie musimy dostarczać implementacji tej metody, wystarczy ją napisać i już będzie działać. Spring Data jest fantastyczne!

---

### Zadanie 3.5. Strona główna z listą wszystkich ogłoszeń

Stwórzmy stronę główną o nazwie `home-page.html` w katalogu `templates`. Na stronie powinniśmy wyświetlić listę wszystkich ogłoszeń w postaci tabeli. Tabela powinna zawierać następujące kolumny:
- liczbę porządkową,
- tytuł ogłoszenia,
- treść ogłoszenia,
- autora ogłoszenia (login użytkowika, który dodał ogłoszenie),
- datę i czas dodania ogłoszenia.

Listę ogłoszeń możemy wyświetlić przy pomocy atrybutu `forEach` z thymeleaf. Naszymi elementami, po którymi będziemy iterować będzie lista ogłoszeń wstawiona do obiektu `model`. 

> Będziemy mieli do niej dostęp na stronie html w wyrażeniach Expression Language (EL) pod nazwą, której użyliśmy jako klucza wstawiając do obiektu `model`. W naszym przypadku będzie to `adverts`. 

> Przykłady jak wykonać iterację w thymeleaf: [tutaj](https://www.baeldung.com/thymeleaf-iteration)

Treść strony, którą możemy wykorzystać i rozbudować o elementy dynamiczne (jest nawet ładna):

```html
<!DOCTYPE html>
<html lang="pl" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css"
          integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS"
          crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"
            integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"
            integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k"
            crossorigin="anonymous"></script>
    <title>Ogłoszenia</title>
</head>
<body>
<div class="container">

    <div class="row" style="margin-top: 40px; margin-bottom: 10px">
        <div class="col-1"></div>
        <div class="col-6"><h2>Dodaj ogłoszenie</h2></div>
        <div class="col-5"></div>
    </div>

    <div class="row form-div">
        <div class="col-2"></div>
        <div class="col-8">

            <!-- Tutaj w przyszłości będzie formularz dodawania ogłoszenia -->

        </div>
        <div class="col-2"></div>
    </div>

    <div class="row" style="margin-top: 40px; margin-bottom: 10px">
        <div class="col-1"></div>
        <div class="col-6"><h2>Lista ogłoszeń</h2></div>
        <div class="col-5"></div>
    </div>

    <div class="row">
        <div class="col-12" style="padding-bottom: 20px">
            <table class="table">
                <tr>
                    <th>Lp.</th>
                    <th>Tytuł</th>
                    <th>Treść</th>
                    <th>Autor</th>
                    <th>Data dodania</th>
                </tr>
                <!-- Poniższy fragment powinien być powtórzony dla każdego ogłoszenia -->
                <tr>
                    <td>1</td>
                    <td>Ciekawy tytuł</td>
                    <td>Interesujący opis</td>
                    <td>marekwojak</td>
                    <td>2021-03-30</td>
                </tr>
                <!-- Koniec fragmentu -->
            </table>
        </div>
    </div>

</div>
</body>
</html>
```

Kiedy skończymy implementację, to dodajmy ręcznie do bazy kilka wpisów ogłoszeń i przetestujmy naszą stronę główną wchodząc na ścieżkę [localhost:8080/](http://localhost:8080/). Ogłoszenia powinny mieć wszystkie dane i wyświetlać się od najnowszego do najstarszego.

> Uwaga: Dane możemy dodać ręcznie albo możemy napisać komponent, w którym utworzymy obiekty na start aplikacji i dodamy je do bazy. W przypadku dodawania ręcznego konieczne będzie odblokowanie dostępu do konsoli H2 w konfiguracji bezpieczeństwa. Natomiast aby stworzyć komponent, który posiada metodę uruchomiającą się przy starcie aplikacji warto zajrzeć [tutaj](https://www.baeldung.com/running-setup-logic-on-startup-in-spring) i wybrać jedną z możliwości (np. mechanizm `@EventListener` i zdarzenie `ContextRefreshedEvent`).

---

### Zadanie 3.6. Konfiguracja bezpieczeństwa dla strony głównej

Nasza strona główna dostępna jest tylko dla zalogowanych użytkowników, bo konfiguracja bezpieczeństwa zakładała, że każda ścieżka, która nie została skonfigurowana wprost, będzie dostępna tylko po wcześniejszym uwierzytelnieniu.

Dodajmy teraz konfigurację autoryzacji dla strony głównej (ścieżki `/`) i użyjmy metody `permitAll()`. Metoda `permitAll` konfiguruje obsługę zasobu w taki sposób, aby dostępny był dla wszystkich: zarówno zalogowanych jak i niezalogowanych użytkowników. Jest jednocześnie najszerszą opcją dostępu.

---

### Zadanie 3.7. Formularz nowego ogłoszenia

Formularz dodawania ogłoszenia umieścimy na stronie głównej. Powinien to być standardowy formularz, który zawierać będzie pola:
- `title`, typu `input`,
- `description`, typu `textarea`.

Oba pola powinny być wymagane, a formularz powinien wysyłać zapytanie typu `POST` na ścieżkę `/add-advert`. Formularz umieścimy we wcześniej przygotowanej sekcji na stronie głównej.

Od strony bezpieczeństwa naszej aplikacji musimy zadbać, aby formularz był dostępy tylko dla zalogowanych użytkowników. Jeżeli chcemy jakąś część strony generować tylko dla zalogowanych użytkowników, to możemy skorzystać z atrybutu `sec:authorize`:

```html
    <div class="row form-div" sec:authorize="isAuthenticated()">
        <div class="col-2"></div>
        <div class="col-8">

            <!-- Tutaj w przyszłości będzie formularz dodawania ogłoszenia -->

        </div>
        <div class="col-2"></div>
    </div>
```

Dzięki temu, kiedy nie jesteśmy zalogowani, to możemy wejść na stronę główną, ale już nie możemy dodać nowego ogłoszenia.

Przetestujmy działanie tych zabezpieczeń wchodząc na stronę główną zalogowanym i niezalogowanym użytkownikiem. W pierwszym przypadku powinniśmy widzieć sekcję z dodawaniem ogłoszenia oraz listę ogłoszeń, a w drugim przypadku tylko listę ogłoszeń.

---

### Zadanie 3.8. Obsługa dodawania ogłoszenia 

Obsługę dodawania ogłoszenia zrealizujemy w kontrolerze `HomePageController`. Potrzebujemy mapowania `@PostMapping` do ścieżki `/add-advert`, a samą metodę (akcję) możemy nazwać `addAdvert`.

W kontrolerze będziemy potrzebować:
- wstrzykniętego obiektu `UserRepository`,
- wstrzykniętego obiektu `AdvertRepository`.

W metodzie `addAdvert` powinniśy przyjąć parametry odpowiadające polom formularza, a więc `title` typu `String` oraz `description` typu `String`. 

Pozostaje pytanie skąd wziąć informację o zalogowanym użytkowniku, który dodał ogłoszenie? Podobnie, jak Spring MVC z przyjemnością dostarcza naszym metodom kontrolera obiekt klasy `Model`, tak również możemy go poprosić o obiekt klasy `Principal`. Jest to obiekt, który reprezentuje bardzo ogólną informację o zalogowanym użytkowniku, ale co ważne: posiada metodę `getName()`. 

> Metoda `getName` zwraca nazwę zalogowanego użytkownika zgodną z nazwą, którą się logowaliśmy. Dla nas jest to jednocześnie wartość pola `username` w klasie `User`. 

Sygnatura naszej metody będzie wyglądała więc jak niżej:

```java
@PostMapping("/add-advert")
public String addAdvert(String title, String description, Principal principal) {
    String username = principal.getName();
    // TODO
    return "";
}
```

> Listę wszystkich parametrów, których możemy oczekiwać od Spring MVC, znajdziemy [tutaj](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-arguments)

Obiekt `Principal` będzie różny od `null` tylko, gdy istnieje zalogowany użytkownik. Zauważ, że nie musimy obsługiwać przypadku gdy obiekt ten będzie `null`, bo nasza konfiguracja Spring Security do tego nie dopuści. Skorzystanie w niej z opcji `anyRequest().authenticated()`, sprawia, że ścieżka `/add-advert` jest dostępna tylko po zalogowaniu. Dzięki temu zawsze uzyskamy _nienullowy_ obiekt `Principal`, bo nie zalogowany użytkownik nie może przesłać żądania na ścieżkę `/add-advert`.

Brakującym elementem całej układanki jest uzyskanie obiektu użytkownika, który wykorzystamy do stworzenia obiektu encji `Advert`. Dopiszmy do repozytorium `UserRepository` metodę:

```java
User findByUsername(String username);
```

Spring Data sprawia, że poza napisaniem deklaracji tej metody w repozytorium nie musimy robić nic więcej. Pozostaje użyć jej tam, gdzie jest nam potrzebna. 

Możemy teraz w kontrolerze spokojnie utworzyć obiekt encji `Advert` i uzupełnić jej pola.

Na końcu, z metody `addAdvert` zwróćmy wyrażenie: `return "redirect:/";`. Taki wynik metody zapewni, że po wykonaniu logiki procesu dodawania ogłoszenia nie zostanie zwrócony kod HTML a informacja o przekierowaniu. Przeglądarka sama wykona żądanie na wskazany adres.

Możemy teraz uruchomić naszą aplikację, zalogować się i sprawdzić czy ogłoszenia dodają się prawidłowo.

---

### Zadania dodatkowe

W ramach zadań dodatkowych możemy zrealizować:
- listę ogłoszeń zalogowanego użytkownika,
- listę ogłoszeń wybranego użytkownika,
- ograniczenie wyświetlania ogłoszeń dla niezalogowanego użytkownika,
- usuwanie swojego ogłoszenia,
- edytowanie swojego ogłoszenia,
- listę obserwowanych ogłoszeń.

W poniższych punktach zostały ogólnie opisane założenia dla każdego z tych zadań.

#### Lista ogłoszeń zalogowanego użytkownika

Lista ogłoszeń zalogowanego użytkownika będzie wymagała:
- nowego kontrolera `UserAdvertsController`,
- obsługi żądania `GET` z mapowaniem `/user-adverts`, w którym udostępnimy ogłoszenia tylko zalogowanego użytkownika (korzystając z obieków `Model` oraz `Principal`),
- metody w `AdvertRepository`, która zwróci listę ogłoszeń tylko dla wskazanego użytkownika, np: `List<Advert> findAllByUserOrderByPostedDesc(User user);`,
- widoku `user-adverts-page.html` z tabelą analogiczną jak na stronie głównej, ale rozszerzoną o kolumnę `Akcje` (na przyszłe opcje usunięcia i edycji)

---

#### Lista ogłoszeń wybranego użytkownika

Lista ogłoszeń wybranego użytkownika będzie wymagała:
- zmodyfikowania metody `GET` w kontrolerze `UserAdvertsController` tak, aby przyjmowała opcjonalny parametr żądania `userId` typu `Long`

  > Jeżeli ten parametr się pojawi to nasz użytkownik nie będzie pobierany na podstawie nazwy z obiektu `Principal`, a na podstawie pola `userId`, które jest zgodne z kluczem głównym użytkownika.
  
- zmiany w tabeli na stronie głównej kolumny z informacją o właścicielu ogłoszenia tak, aby teraz był to link do ścieżki `/user-adverts?userId=$1`, gdzie symbol `$1` należy zamienić wartością pola `id` z obiektu `User`.
  
---

#### Ograniczenie wyświetlania ogłoszeń dla niezalogowanego użytkownika

Chcemy, aby wchodząc na stronę główną niezalogowany użytkownik widział tylko 10 najnowszych ogłoszeń oraz nie mógł przejść na stronę z ogłoszeniami konkretnego użytkownika (bo i tak Spring Security przecież go tam nie wpuści). W tym celu należy:
- zmienić metodę `GET` w klasie `HomePageController`, aby przyjmowała dodatkowo parametr `Principal principal` i w sytuacji gdy obiekt `principal` nie będzie dostępny (będzie `null'em`), to do modelu pod atrybut `adverts` wstawiała listę tylko 10 najnowszych ogłoszeń, a nie wszystkich.

  > Możesz albo to zapisać w Javie, a więc na poziomie kodu obciąć listę do pierwszych dziesięciu pozycji albo napisać dedykowaną metodę w `UserRepository` postaci: `List<Advert> findFirst10ByOrderByPostedDesc();`
  
- wprowadzony we wcześniejszym zadaniu link do ogłoszeń użytkownika umieścić wewnątrz sekcji chronionej i dodać sekcję dla niezalogowanych użytkowników, w której zamiast linku będzie wyświetlany po prostu tekstowo login.

---

#### Usuwanie ogłoszenia

Usuwanie ogłoszenie będzie wymagało:
- dodania nowej metody do kontrolera `UserAdvertsController` typu `POST` i na ścieżkę `/delete-advert`,
- w metodzie przyjęcia parametru żądania `advertId`, który będzie wskazywał na `id` ogłoszenia do usunięcia,
- zweryfikowania czy wskazane ogłoszenie istnieje oraz czy zalogowany użytkownik jest jego właścicielem,
- usunięcia ogłoszenia z bazy (metoda `delete` w `AdvertRepository`) i zakończenie metody przekierowaniem do listy ogłoszeń użytkownika (`return "redirect:/user-adverts"`),
- dodania na liście ogłoszeń zalogowanego użytkownika małego formularza do ścieżki `/delete-advert?advertId=$1`, gdzie pod `$1` będzie trzeba podstawić `id` konkretnego ogłoszenia.
  
  > `id` ogłoszenia można przesłać z formularza w postaci pola typu `hidden`, pamiętaj tylko, aby pole to miało atrybut `name` o wartości `advertId`, czyli takiej, jakiej oczekiwać będziemy w metodzie kontrolera.
  
---
  
#### Edytowanie ogłoszenia

Edytowanie ogłoszenia będzie wymagało:
- dodania nowej metody do kontrolera `UserAdvertsController` typu `POST` i na ścieżkę `/edit-advert`,
- w metodzie przyjęcia parametrów żądania `advertId`, `title` oraz `description`,
- na podstawie `advertId` pobranie ogłoszenia i upewnienie się czy zalogowany użytkownik jest jego właścicielem,
- zmianę tytułu i opisu ogłoszenia zgodnie z podanymi parametrami,
- zakończenie metody przekierowaniem do listy ogłoszeń użytkownika,
- dodaniem nowej strony z formularzem edycji ogłoszenia i jej obsługi w metodzie `GET` na ścieżkę `/edit-advert`,
- w metodzie `GET` należy na podstawie parametru `advertId` upewnić się czy użytkownik jest właścicielem edytowanego ogłoszenia i jeżeli tak, to pobrać to ogłoszenie i wstawić do modelu pod kluczem `editedAdvert`,
- w formularzu należy wykorzystać atrybut `value` w elementach typu `input` i `textarea`, aby formularz edycji był wypełniony na start oryginalnym tytułem i opisem,
- w formularzu należy dodać pole typu `hidden` o atrybucie `name` równym `advertId` oraz wartości równej polu `id` z obiektu `editedAdvert`.

---

#### Lista obserwowanych ogłoszeń

Lista obserwowanych ogłoszeń będzie od nas wymagała:
- rozszerzenie encji `User` o pole `Set<Advert> observedAdverts`, które będzie w relacji wiele do wielu między klasą `User` a klasą `Advert` (do pola również metody `get/set`),
- dodanie na stronie głównej i na stronie z ogłoszeniami konkretnego użytkownika linku/ikony/przycisku do obserwowania i linku/ikonu/przycisku do wstrzymania obserwowania,

  > Jeżeli ogłoszenie jest na liście obserwowanych zalogowanego użytkownika, to mamy dostępne działanie _zaprzestania obserwowania_, a gdy go nie ma to _rozpoczęcia obserwowania_
  
- dodanie odpowiedniej obsługi w kontrolerze `HomePageController` i `UserAdvertsController` akcji _obserwowania_, która będzie wymagała `id` ogłoszenia i na podstawie zalogowanego użytkownika doda do jego listy obserwowanych wskazane ogłoszenie,
- dodania odpowiedniej obsługi w kontrolerze `HomePageController` i `UserAdvertsController` akcji _zaprzestania obserwowania_, która będzie wymagała `id` ogłoszenia i na podstawie zalogowanego użytkownika usuniego z jego listy obserwowanych wskazane ogłoszenie,
- dodanie widoku z listą obserwowanych ogłoszeń oraz obsługi tego widoku w kontrolerze `UserAdvertsController` i linku do widoku w menu głównym (tylko dla zalogowanych).

Dla ułatwienia można w zadaniu przyjąć, że użytkownik może dodawać do obserwowanych również swoje ogłoszenie, a dla utrudnienia przyjąć, że na swoich ogłoszeniach taka funkcjonalność w ogóle nie jest potrzebna.

---

### Podsumowanie

Realizacja wszystkich kroków, nawet bez zadań dodatkowych, jest równoznaczna z wykonaniem solidnej pracy. Należą Ci się ogromne gratulację oraz słowa uznania jak niżej:

[Good job!](https://www.youtube.com/watch?v=S0UvJZmGTsk)

Natomiast wykonanie wszystkich kroków razem z zadaniami dodatkowymi, to szacunek od teraz, aż do kolejnej wersji Spring'a (czyli Spring 6)!

---

[Wstecz](../module_2/README.md) | Dalej