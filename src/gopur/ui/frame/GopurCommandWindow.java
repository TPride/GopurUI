package gopur.ui.frame;

import gopur.Gopur;
import gopur.GopurTool;
import gopur.ui.input.InputMode;
import gopur.Information;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GopurCommandWindow {
    private JFrame jFrame = new JFrame();
    private JTextArea textArea = new JTextArea();
    private JSplitPane splitPane = new JSplitPane(), splitPane1 = new JSplitPane();
    private boolean isTab = false;
    private int tabArrayIndex = -1;
    private String[] tabArray = null;

    public GopurCommandWindow() {
        jFrame.setTitle("[GopurUI] - ".concat(Information.VERSION));
        jFrame.setSize(700, 600);
        jFrame.setIconImage(new ImageIcon("resource/gopur.png").getImage());
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
            }
        });

        JTextField textField = new JTextField();
        textField.setFocusTraversalKeysEnabled(false);
        textField.setFont(new Font(null, Font.BOLD, 14));
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    /**
                     * 排序历史输入记录
                     */
                    ///////////////////////////////////////////////
                    if (Gopur.receive.getMode() == InputMode.DONT)
                        return;
                    if (textField.getText() != null && textField.getText().replace(" ", "").length() > 0 && !textField.getText().equalsIgnoreCase("")) { // 如果文本框不是空的
                        if (Gopur.getInstance().inputed.size() == 0) { // 如果inputed也是空的
                            Gopur.getInstance().inputed.add(0, textField.getText());
                        } else {
                            if (!Gopur.getInstance().inputed.get(Gopur.getInstance().inputed.size() - 1).equals(textField.getText())) {
                                if (Gopur.getInstance().index != Gopur.getInstance().inputed.size()) { //如果当前索引不是自然索引
                                    if (Gopur.getInstance().inputed.get(Gopur.getInstance().index).equals(textField.getText())) {
                                        String indexString = Gopur.getInstance().inputed.get(Gopur.getInstance().index);
                                        Gopur.getInstance().inputed.remove(Gopur.getInstance().index);
                                        Gopur.getInstance().inputed.add(Gopur.getInstance().inputed.size(), indexString);
                                    } else
                                        Gopur.getInstance().inputed.add(Gopur.getInstance().inputed.size(), textField.getText());
                                } else
                                    Gopur.getInstance().inputed.add(Gopur.getInstance().inputed.size(), textField.getText());
                            }
                        }
                        Gopur.getInstance().index = Gopur.getInstance().inputed.size();
                        //System.out.println(Gopur.getInstance().inputed);
                        InputPrintln(textField.getText());
                        String cmdName = GopurTool.getCmd(textField.getText());
                        if (Gopur.getInstance().getCommandMap().getCommands().containsKey(cmdName)) {
                            Gopur.getInstance().getCommandMap().dispatch(textField.getText());
                        } else
                            GopurPrintln("未知指令 `" + cmdName + "`");
                    } else
                        InputPrintln("");
                    ///////////////////////////////////////////////

                    /**
                     * 重置TAB变量
                     */
                    ///////////////////////////////////////////////
                    if (isTab) {
                        isTab = false;
                        tabArrayIndex = -1;
                        tabArray = null;
                    }
                    ///////////////////////////////////////////////
                    textArea.setCaretPosition(textArea.getText().length());
                    textField.setText("");
                } catch (Exception ee) {
                    
                }
            }
        });
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                /**
                 * 历史输入记录快捷键实现
                 */
                ///////////////////////////////////////////////
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (Gopur.getInstance().index - 1 < 0)
                        return;
                    Gopur.getInstance().index -= 1;
                    textField.setText(Gopur.getInstance().inputed.get(Gopur.getInstance().index));
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (Gopur.getInstance().index + 1 >= Gopur.getInstance().inputed.size())
                        return;
                    Gopur.getInstance().index += 1;
                    textField.setText(Gopur.getInstance().inputed.get(Gopur.getInstance().index));
                }
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (isTab) {
                        isTab = false;
                        tabArrayIndex = -1;
                        tabArray = null;
                    }
                    if (textField.getText() != null && textField.getText().replace(" ", "").length() > 0 && !textField.getText().equalsIgnoreCase(""))
                        if (Gopur.getInstance().inputed.size() > 0)
                            if (!Gopur.getInstance().inputed.get(Gopur.getInstance().inputed.size() - 1).equals(textField.getText()))
                                Gopur.getInstance().index = Gopur.getInstance().inputed.size();
                }
                ///////////////////////////////////////////////

                /**
                 * TAB自动联想
                 */
                ///////////////////////////////////////////////
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    if (!isTab) {
                        if (textField.getText() == null || textField.getText().contains(" "))
                            return;
                        String[] strings = Gopur.gopurTool.getTabInput().matchCommands(textField.getText());
                        if (strings.length == 0)
                            return;
                        else if (strings.length > 1) {
                            isTab = true;
                            tabArrayIndex = 0;
                            tabArray = strings;
                        }
                        textField.setText(strings[0]);
                    } else {
                        if (tabArrayIndex + 1 >= tabArray.length)
                            tabArrayIndex = 0;
                        else
                            tabArrayIndex += 1;
                        textField.setText(tabArray[tabArrayIndex]);
                    }
                }
                ///////////////////////////////////////////////
            }
        });
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                jFrame.setCursor(new Cursor(Cursor.TEXT_CURSOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                jFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        textArea.setLineWrap(true);
        textArea.setFont(new Font("宋体", Font.BOLD, 14));
        textArea.setBackground(Color.BLACK);
        //textArea.setBackground(new Color(50, 59, 72));
        textArea.setEditable(false);
        textArea.setForeground(new Color(0, 191, 0));
        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        JScrollPane scrollPane = new JScrollPane(
                textArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        JLabel jLabel = new JLabel();
        jLabel.setFont(new Font(null, Font.BOLD, 15));
        jLabel.setForeground(Color.WHITE);
        jLabel.setText("Input");

        JPanel jPanel = new JPanel();
        jPanel.setBackground(new Color(41, 48, 57));
        jPanel.add(jLabel);

        splitPane1 = new JSplitPane();
        splitPane1.setBackground(Color.BLACK);
        splitPane1.setEnabled(false);
        splitPane1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane1.setContinuousLayout(true);
        splitPane1.setDividerSize(0);
        splitPane1.setIgnoreRepaint(true);
        splitPane1.setLeftComponent(jPanel);
        splitPane1.setRightComponent(textField);

        splitPane = new JSplitPane();
        splitPane.setBackground(Color.BLACK);
        splitPane.setEnabled(false);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(((jFrame.getSize().width / 4) + (jFrame.getSize().width / 2)));
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(0);
        splitPane.setIgnoreRepaint(true);
        splitPane.setTopComponent(scrollPane);
        splitPane.setBottomComponent(splitPane1);

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                textField.requestFocus();
            }
        });
        jFrame.setContentPane(splitPane);
    }

    public void GopurPrint(String string) {
        textArea.append("Gopur > ".concat(string));
    }

    public void GopurPrintln(String string) {
        textArea.append("Gopur > ".concat(string).concat("\n\n"));
    }

    public void InputPrint(String string) {
        textArea.append("Input > ".concat(string));
    }

    public void InputPrintln(String string) {
        textArea.append("Input > ".concat(string).concat("\n"));
    }

    public void print(String string) {
        textArea.append(string);
    }

    public void clear() {
        textArea.setText("");
    }

    public final JFrame getFrame() {
        return jFrame;
    }
}
