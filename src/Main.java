import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {
    //    public static ArrayList<Token> tokens;
    public static HashMap<TokenEnum, Token> tokens;
    public static LinkedList<Token> gatheredTokens;

    public static void main(String[] args) {
        createTokens();
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new FileReader("jsonExample.txt"));
            Lexer lx = new Lexer();
            gatheredTokens = lx.start(bReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONParser.init(gatheredTokens);
        System.out.close();
    }

    public static void createTokens() {
//        tokens = new ArrayList<Token>();
        tokens = new HashMap<TokenEnum, Token>();

        Token t = new Token(TokenEnum.TRUE, "true");
        tokens.put(TokenEnum.TRUE, t);
        t = new Token(TokenEnum.FALSE, "false");
        tokens.put(TokenEnum.FALSE, t);
        t = new Token(TokenEnum.NULL, "null");
        tokens.put(TokenEnum.NULL, t);
        t = new Token(TokenEnum.POCZATEK_OBIEKTU, "{");
        tokens.put(TokenEnum.POCZATEK_OBIEKTU, t);
        t = new Token(TokenEnum.KONIEC_OBIEKTU, "}");
        tokens.put(TokenEnum.KONIEC_OBIEKTU, t);
        t = new Token(TokenEnum.POCZATEK_TABLICY, "[");
        tokens.put(TokenEnum.POCZATEK_TABLICY, t);
        t = new Token(TokenEnum.KONIEC_TABLICY, "]");
        tokens.put(TokenEnum.KONIEC_TABLICY, t);
        t = new Token(TokenEnum.SEPARATOR_WARTOSCI, ":");
        tokens.put(TokenEnum.SEPARATOR_WARTOSCI, t);
        t = new Token(TokenEnum.SEPARATOR_REKORDOW, ",");
        tokens.put(TokenEnum.SEPARATOR_REKORDOW, t);
        t = new Token(TokenEnum.NUMER, "^-?[0-9]+(\\.[0-9]+)?$");
        tokens.put(TokenEnum.NUMER, t);
        t = new Token(TokenEnum.ZNAKI, "^((?!\\\\[\\\\\\\\]*[^\\\"\\\\/bfnrtu]).)*$");
        tokens.put(TokenEnum.ZNAKI, t);
        t = new Token(TokenEnum.ZNAK_NAPISU, "\"");
        tokens.put(TokenEnum.ZNAK_NAPISU, t);
    }
}
