package gopur;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class GopurTool {
    private final ClipBoard clipBoard = new ClipBoard();

    public GopurTool() {

    }

    public static String getAuthors() {
        String result = "";
        for (int i = 0; i < Information.AUTHORS.length; i++)
            result += Information.AUTHORS[i] + (Information.AUTHORS.length > i + 1 ? "," : "");
        return result;
    }

    public static String getCmd(String cmd_line) {
        try {
            if (cmd_line == null || cmd_line.length() == 0)
                return null;
            if (!cmd_line.contains(" "))
                return cmd_line;
            String result = cmd_line.trim().substring(0, cmd_line.indexOf(" ")).replace(" ", "");
            return result.length() == 0 ? null : result;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String[] getArgs(String cmd_line) {
        try {
            if (cmd_line == null || cmd_line.length() == 0 || !cmd_line.contains(" ") || cmd_line.substring(cmd_line.indexOf(" "), cmd_line.length()).trim().length() == 0)
                return new String[0];
            return cmd_line.substring(cmd_line.indexOf(" "), cmd_line.length()).trim().split(" ");
        } catch (Exception e) {
            return new String[0];
        }
    }

    public DomainName getDomainName(String domainName) {
        return new DomainName(domainName);
    }

    public PortCheck getPortCheck(DomainName domainName) {
        return new PortCheck(domainName);
    }

    public TabInput getTabInput() {
        return new TabInput();
    }

    public ClipBoard getClipBoard() {
        return clipBoard;
    }

    public class DomainName {
        public String domainName;

        public DomainName(String domainName) {
            this.domainName = domainName;
        }

        public InetAddress getInetAddress() {
            if (domainName == null || domainName.length() == 0) return null;
            try {
                return InetAddress.getByName(domainName);
            } catch (UnknownHostException e) {
                return null;
            }
        }

        public String parse() {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress == null)
                return "~";
            return inetAddress.getHostAddress();
        }

        public String getName() {
            return domainName.length() == 0 ? "~" : domainName;
        }
    }

    public class PortCheck {
        private DomainName domainName;

        public PortCheck(DomainName domainName) {
            this.domainName = domainName;
        }

        public boolean check(String port) {
            if (domainName.parse().equalsIgnoreCase("~")) return false;
            Pattern pattern = Pattern.compile("[0-9]*");
            if (port == null)
                return false;
            if (!pattern.matcher(port).matches())
                return false;
            if (pattern.matcher(port).matches() && (Integer.parseInt(port) < 1 || Integer.parseInt(port) > 65535))
                return false;
            return isConnectable(port);
        }

        public boolean checkPrint(String port) {
            if (domainName.parse().equalsIgnoreCase("~")) {
                Gopur.getLogger().info("无效IP", 2);
                return false;
            }
            if (port == null || port.length() == 0) {
                Gopur.getLogger().info("无输入的端口", 2);
                return false;
            }
            Pattern pattern = Pattern.compile("[0-9]*");
            if (!pattern.matcher(port).matches()) return false;
            if ((Integer.parseInt(port) < 1 || Integer.parseInt(port) > 65535)) return false;
            if (isConnectable(port)) {
                Gopur.getLogger().info("\t" + port.concat(" - ").concat("开启"));
                return true;
            }
            return false;
        }

        public boolean isConnectable(String port){
            if (domainName.parse().equalsIgnoreCase("~")) return false;
            Pattern pattern = Pattern.compile("[0-9]*");
            if (!pattern.matcher(port).matches())
                return false;
            int p = Integer.parseInt(port);
            if (p < 1 || p > 65535)
                return false;
            SocketAddress socketAddress = new InetSocketAddress(domainName.parse(), p);
            Socket socket = new Socket();
            try {
                socket.connect(socketAddress, 1200);
                socket.close();
                return true;
            } catch (SocketTimeoutException exception) {
                return false;
            } catch (IOException exception) {
                return false;
            }
        }

        public DomainName getDomainName() {
            return domainName;
        }
    }

    public class TabInput {
        public boolean isTab = false;
        public int tabArrayIndex = -1;
        public String[] tabArray = null;

        public TabInput() {

        }

        public String match(String string, List<String> list) {
            if (string == null || string.length() == 0)
                return "";
            ArrayList<String> result = new ArrayList<>();
            String top = null;
            String s = string.toLowerCase();
            for (Iterator<String> iterator = new LinkedList<>(list).iterator(); iterator.hasNext();) {
                String cmd = iterator.next();
                String cmd1 = cmd.toLowerCase();
                if (cmd1.length() == string.length()) { //绝对长度
                    if (cmd1.equals(s)) //匹配
                        top = cmd; //置顶
                } else if (cmd1.length() > string.length()) { //相对长度
                    if (cmd1.substring(0, string.length()).equals(s))
                        result.add(cmd);
                }
            }
            if (top != null)
                result.add(0, top);
            String[] re = sort(result.toArray(new String[0]));
            if (re.length > 1) {
                isTab = true;
                tabArrayIndex = 0;
                tabArray = re;
            } else if (re.length == 0)
                return "";
            return re[0];
        }

        public String match(String string, Set<String> set) {
            if (string == null || string.length() == 0)
                return "";
            ArrayList<String> result = new ArrayList<>();
            String top = null;
            String s = string.toLowerCase();
            for (Iterator<String> iterator = new LinkedList<>(set).iterator(); iterator.hasNext();) {
                String cmd = iterator.next();
                String cmd1 = cmd.toLowerCase();
                if (cmd1.length() == string.length()) { //绝对长度
                    if (cmd1.equals(s)) //匹配
                        top = cmd; //置顶
                } else if (cmd1.length() > string.length()) { //相对长度
                    if (cmd1.substring(0, string.length()).equals(s))
                        result.add(cmd);
                }
            }
            if (top != null)
                result.add(0, top);
            String[] re = sort(result.toArray(new String[0]));
            if (re.length > 1) {
                isTab = true;
                tabArrayIndex = 0;
                tabArray = re;
            } else if (re.length == 0)
                return "";
            return re[0];
        }

        public String next() {
            if (tabArray.length == 0)
                return "";
            if (tabArrayIndex + 1 >= tabArray.length)
                tabArrayIndex = 0;
            else
                tabArrayIndex += 1;
            return tabArray[tabArrayIndex];
        }

        public void clear() {
            isTab = false;
            tabArrayIndex = -1;
            tabArray = null;
        }

        /* 对比算法(搜索用)
        private boolean contains(String string1, String string) {
            if (string1 == null || string == null || string1.length() == 0 || string.length() == 0)
                return false;
            if (string1.equalsIgnoreCase(string))
                return true;
            char[] chars = string.toCharArray();
            int ii = 0;
            for (int i = 0; i < chars.length; i++) {
                if (string1.contains(new String(new char[]{chars[i]})))
                    ii++;
                else break;
            }
            return ii == chars.length;
        }
         */

        private String[] sort(String[] array) {
            if (array.length == 0)
                return new String[0];
            TreeSet<String> set = new TreeSet<>(new Compare());
            Collections.addAll(set, array);
            return set.toArray(new String[0]);
        }
    }

    class Compare implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int temp = o1.length() - o2.length();
            return temp == 0 ? o1.compareTo(o2) : temp;
        }
    }


    public class ClipBoard {
        public ClipBoard() {

        }

        public void setClipboardString(String text) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
        }

        public String getClipboardString() {

            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null) {
                if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        return (String) transferable.getTransferData(DataFlavor.stringFlavor);
                    } catch (Exception e) {
                        //no-code
                    }
                }
            }
            return null;
        }
    }
}
