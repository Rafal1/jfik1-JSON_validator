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
        Integer tmpStringLocation = null;
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
                        Token t = new Token(TokenEnum.ZNAK_NAPISU, Main.tokens.get(TokenEnum.ZNAK_NAPISU));
                        t.row = lineCounter;
                        t.column = i + 1;
                        tmpStringLocation = i + 2;
                        gatheredTokens.add(t);
                    } else {
                        if (!tokenBuffor.equals("")) {
                            Pattern p = Pattern.compile(Main.tokens.get(TokenEnum.ZNAKI));
                            Matcher m = p.matcher(tokenBuffor);
                            if (m.matches()) {
                                Token t = new Token(TokenEnum.ZNAKI, Main.tokens.get(TokenEnum.ZNAKI));
                                t.row = lineCounter;
                                t.column = tmpStringLocation;
                                tmpStringLocation = null;
                                gatheredTokens.add(t);
                                tokenBuffor = "";
                            } else {
                                System.out.println("NAPIS Error token is inappropriate character: " + c + " in line " + lineCounter + " and column " + (i + 1) + ".");
                                tokenBuffor = "";
                            }
                        }
                        Token t = new Token(TokenEnum.ZNAK_NAPISU, Main.tokens.get(TokenEnum.ZNAK_NAPISU));
                        t.row = lineCounter;
                        t.column = i + 1;
                        gatheredTokens.add(t);
                        chainFlag = false;
                    }
                    continue;
                }
                tokenBuffor += c;

                if (!chainFlag) {
                    if (tokenBuffor.equals("true")) {
                        Token t = new Token(TokenEnum.TRUE, Main.tokens.get(TokenEnum.TRUE));
                        t.row = lineCounter;
                        t.column = i - 2;
                        gatheredTokens.add(t);
                        tokenBuffor = "";
                        continue;
                    }

                    if (tokenBuffor.equals("false")) {
                        Token t = new Token(TokenEnum.FALSE, Main.tokens.get(TokenEnum.FALSE));
                        t.row = lineCounter;
                        t.column = i - 3;
                        gatheredTokens.add(t);
                        tokenBuffor = "";
                        continue;
                    }

                    if (tokenBuffor.equals("null")) {
                        Token t = new Token(TokenEnum.NULL, Main.tokens.get(TokenEnum.NULL));
                        t.row = lineCounter;
                        t.column = i - 2;
                        gatheredTokens.add(t);
                        tokenBuffor = "";
                        continue;
                    }

                    Pattern p = Pattern.compile("^[\\-\\.0-9]+$");
                    Matcher m = p.matcher(tokenBuffor);
                    if (m.matches()) {
                        if(i.equals(charsOfLine.length - 1)) {
                            Token t = new Token(TokenEnum.NUMER, Main.tokens.get(TokenEnum.NUMER));
                            t.row = lineCounter;
                            t.column = i - (tokenBuffor.length() - 1);
                            gatheredTokens.add(t);
                            tokenBuffor = "";
                        }
                        continue;
                    } else {
                        if (!(tokenBuffor.length() == 1 | tokenBuffor.length() == 0)) {
                            String tmpTokenBuffor;
                            tmpTokenBuffor = tokenBuffor.substring(0, tokenBuffor.length() - 1);
                            Matcher tmpM = p.matcher(tmpTokenBuffor);
                            if (tmpM.matches()) {
                                Token t = new Token(TokenEnum.NUMER, Main.tokens.get(TokenEnum.NUMER));
                                t.row = lineCounter;
                                t.column = i - (tokenBuffor.length() - 1);
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
                    Token t = new Token(TokenEnum.POCZATEK_OBIEKTU, Main.tokens.get(TokenEnum.POCZATEK_OBIEKTU));
                    t.row = lineCounter;
                    t.column = i + 1;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character('}'))) {
                    Token t = new Token(TokenEnum.KONIEC_OBIEKTU, Main.tokens.get(TokenEnum.KONIEC_OBIEKTU));
                    t.row = lineCounter;
                    t.column = i + 1;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character('['))) {
                    Token t = new Token(TokenEnum.POCZATEK_TABLICY, Main.tokens.get(TokenEnum.POCZATEK_TABLICY));
                    t.row = lineCounter;
                    t.column = i + 1;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character(']'))) {
                    Token t = new Token(TokenEnum.KONIEC_TABLICY, Main.tokens.get(TokenEnum.KONIEC_TABLICY));
                    t.row = lineCounter;
                    t.column = i + 1;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character(':'))) {
                    Token t = new Token(TokenEnum.SEPARATOR_WARTOSCI, Main.tokens.get(TokenEnum.SEPARATOR_WARTOSCI));
                    t.row = lineCounter;
                    t.column = i + 1;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character(','))) {
                    Token t = new Token(TokenEnum.SEPARATOR_REKORDOW, Main.tokens.get(TokenEnum.SEPARATOR_REKORDOW));
                    t.row = lineCounter;
                    t.column = i + 1;
                    gatheredTokens.add(t);
                    tokenBuffor = "";
                    continue;
                }

                if(!chainFlag) {
                    Pattern pp = Pattern.compile("[nulfasetr]");
                    Matcher tmpM = pp.matcher(c.toString());
                    if (!tmpM.matches()) {
                        System.out.println("Wrong sing " + c + " in line: " + lineCounter + " and column: " + (i + 1));
                        System.exit(0);
                    }
                }

            }

        }
        return gatheredTokens;
    }

//    public static Integer getLengthFieldLimit() {
//        return lengthFieldLimit;
//    }
//
//    public static void setLengthFieldLimit(Integer lengthFieldLimit) {
//        Lexer.lengthFieldLimit = lengthFieldLimit;
//    }

}
