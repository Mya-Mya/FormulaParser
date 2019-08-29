package formulaparser;

public class NumberToken implements Token {
    protected String numberText;
    public NumberToken(String numberText){
        this.numberText=numberText;
    }

    @Override
    public String toString() {
        return numberText;
    }
}
