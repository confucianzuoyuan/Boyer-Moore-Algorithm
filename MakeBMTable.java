/// aokako
/// kokako

///  akako
/// kokako

///   aako
/// kokako

///    ako
/// kokako

///     ao
/// kokako

///      a
/// kokako

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class MakeBMTable {
    public static int strong_good_suffix(char[] pattern, char[] text) {
        /// 寻找text的后缀同时是pattern前缀这个子字符串的长度
        int maxLen = 0;

        for (int prefixLen = 1; prefixLen <= pattern.length; prefixLen++) {
            boolean match = true;

            // 如果当前前缀长度大于text的长度，则无法继续
            if (prefixLen > text.length) break;

            for (int i = 0; i < prefixLen; i++) {
                if (pattern[i] != text[text.length - prefixLen + i]) {
                    match = false;
                    break;
                }
            }

            if (match) {
                maxLen = prefixLen;
            }
        }

        /// 处理 ako <====> kokako
        int j = text.length - 1;
        int k = pattern.length - 1;
        while (j >= 0) {
            if (text[j] == pattern[k]) {
                j--;
                k--;
            } else {
                break;
            }
        }
        if (j == -1) {
            return 0;
        }

        /// 处理 a <====> kokako
        if (pattern[pattern.length - 1] != text[text.length - 1]) {
            k = pattern.length - 1;
            while (k >= 0) {
                if (pattern[k] != text[text.length - 1]) {
                    k--;
                } else {
                    return pattern.length - 1 - k;
                }
            }
        }

        // 模板长度 - 前缀长度 ==> 跳过的字符数
        return pattern.length - maxLen;
    }

    public static ArrayList<char[]> makeTextList(char[] pattern, char c) {
        ArrayList<char[]> result = new ArrayList<>();
        for (int i = 0; i < pattern.length; i++) {
            char[] text = new char[pattern.length - i];
            text[0] = c;
            for (int j = 1; j < pattern.length - i; j++) {
                text[j] = pattern[i + j];
            }
            result.add(text);
        }

        return result;
    }

    public static char[] deduplicateAndSort(char[] chars) {
        // 使用TreeSet去重并排序
        Set<Character> charSet = new TreeSet<>();
        for (char c : chars) {
            charSet.add(c);
        }

        // 将TreeSet转换为char数组
        char[] result = new char[charSet.size()];
        int index = 0;
        for (Character c : charSet) {
            result[index++] = c;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        String p = args[0];
        String filename = args[1];
        boolean ignored = new File(filename).isFile();
        PrintWriter out = new PrintWriter(filename);
        char[] pattern = p.toCharArray();
        out.print("*,");
        for (int i = 0; i < pattern.length - 1; i++) {
            out.print(pattern[i]);
            out.print(",");
        }
        out.println(pattern[pattern.length - 1]);
        for (char c : deduplicateAndSort(pattern)) {
            var result = makeTextList(pattern, c);
            out.print(c);
            out.print(",");
            for (int i = 0; i < result.size() - 1; i++) {
                out.print(strong_good_suffix(pattern, result.get(i)));
                out.print(",");
            }
            out.println(strong_good_suffix(pattern, result.getLast()));
        }

        /// 打印最后一行
        char notIn = (char) 0;
        for (int j = 1; j < 256; j++) {
            if (p.indexOf((char)j) == -1) {
                notIn = (char)j;
                break;
            }
        }

        var result = makeTextList(pattern, notIn);
        out.print("*,");
        for (int i = 0; i < result.size() - 1; i++) {
            out.print(strong_good_suffix(pattern, result.get(i)));
            out.print(",");
        }
        out.println(strong_good_suffix(pattern, result.getLast()));
        out.close();
    }
}