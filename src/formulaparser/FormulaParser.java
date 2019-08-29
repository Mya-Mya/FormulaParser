package formulaparser;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class FormulaParser {
    /**
     * @param invpolTokens 逆ポーランド記法のトークン列
     * @return 逆ポーランド記法の文字列
     */
    public static final String fromTokensToText(Queue<Token> invpolTokens) {
        StringBuilder sb = new StringBuilder();
        while (!invpolTokens.isEmpty()) {
            Token nextToken = invpolTokens.poll();
            sb.append(nextToken.toString());
            sb.append(' ');
        }
        return sb.toString();
    }

    /**
     * @param mathTokens 数学語のトークン列
     * @return 逆ポーランド記法のトークン列
     */
    public static final Queue<Token> fromMathTokensToInvpolTokens(Queue<Token> mathTokens) {
        Queue<Token> invpolTokens = new ArrayDeque<>();
        Stack<Token> operators = new Stack<>();
        while (!mathTokens.isEmpty()) {
            Token nextToken = mathTokens.poll();
            if (nextToken instanceof NumberToken) {
                invpolTokens.offer(nextToken);
            } else {
                if (nextToken instanceof OperatorToken) {
                    OperatorToken nextOp = (OperatorToken) nextToken;
                    //カッコ閉じの場合
                    if (nextOp.getKind() == OperatorToken.Kind.Clb) {
                        while (!operators.isEmpty() && ((OperatorToken) operators.peek()).getKind() != OperatorToken.Kind.Bra) {
                            invpolTokens.offer(operators.pop());
                        }
                        if (!operators.isEmpty()) operators.pop();
                        continue;
                    }
                    //関数やカッコ始まりの場合
                    if (nextOp.isFunction() || nextOp.getKind() == OperatorToken.Kind.Bra) {
                        operators.push(nextToken);
                        continue;
                    }
                    int nextTokenLevel = ((OperatorToken) nextToken).getLevel();
                    while (!operators.isEmpty() && nextTokenLevel <= ((OperatorToken) operators.peek()).getLevel()) {
                        invpolTokens.offer(operators.pop());
                    }
                    operators.push(nextToken);
                }
            }
        }
        while (!operators.isEmpty()) {
            invpolTokens.offer(operators.pop());
        }
        return invpolTokens;
    }

    /**
     * @param mathFormula 数学語の文字列
     * @return 数学語のトークン列
     */
    public static final Queue<Token> fromMathTextToMathTokens(String mathFormula) {
        String rawMathFormula = mathFormula;
        Queue<Token> mathTokens = new ArrayDeque<>();

        boolean lastTokenWasNumber = false;
        while (rawMathFormula.length() > 0) {
            char firstC = rawMathFormula.charAt(0);
            if (rawMathFormula.startsWith("**")) {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Pow));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(2);
                continue;
            }
            if (firstC == '*' || firstC == ' ') {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Mul));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(1);
                continue;
            }
            if (firstC == '/') {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Div));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(1);
                continue;
            }
            if (firstC == '+') {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Add));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(1);
                continue;
            }

            if (rawMathFormula.startsWith("abs")) {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Abs));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(3);
                continue;
            }
            if (rawMathFormula.startsWith("exp")) {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Exp));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(3);
                continue;
            }
            if (rawMathFormula.startsWith("sin")) {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Sin));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(3);
                continue;
            }
            if (rawMathFormula.startsWith("cos")) {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Cos));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(3);
                continue;
            }
            if (rawMathFormula.startsWith("tan")) {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Tan));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(3);
                continue;
            }
            if (rawMathFormula.startsWith("sqrt")) {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Sqr));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(4);
                continue;
            }
            if (rawMathFormula.startsWith("^")) {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Pow));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(1);
                continue;
            }
            if (lastTokenWasNumber&&firstC == '(') {//ひとつ前のトークンが数なら * がこの前に入る
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Mul));
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Bra));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(1);
                continue;
            }
            if (!lastTokenWasNumber&&firstC == '(') {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Bra));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(1);
                continue;
            }
            if (firstC == ')') {
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Clb));
                lastTokenWasNumber = true;
                rawMathFormula = rawMathFormula.substring(1);
                continue;
            }
            if (lastTokenWasNumber && firstC == '-') {//ひとつ前のトークンが数なら - は演算子
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Sub));
                lastTokenWasNumber = false;
                rawMathFormula = rawMathFormula.substring(1);
                continue;
            }
            //これより下は今のトークンは数
            if (lastTokenWasNumber) {//ひとつ前のトークンも数なら
                mathTokens.offer(new OperatorToken(OperatorToken.Kind.Mul));
                lastTokenWasNumber = false;
                continue;
            }

            StringBuilder numberText = new StringBuilder(String.valueOf(firstC));
            int i = 1;

            boolean digitMode = Character.isDigit(firstC) || firstC == '.' || firstC == '-';
            if (digitMode) {//最初が数字で
                for (i = 1; i < rawMathFormula.length(); i++) {
                    char nowC = rawMathFormula.charAt(i);
                    if ((Character.isDigit(nowC) || nowC == '.')) numberText.append(nowC);//今も数字
                    else break;
                }
            }

            boolean alphabetMode = Character.isAlphabetic(firstC);
            if (alphabetMode) {//最初がアルファベットで
                for (i = 1; i < rawMathFormula.length(); i++) {
                    if (Character.isAlphabetic(rawMathFormula.charAt(i)))
                        numberText.append(rawMathFormula.charAt(i));//今もアルファベット
                    else break;
                }
            }
            rawMathFormula = rawMathFormula.substring(i);
            mathTokens.offer(new NumberToken(numberText.toString()));
            lastTokenWasNumber = true;
        }

        return mathTokens;
    }
}
