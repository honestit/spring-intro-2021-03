package com.projects.ams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdvertsManagementSystemApplication {

    /*

    Schemat realizowania nowej funkcjonalności w aplikacji

    1. Decyzja czy potrzebujemy nowego widoku (strony dla użytkownika) czy rozwijamy
    już istniejący.
    2a. Istniejący - rozwijamy kod istniejącej klasy kontrolera dla tego widoku
    2b. Nowa - tworzymy nową klasę kontrolera

    Nowa klasa kontrolera i nowa funkcjonalności i nowy widok

    1. Tworzymy nową klasę z adnotacją @Controller + adnotacja @RequestMapping ze wskazaną ścieżką (adresem strony), którą ten kontroler będzie obsługiwał
    2. Zaimplementowanie nowej "akcji" (metody z adnotacją @GetMapping albo @PostMapping albo @PutMapping albo itd.) do obsługi żądania.

    Cechy metody obsługującej żądanie:
    - metoda jest publiczna,
    - metoda posiada adnotacją mapującą (najczęściej @GetMapping lub @PostMapping),
    - zwraca String, który ma oznaczać identyfikator widoku (view name), w przypadku podstawowej konfiguracji thymeleaf będzie to ścieżka do pliku "licząc" od katalogu "/resources/templates/" z pominięciem rozszerzenia (domyślnie ".html"),
    - jeżeli wartości, które stworzymy/pobierzemy w kontrolerze chcemy użyć na stronie widoku (stronie HTML), to korzystamy z klasy Model i wypełniamy ją danymi (patrz: HomePageController#prepareHomePage)
    - jeżeli nasza akcja ma przetwarzać dane od użytkownika (parametry żądania), to dla każdego parametru żądania dodajemy nowy parametr do naszej metody z adnotacją @RequestParam (patrz: RegistrationController#processRegistrationData),
    - nazwy parametrów przesyłane przez formularz wynikają z atrybuty "name" w znacznikach reprezentujących kontrolki (np. "input"),
    - nazwy parametrów przesyłane "gołębiem" (czyli adres wpisany w przeglądarce lub link) są fragmentem żądania, np. "/adverts?size=20&sort=title,

    Wykorzystanie innych elementów aplikacji w metodzie do obsługi żądania (akcji):
    - jeżeli nasza metoda potrzebuje wykonywać działanie na bazie danych, to musimy skorzystać z repozytoriów dla każdej encji, którą przetwarzamy (np. chcemy zapisać encję User, to potrzebujemy klasy/interfejsu UserRepository),
    - aby uzyskać dostęp do repozytorium, to tworzy w klasie kontrolera pole (np. private UserRepository userRepository) i dajemy możliwość ustawienia tego pola Springowi (np. tworząc konstruktor dla tego pola [i innych]),
    - w metodach naszego kontrolera potem możemy korzystać z metod wywołanych na tym polu (zmiennej) i mamy gwarancję, że tam nie ma nulla.
     */

    public static void main(String[] args) {
        SpringApplication.run(AdvertsManagementSystemApplication.class, args);
    }

}
