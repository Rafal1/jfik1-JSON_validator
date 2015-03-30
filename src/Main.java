import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
    public static ArrayList<Token> tokens;
    public static LinkedList<String> gatheredTokens;

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
        System.out.close();
    }

    public static void createTokens() {
        tokens = new ArrayList<Token>();

        Token t = new Token("TRUE", "true");
        tokens.add(t);
        t = new Token("FALSE", "false");
        tokens.add(t);
        t = new Token("NULL", "null");
        tokens.add(t);
        t = new Token("POCZATEK_OBIEKTU", "{");
        tokens.add(t);
        t = new Token("KONIEC_OBIEKTU", "}");
        tokens.add(t);
        t = new Token("POCZATEK_TABLICY", "[");
        tokens.add(t);
        t = new Token("KONIEC_TABLICY", "]");
        tokens.add(t);
        t = new Token("SEPARATOR_WARTOSCI", ":");
        tokens.add(t);
        t = new Token("SEPARATOR_REKORDOW", ",");
        tokens.add(t);
        t = new Token("NUMER", "^-?[0-9]+(\\.[0-9]+)?$");
        tokens.add(t);
        t = new Token("ZNAKI", "^((?!\\\\[\\\\\\\\]*[^\\\"\\\\/bfnrtu]).)*$");
        tokens.add(t);
        t = new Token("ZNAK_NAPISU", "\"");
        tokens.add(t);
    }
}
