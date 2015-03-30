import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rafał Zawadzki
 */
public class Lexer {
    private static Integer lengthFieldLimit = 500000;

    public LinkedList<Token> start(BufferedReader bReader) throws IOException {
        LinkedList<Token> gatheredTokens = new LinkedList<Token>();
        String lineConstant;
        String tokenBuffor = "";
        Boolean chainFlag = false;
        Integer lineCounter = 0;
        while ((lineConstant = bReader.readLine()) != null) {
            lineCounter++;
            char[] charsOfLine = lineConstant.toCharArray();
            for (Integer i = 0; i < charsOfLine.length; i++) {
                Character c = charsOfLine[i];
                if (c.equals(new Character(' '))) {
                    continue;
                }

                if (c.equals('\"')) {
                    if (!chainFlag) {
                        chainFlag = true;
                        Token t = Main.tokens.get(TokenEnum.ZNAK_NAPISU);
                        t.row = lineCounter;
                        t.column = i;
                        gatheredTokens.add(t);
                    } else {
                        if (!tokenBuffor.equals("")) {
                            Pattern p = Pattern.compile(".*");
                            Matcher m = p.matcher(tokenBuffor);
                            if (m.matches()) {
                                Token t = Main.tokens.get(TokenEnum.ZNAKI);
                                t.row = lineCounter;
                                t.column = i;
                                gatheredTokens.add(t);
                                tokenBuffor = "";
                            } else {
                                System.out.println("NAPIS Error token is inappropriate character: " + c + " in line " + lineCounter + " and column " + i + ".");
                                tokenBuffor = "";
                            }
                        }
                        Token t = Main.tokens.get(TokenEnum.ZNAK_NAPISU);
                        t.row = lineCounter;
                        t.column = i;
                        gatheredTokens.add(t);
                        chainFlag = false;
                    }
                    continue;
                }
                tokenBuffor += c;

                if (!chainFlag) {
                    if (tokenBuffor.equals("true")) {
                        Token t = Main.tokens.get(TokenEnum.TRUE);
                        t.row = lineCounter;
                        t.column = i;
                        gatheredTokens.add(t);
                        tokenBuffor = "";
                        continue;
                    }

                    if (tokenBuffor.equals("false")) {
                        Token t = Main.tokens.get(TokenEnum.FALSE);
                        t.row = lineCounter;
                        t.column = i;
                        gatheredTokens.add(t);
                        tokenBuffor = "";
                        continue;
                    }

                    if (tokenBuffor.equals("null")) {
                        Token t = Main.tokens.get(TokenEnum.NULL);
                        t.row = lineCounter;
                        t.column = i;
                        gatheredTokens.add(t);
                        tokenBuffor = "";
                        continue;
                    }

                    Pattern p = Pattern.compile("^[\\-\\.0-9]+$");
                    Matcher m = p.matcher(tokenBuffor);
                    if (m.matches()) {
                        continue;
                    } else {
                        if (!(tokenBuffor.length() == 1 | tokenBuffor.length() == 0)) {
                            String tmpTokenBuffor;
                            tmpTokenBuffor = tokenBuffor.substring(0, tokenBuffor.length() - 1);
                            Matcher tmpM = p.matcher(tmpTokenBuffor);
                            if (tmpM.matches()) {
                                Token t = Main.tokens.get(TokenEnum.NUMER);
                                t.row = lineCounter;
                                t.column = i;
                                gatheredTokens.add(t);
                                tokenBuffor = "";
                            }
                        }
                    }
                }

                if (tokenBuffor.length() > lengthFieldLimit) {
                    System.out.println("Too long string or number. Length limit is " + lengthFieldLimit + " signs.");
                }

                if (c.equals(new Character('{'))) {
                    Token t = Main.tokens.get(TokenEnum.POCZATEK_OBIEKTU);
                    t.row = lineCounter;
                    t.column = i;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character('}'))) {
                    Token t = Main.tokens.get(TokenEnum.KONIEC_OBIEKTU);
                    t.row = lineCounter;
                    t.column = i;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character('['))) {
                    Token t = Main.tokens.get(TokenEnum.POCZATEK_TABLICY);
                    t.row = lineCounter;
                    t.column = i;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character(']'))) {
                    Token t = Main.tokens.get(TokenEnum.KONIEC_TABLICY);
                    t.row = lineCounter;
                    t.column = i;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character(':'))) {
                    Token t = Main.tokens.get(TokenEnum.SEPARATOR_WARTOSCI);
                    t.row = lineCounter;
                    t.column = i;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character(','))) {
                    Token t = Main.tokens.get(TokenEnum.SEPARATOR_REKORDOW);
                    t.row = lineCounter;
                    t.column = i;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }
            }

        }
        return gatheredTokens;
    }

    public static Integer getLengthFieldLimit() {
        return lengthFieldLimit;
    }

    public static void setLengthFieldLimit(Integer lengthFieldLimit) {
        Lexer.lengthFieldLimit = lengthFieldLimit;
    }

}
