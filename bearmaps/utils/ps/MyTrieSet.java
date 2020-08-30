package bearmaps.utils.ps;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;

public class MyTrieSet{
    Node root;

    public MyTrieSet() {
        root = new Node(' ', false);
    }

    public void clear() {
        root = new Node(' ', false);
    }

    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            return true;
        }
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                return false;
            }
            curr = curr.map.get(c);
        }
        return curr.isKey;
    }

    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    public List<String> keysWithPrefix(String prefix) {
        LinkedList<String> output = new LinkedList<>();
        Node curr = root;
        for (int i = 0, n = prefix.length(); i < n; i++) {
            char c = prefix.charAt(i);
            if (!curr.map.containsKey(c)) {
                return output;
            }
            curr = curr.map.get(c);
        }
        if (curr.isKey) {
            output.add(prefix);
        }
        for (Character child : curr.map.keySet()) {
            helper(curr.map.get(child), prefix, output);
        }
        return output;
    }

    public void helper(Node n, String prefix, List<String> output) {
        if (n.isKey) {
            output.add(prefix + n.ch);
        }
        for (Character child : n.map.keySet()) {
            helper(n.map.get(child), prefix + n.ch, output);
        }
    }

    private class Node {
        char ch;
        boolean isKey;
        HashMap<Character, Node> map;

        Node(char c, boolean b) {
            this.ch = c;
            this.isKey = b;
            this.map = new HashMap();
        }
    }
}
