/**
 * @author Rafał Zawadzki
 */
public enum TokenEnum {
    ZNAK_NAPISU ("\""),
    FALSE  ("false"),
    TRUE ("true"),
    NULL ("null"),
    POCZATEK_OBIEKTU ("{"),
    KONIEC_OBIEKTU ("}"),
    POCZATEK_TABLICY ("["),
    KONIEC_TABLICY ("]"),
    SEPARATOR_WARTOSCI (":"),
    SEPARATOR_REKORDOW (","),
    NUMER ("^-?[0-9]+(\\.[0-9]+)?$"),
    ZNAKI ("^.*$");

    String token;
    TokenEnum(String token){
        this.token = token;
    }
}
