import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 文进
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String s = "这台车的发动机有很明显的且让人厌烦的异响，我本来高高兴兴的，结果让我喜爱不起来。";
        StringBuilder res = new StringBuilder();        // 记录结果
        List<String> strLists = new ArrayList<>();      // 将段落分成的句子保存
        int last = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '，' || s.charAt(i) == '。') { // 以"，"和"。"划分句子
                strLists.add(s.substring(last, i));
                last = i + 1;
            }
        }
        for (String str : strLists) { // 每一个句子去匹配
            res.append(match(str));
        }
        if (res.length() > 0) res.deleteCharAt(res.length() - 1); // 去除最后一个";"号
        System.out.println(res);
    }

    static StringBuilder match(String s) throws IOException {
        String words = new String(Files.readAllBytes(Paths.get("src/words.txt")));
        StringBuilder res = new StringBuilder();
        int i = 0, spaceIndex = 0, lastStrIndex = 0; // spaceIndex 表示空格索引，lastStrIndex 上一个字符串的索引
        while (i < words.length()) {
            StringBuilder sb = new StringBuilder();
            while (i < words.length() && words.charAt(i) != '\n' && words.charAt(i) != ':') { // 主要提取配置的字符串
                if (words.charAt(i) == ' ') {
                    spaceIndex = i - lastStrIndex;
                }
                sb.append(words.charAt(i++));
            }
            if (isMatch(s, "." + sb.substring(0, spaceIndex))) { // 看是否匹配
                res.append(sb.substring(0, sb.length()));
                res.append(";");
                while (i < words.length() && words.charAt(i) != '\n') i++;// 忽略当前行的后序字符
            }
            lastStrIndex = ++i;
        }
        return res;
    }

    static boolean isMatch(String s, String p) {
        int i = 0, j = 0, ii = -1, jj = -1; // ii, jj 记录回溯点
        while (i < s.length()) {
            if (j < p.length() && p.charAt(j) == '.') { // 如果在p串中碰到通配符.，复制两串的当前索引，并对p串+1，定位到非通配符位置
                ii = i;
                jj = j;
                j++;
            }
            else if (j < p.length() && s.charAt(i) == p.charAt(j)) {
                i++;
                j++;
            } else if (j == p.length()) { // p 串到末尾
                break;
            } else {
                if (jj == -1) return false;
                j = jj;         // 如果此时在s串在通配符.的监听下， 让p串回到通配符*的位置上继续监听下一个字符
                i = ii + 1;     // 让i回到s串中与通配符对应的当前字符的下一个字符上，也就是此轮匹配只放行一个字符
            }
        }
        while (j < p.length() && p.charAt(j) == '.') j++;
        return j == p.length(); // 此时查看p是否已经检测到最后，如果检测到最后表示匹配成功，否则匹配失败
    }
}