package view;

import application.FormulaParserAPI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements IMainFrame, DocumentListener, ActionListener {
    private FormulaParserAPI app;

    private JTextArea tMathFormula;
    private JComboBox<String> cLanguage;
    private JTextArea tResultFormula;
    private JButton bSend;
    private JCheckBox cSendEveryCharacterPushed;

    public MainFrame(){
        super("FormulaParser");
        setPreferredSize(new Dimension(1300,400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Insets tMargin=new Insets(10,10,10,10);

        JPanel pFormula=new JPanel(new GridLayout(2,1));
        tMathFormula=new JTextArea();
        tMathFormula.setFont(UIConst.formulaFont);
        tMathFormula.setForeground(UIConst.black);
        tMathFormula.setAutoscrolls(true);
        tMathFormula.setMargin(tMargin);
        tMathFormula.setFocusable(true);
        tMathFormula.getDocument().addDocumentListener(this);
        tResultFormula=new JTextArea();
        tResultFormula.setFont(UIConst.formulaFont);
        tResultFormula.setAutoscrolls(true);
        tResultFormula.setForeground(UIConst.blue);
        tResultFormula.setMargin(tMargin);
        pFormula.add(tMathFormula);
        pFormula.add(tResultFormula);
        add(pFormula,BorderLayout.CENTER);

        JToolBar tSetting=new JToolBar();
        cLanguage=new JComboBox<>();
        cLanguage.setActionCommand("LANGUAGE_COMBO");
        cLanguage.addActionListener(this);
        cSendEveryCharacterPushed=new JCheckBox("文字変更毎に送信する");
        cSendEveryCharacterPushed.setFont(UIConst.normalFont);
        cSendEveryCharacterPushed.setSelected(true);
        cSendEveryCharacterPushed.setActionCommand("SEND_EVERY_TIME_CHARACTER_ENTERED");
        cSendEveryCharacterPushed.addActionListener(this);
        bSend=new JButton("送信");
        bSend.setFont(UIConst.normalFont);
        bSend.setActionCommand("SEND");
        bSend.addActionListener(this);
        tSetting.add(cLanguage);
        tSetting.add(cSendEveryCharacterPushed);
        tSetting.add(bSend);
        add(tSetting,BorderLayout.SOUTH);

        app=new FormulaParserAPI(this);
        app.init();
        pack();
        setVisible(true);
    }

    @Override
    public void setResultFormulaText(String s) {
        tResultFormula.setText(s);
    }

    @Override
    public void setLanguages(String[] s) {
        ComboBoxModel<String>model=new DefaultComboBoxModel<>(s);
        cLanguage.setModel(model);
        cLanguage.setSelectedIndex(0);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (cSendEveryCharacterPushed.isSelected()) {
            sendMathFormula();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        if (cSendEveryCharacterPushed.isSelected()) {
            sendMathFormula();
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        if (cSendEveryCharacterPushed.isSelected()) {
            sendMathFormula();
        }
    }

    private long lastSent=System.currentTimeMillis();
    private void sendMathFormula(){
        long now=System.currentTimeMillis();
        if(now-lastSent>30) app.inputMathFormula(tMathFormula.getText());
        lastSent=now;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String acco=e.getActionCommand();
        if (acco.equals(bSend.getActionCommand())) {
            sendMathFormula();
        }
        if(acco.equals(cLanguage.getActionCommand())){
            app.setResultLanguage((String)cLanguage.getSelectedItem());
        }
    }
}
