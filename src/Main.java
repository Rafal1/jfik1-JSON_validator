import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {
    public static HashMap<TokenEnum, String> tokens;
    public static LinkedList<Token> gatheredTokens;

    public static void main(String[] args) {
        createTokens();
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new FileReader("jsonExample3.txt"));
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
        tokens = new HashMap<TokenEnum, String>();

        tokens.put(TokenEnum.TRUE, "true");
        tokens.put(TokenEnum.FALSE, "false");
        tokens.put(TokenEnum.NULL, "null");
        tokens.put(TokenEnum.POCZATEK_OBIEKTU, "{");
        tokens.put(TokenEnum.KONIEC_OBIEKTU, "}");
        tokens.put(TokenEnum.POCZATEK_TABLICY, "[");
        tokens.put(TokenEnum.KONIEC_TABLICY, "]");
        tokens.put(TokenEnum.SEPARATOR_WARTOSCI, ":");
        tokens.put(TokenEnum.SEPARATOR_REKORDOW, ",");
        tokens.put(TokenEnum.NUMER, "^-?[0-9]+(\\.[0-9]+)?$");
        tokens.put(TokenEnum.ZNAKI, "^((?!\\\\[\\\\\\\\]*[^\\\"\\\\/bfnrtu]).)*$");
        tokens.put(TokenEnum.ZNAK_NAPISU, "\"");
    }
}
