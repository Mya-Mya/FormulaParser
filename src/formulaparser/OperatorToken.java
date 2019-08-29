package formulaparser;

public class OperatorToken implements Token {
    public enum Kind {
        Add, Sub, Mul, Div, Pow, Sqr, Abs, Exp, Sin, Cos, Tan, Bra, Clb;
        public int getLevel(){
            switch (this){
                case Bra:case Clb:return 1;
                case Add: case Sub: return 2;
                case Mul: case Div: return 3;
                case Pow: case Sqr: case Abs: case Exp: case Sin: case Cos: case Tan: return 4;
            }
            return 0;
        }
        public boolean isFunction(){
            switch (this){
                case Sqr: case Abs: case Exp: case Sin: case Cos: case Tan:return true;
            }
            return false;
        }

        @Override
        public String toString() {
            switch (this){
                case Add: return ("+");
                case Sub: return ("-");
                case Mul: return ("*");
                case Div: return ("/");
                case Pow: return ("POW");
                case Sqr: return ("SQRT");
                case Abs: return ("ABS");
                case Exp: return ("EXP");
                case Sin: return ("SIN");
                case Cos: return ("COS");
                case Tan: return ("TAN");
                case Bra:return ("(");
                case Clb: return (")");
            }
            return super.toString();
        }
    }
    protected Kind kind;
    public OperatorToken(Kind kind){
        this.kind=kind;
    }

    public int getLevel(){
        return kind.getLevel();
    }
    public Kind getKind(){
        return kind;
    }
    public boolean isFunction(){
        return kind.isFunction();
    }

    @Override
    public String toString() {
        return kind.toString();
    }
}
