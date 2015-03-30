/**
 * @author Rafał Zawadzki
 */
public class Token {
    public TokenEnum name;
    public String value;
    public Integer row;
    public Integer column;

    public Token(TokenEnum n, String v) {
        this.name = n;
        this.value = v;
    }

    public Token(TokenEnum n, String v, Integer r, Integer c) {
        this.name = n;
        this.value = v;
        this.row = r;
        this.column = c;
    }

}
