package formulaparser;

public enum FormulaLanguage {
    Invpol, Java;

    @Override
    public String toString() {
        switch (this){
            case Invpol:return new String("逆ポーランド記法");
            case Java:return new String("Java");
        }
        return null;
    }
}