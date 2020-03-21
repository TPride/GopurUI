package gopur;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

public class GopurTool {
    private final TabInput tabInput = new TabInput();

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
                return "~";
            if (!cmd_line.contains(" "))
                return cmd_line;
            String result = cmd_line.trim().substring(0, cmd_line.indexOf(" ")).replace(" ", "");
            return result.length() == 0 ? "~" : result;
        } catch (NullPointerException e) {
            return "~";
        }
    }

    public static String[] getArgs(String cmd_line) {
        try {
            if (cmd_line == null || cmd_line.length() == 0 || !cmd_line.contains(" ") || cmd_line.substring(cmd_line.indexOf(" "), cmd_line.length()).trim().length() == 0) return new String[0];
            String[] strings = cmd_line.substring(cmd_line.indexOf(" "), cmd_line.length()).trim().split(" ");
            return strings;
        } catch (NullPointerException e) {
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
        return tabInput;
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
        public TabInput() {

        }

        public String[] matchCommands(String string) {
            if (string == null || string.contains(" ") || string.length() == 0)
                return new String[0];
            ArrayList<String> result = new ArrayList<>();
            String top = null;
            String s = string.toLowerCase();
            for (Iterator<String> iterator = Gopur.getInstance().getCommandMap().getCommands().keySet().iterator(); iterator.hasNext();) {
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
            return sort(result.toArray(new String[0]));
        }

        /** 对比算法(搜索用)
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
         **/

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
}
