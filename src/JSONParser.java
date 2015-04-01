import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Rafał Zawadzki
 */
public class JSONParser {
    private static LinkedList<Token> gatheredTokens;
    private static ListIterator<Token> it;
    private static Token currentToken;

    public static void init(LinkedList<Token> tMap) {
        if (tMap.isEmpty()) {
            return;
        }
        gatheredTokens = tMap;
        it = gatheredTokens.listIterator();
        currentToken = it.next();
        start();
    }

    private static Boolean getNextToken() {
        if (it.hasNext()) {
            currentToken = it.next();
            return true;
        }
        return false;
    }

    private static void err(TokenEnum[] expected, String method) {
        System.out.println("[" + method + "]" + " Unexpected token: " + currentToken.name.name() + " in line " + currentToken.row + " and column "
                + currentToken.column + ". We expect: ");
        if (expected != null) {
            System.out.println("[");
            for (TokenEnum tn : expected) {
                String tValue = Main.tokens.get(tn);
                System.out.println(tn.name() + "(" + tValue + ")");
            }
            System.out.println("]");
        } else {
            System.out.println("End of input file.");
        }
        System.exit(0);
    }

    public static void start() {
        switch (currentToken.name) {
            case POCZATEK_OBIEKTU:
                obiekt();
                break;
            case POCZATEK_TABLICY:
                tablica();
                break;
            case ZNAK_NAPISU:
                napis();
                if (!getNextToken()) {
                    return;
                } else {
                    err(null, "start");
                }
                break;
            case NUMER:
            case TRUE:
            case FALSE:
            case NULL:
                if (!getNextToken()) {
                    return;
                } else {
                    err(null, "start");
                }
                break;
            default:
                TokenEnum[] tel = {
                        TokenEnum.POCZATEK_OBIEKTU,
                        TokenEnum.POCZATEK_TABLICY,
                        TokenEnum.ZNAK_NAPISU,
                        TokenEnum.NUMER,
                        TokenEnum.TRUE,
                        TokenEnum.FALSE,
                        TokenEnum.NULL,
                };
                err(tel, "start");
        }
    }

    public static void obiekt() {
        TokenEnum[] te = {TokenEnum.POCZATEK_OBIEKTU};
        switch (currentToken.name) {
            case POCZATEK_OBIEKTU:
                getNextToken();
                if (currentToken.name.equals(TokenEnum.ZNAK_NAPISU)) {
                    trescObiektu();
                }
                if (currentToken.name.equals(TokenEnum.KONIEC_OBIEKTU)) {
                    return;
                }
                err(te, "obiekt");
                break;
            default:
                err(te, "obiekt");
        }
    }

    public static void trescObiektu() {
        switch (currentToken.name) {
            case ZNAK_NAPISU:
                para();
                getNextToken();
                if (currentToken.name.equals(TokenEnum.SEPARATOR_REKORDOW)) {
                    getNextToken();
                    trescObiektu();
                }
                if (!currentToken.name.equals(TokenEnum.KONIEC_OBIEKTU)) {
                    TokenEnum[] te = {TokenEnum.KONIEC_OBIEKTU};
                    err(te, "trescObiektu");
                }
                break;
            default:
                TokenEnum[] te = {TokenEnum.ZNAK_NAPISU};
                err(te, "trescObiektu");
        }
    }

    public static void tablica() {
        TokenEnum[] te = {TokenEnum.POCZATEK_TABLICY};
        switch (currentToken.name) {
            case POCZATEK_TABLICY:
                getNextToken();
                elementy();
                if (currentToken.name.equals(TokenEnum.KONIEC_TABLICY)) {
                    return;
                } else {
                    err(te, "tablica");
                }
                break;
            default:
                err(te, "tablica");
        }
    }

    public static void para() {
        switch (currentToken.name) {
            case ZNAK_NAPISU:
                napis();
                getNextToken();
                if (currentToken.name.equals(TokenEnum.SEPARATOR_WARTOSCI)) {
                    getNextToken();
                    wartosc();
                } else {
                    TokenEnum[] te = {TokenEnum.SEPARATOR_WARTOSCI};
                    err(te, "para");
                }
                break;
            default:
                TokenEnum[] te = {TokenEnum.ZNAK_NAPISU};
                err(te, "para");
        }
    }

    public static void elementy() {
        Boolean w = wartosc();
        getNextToken();
        if (w && currentToken.name.equals(TokenEnum.KONIEC_TABLICY)) {
            return;
        } else if (w && currentToken.name.equals(TokenEnum.SEPARATOR_REKORDOW)) {
            getNextToken();
            elementy();
        } else {
            TokenEnum[] te = {TokenEnum.SEPARATOR_REKORDOW, TokenEnum.KONIEC_TABLICY};
            err(te, "elementy");
        }

    }

    public static Boolean wartosc() {
        TokenEnum[] te = {TokenEnum.NULL,
                TokenEnum.TRUE,
                TokenEnum.POCZATEK_TABLICY,
                TokenEnum.POCZATEK_OBIEKTU,
                TokenEnum.FALSE,
                TokenEnum.NUMER,
                TokenEnum.ZNAK_NAPISU};

        switch (currentToken.name) {
            case NULL:
            case TRUE:
            case FALSE:
            case NUMER:
                return true;
            case ZNAK_NAPISU:
                napis();
                return true;
            case POCZATEK_OBIEKTU:
                obiekt();
                return true;
            case POCZATEK_TABLICY:
                tablica();
                return true; //todo ensure its true
            default:
                err(te, "wartosc");
                return false;
        }
    }

    public static void napis() {
        TokenEnum[] te = {TokenEnum.ZNAK_NAPISU};
        switch (currentToken.name) {
            case ZNAK_NAPISU:
                getNextToken();
                if (currentToken.name.equals(TokenEnum.ZNAK_NAPISU)) {
                    return;
                } else if (currentToken.name.equals(TokenEnum.ZNAKI)) {
                    getNextToken();
                    if (currentToken.name.equals(TokenEnum.ZNAK_NAPISU)) {
                        return;
                    } else {
                        te[0] = TokenEnum.ZNAK_NAPISU;
                        err(te, "napis");
                    }
                } else {
                    err(te, "napis");
                }
            default:
                err(te, "napis");
        }
    }
}