![Build status](https://travis-ci.org/vonshick/Network-Analyzer.svg?branch=master)

# Network Analyzer

Dla administratorów różnego rodzaju sieci (energetycznych, wodociągowych itp.) nasza aplikacja Network Analyzer umożliwi znajdowanie ekonomicznych połączeń pomiędzy węzłami sieci oraz dostarczy informacji o samej sieci.

## Opis struktury sieci :
1. Node = każdy węzeł opisany jest poprzez:
   - id = unikalny identyfikator
   - name = opcjonalny tekst opisujący węzeł
   - type = entry, exit, regular
   - outgoing = lista połączeń wychodzących z tego węzła
   - incoming = lista połączeń wchodzących z innych węzłów
2. Connection = powiązanie węzłów
   - Połączenie dwóch węzłów:
     - from = z jakiego węzła
     - to = do jakiego węzła
     - value = wartość, która w zależności od dziedziny biznesowej może oznaczać np. dystans, przepustowość, itd.
     
## UML: 
![Alt text](UML.png?raw=true "Title")

## Uwagi do pracy w zespole z użyciem metodyki Scrum
### Sprint 1
1. Powinniśmy informować się o problemach na bieżąco.
2. W razie przeszacowania części sprintu należy podzielić ją pomiędzy kilku członków zespołu.
### Sprint 2
1. Praca nad oszacowywaniem zadań - dodawanie zapasu czasu na niespodziewane problemy.

## Pomiar jakości kodu 
Dokonaliśmy pomiaru jakości kodu zgodnie z metodą Goal Question Metric:
- Question: Czy klasy nie są zbyt rozbudowane? 
- Goal: Minimalizacja liczby linii kodu w poszczególnych klasach. 
- Metric: NCSS (non-commenting source statements).
   
Obliczyliśmy liczbę linii kodu dla każdej z klas (również tych testowych) i zauważyliśmy, że we wszystkich przypadkach wynosi ona znacznie mniej niż 300, która to wartość uznawana jest za górną granicę dobrego wyniku. 
