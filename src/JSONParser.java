import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Rafał Zawadzki
 */
public class JSONParser {
    private static LinkedList<Token> gatheredTokens;
    private static Iterator it;
    private static Token currentToken;

    public static void init(LinkedList<Token> tMap) {
        gatheredTokens = tMap;
        it = gatheredTokens.iterator();
        currentToken = (Token) it.next();
        start();
    }

    private static void getNextToken() {
        if (it.hasNext()) {
            currentToken = (Token) it.next();
        } //if wszystko zamkniete? ;/
    }

    private static void err(TokenEnum[] expected) {
        System.out.println("Unexpected token: " + currentToken.name.name() + " in line " + currentToken.row + " and column "
                + currentToken.column + ". We expect: ");
        System.out.println("[");
        for (TokenEnum tn : expected) {
            Token t = Main.tokens.get(tn.name());
            System.out.println(t.name.name() + "(" + t.value + ")");
        }
        System.out.println("]");
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
                break;
            case NUMER:
                getNextToken();
                break;
            case TRUE:
                getNextToken();
                break;
            case FALSE:
                getNextToken();
                break;
            case NULL:
                getNextToken();
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
                err(tel);
        }
    }

    public static void obiekt() {

    }

    public static void trescObiektu() {

    }

    public static void tablica() {

    }

    public static void para() {

    }

    public static void elementy() {

    }

    public static void wartosc() {

    }

    public static void napis() {

    }


}
