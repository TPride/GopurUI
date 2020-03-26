package gopur.ui.frame;

import gopur.Gopur;
import gopur.GopurTool;
import gopur.event.command.InputCommandEvent;
import gopur.event.key.InputTabEvent;
import gopur.ui.input.InputMode;
import gopur.Information;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GopurCommandWindow {
    private JFrame jFrame = new JFrame();
    private JTextArea textArea = new JTextArea();
    private JSplitPane splitPane = new JSplitPane(), splitPane1 = new JSplitPane();
    private JTextField textField = new JTextField();
    private GopurTool.TabInput tabInput = Gopur.gopurTool.getTabInput();

    public GopurCommandWindow() {
        jFrame.setTitle("[GopurUI] - ".concat(Information.VERSION));
        jFrame.setSize(700, 600);
        jFrame.setIconImage(new ImageIcon("resource/gopur.png").getImage());
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(true);

        /**
         * 全局键盘监听
         */
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(new KeyEventPostProcessor() {
            public boolean postProcessKeyEvent(KeyEvent e) {
                if (e.getID() != KeyEvent.KEY_PRESSED)
                    return false;
                gopur.event.key.KeyEvent event;
                Gopur.getInstance().getPluginManager().callEvent(event = new gopur.event.key.KeyEvent(e));
                if (!event.isCancelled()) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                        Gopur.getInstance().getCommandMap().dispatch("exit");
                    else if (e.getKeyCode() == KeyEvent.VK_F11) {
                        if (jFrame.getExtendedState() == JFrame.NORMAL)
                            jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        else
                            jFrame.setExtendedState(JFrame.NORMAL);
                    } else if (e.getKeyCode() == KeyEvent.VK_F2)
                        textField.requestFocus();
                }
                return false;
            }
        });

        textField.setFocusTraversalKeysEnabled(false);
        textField.setFont(new Font(null, Font.BOLD, 14));
        if (    !Gopur.getInstance().isReloading) {
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
                            InputPrintln(textField.getText());
                            String cmdName = GopurTool.getCmd(textField.getText());
                            if (Gopur.getInstance().getCommandMap().getCommands().containsKey(cmdName)) {
                                InputCommandEvent event;
                                Gopur.getInstance().getPluginManager().callEvent(event = new InputCommandEvent(textField.getText(), cmdName, GopurTool.getArgs(textField.getText())));
                                if (!event.isCancelled())
                                    Gopur.getInstance().getCommandMap().dispatch(event.getFullLine());
                            } else
                                GopurPrintln("未知指令 `" + (cmdName == null ? "" : cmdName) + "`");
                        } else
                            InputPrintln("");
                        ///////////////////////////////////////////////

                        /**
                         * 重置TAB变量
                         */
                        ///////////////////////////////////////////////
                        if (tabInput.isTab)
                            tabInput.clear();
                        ///////////////////////////////////////////////
                        textArea.setCaretPosition(textArea.getText().length());
                        textField.setText("");
                    } catch (Exception ee) {
                        //no-code
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
                        if (tabInput.isTab)
                            tabInput.clear();
                        if (textField.getText() != null && textField.getText().replace(" ", "").length() > 0 && !textField.getText().equalsIgnoreCase(""))
                            if (Gopur.getInstance().inputed.size() > 0)
                                if (!Gopur.getInstance().inputed.get(Gopur.getInstance().inputed.size() - 1).equals(textField.getText()))
                                    Gopur.getInstance().index = Gopur.getInstance().inputed.size();
                    }
                    ///////////////////////////////////////////////

                    /**
                     * TAB自动填充
                     */
                    ///////////////////////////////////////////////
                    if (e.getKeyCode() == KeyEvent.VK_TAB) {
                        InputTabEvent event;
                        Gopur.getInstance().getPluginManager().callEvent(event = new InputTabEvent(textField.getText()));
                        if (!event.isCancelled()) {
                            if (event.isEnableTab) {
                                if (!tabInput.isTab) {
                                    if (event.getCmd() == null || event.getFullCmd().contains(" "))
                                        return;
                                    String re = tabInput.match(event.getCmd(), Gopur.getInstance().getCommandMap().getCommands().keySet());
                                    textField.setText(re);
                                } else {
                                    if (event.getFullCmd().contains(" ")) {
                                        tabInput.clear();
                                        return;
                                    }
                                    textField.setText(tabInput.next());
                                }
                            }
                        }
                    }

                    ///////////////////////////////////////////////
                    if (e.getKeyCode() == KeyEvent.VK_F1)
                        textField.setText(""); //清空文本
                    ///////////////////////////////////////////////
                }
            });
        }
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                jFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                jFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (Gopur.gopurTool.getClipBoard().getClipboardString() != null && Gopur.gopurTool.getClipBoard().getClipboardString().length() > 0) {
                        textField.replaceSelection((textField.getText() == null ? "" : textField.getText()) + Gopur.gopurTool.getClipBoard().getClipboardString());
                        textField.requestFocus();
                    }
                }
            }
        });

        textArea.setLineWrap(true);
        textArea.setBorder(BorderFactory.createEmptyBorder());
        textArea.setSelectedTextColor(Color.GRAY);
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
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (textArea.getSelectedText() != null && textArea.getSelectedText().length() > 0) {
                        Gopur.gopurTool.getClipBoard().setClipboardString(textArea.getSelectedText());
                    }
                }
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
        jFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                splitPane.setDividerLocation((jFrame.getHeight() - 75));
            }
        });
        jFrame.setContentPane(splitPane);
    }

    public void GopurPrint(String string) {
        print("Gopur > ".concat(string));
    }

    public void GopurPrintln(String string) {
        print("Gopur > ".concat(string).concat("\n\n"));
    }

    public void InputPrint(String string) {
        print("Input > ".concat(string));
    }

    public void InputPrintln(String string) {
        print("Input > ".concat(string).concat("\n"));
    }

    public void print(String string) {
        textArea.append(string);
        textArea.setCaretPosition(textArea.getText().length());
    }

    public GopurTool.TabInput getTabInput() {
        return tabInput;
    }

    public void clear() {
        textArea.setText("");
    }

    public final JFrame getFrame() {
        return jFrame;
    }
}
