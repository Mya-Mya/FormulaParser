package application;

import formulaparser.FormulaLanguage;
import formulaparser.Token;
import view.IMainFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import formulaparser.FormulaParser;

public class FormulaParserAPI {
    private IMainFrame iMainFrame;
    private FormulaLanguage resultLanguage=FormulaLanguage.Invpol;

    public FormulaParserAPI(IMainFrame iMainFrame){
        this.iMainFrame=iMainFrame;
    }
    public void init(){
        List<String>languageList=new ArrayList<>();
        for(FormulaLanguage l:FormulaLanguage.values()){
            languageList.add(l.toString());
        }
        iMainFrame.setLanguages(languageList.toArray(new String[]{}));
    }
    public void inputMathFormula(String s){
        switch (resultLanguage){
            case Invpol:
                Runnable e=()->{
                    Queue<Token>mathTokens=FormulaParser.fromMathTextToMathTokens(s);
                    Queue<Token>invpolTokens=FormulaParser.fromMathTokensToInvpolTokens(mathTokens);
                    String invpolText=FormulaParser.fromTokensToText(invpolTokens);
                    iMainFrame.setResultFormulaText(invpolText);
                };
                e.run();

        }
        Runtime.getRuntime().gc();
    }

    public void setResultLanguage(String selectedItem) {
        for(FormulaLanguage l:FormulaLanguage.values()){
            if (selectedItem.equals(l.toString())) {
                resultLanguage=l;
                break;
            }
        }
    }
}
