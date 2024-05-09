import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BMSearch {
    public static Map<String, Integer> matrixMap = new HashMap<>();
    public static String pattern = "";

    public static boolean searchInLine(char[] line, char[] pattern) {
        int length = line.length;
        int i = pattern.length - 1;
        while (i < length) {
            int j = pattern.length - 1;
            while (j >= 0 && line[i] == pattern[j]) {
                i--;
                j--;
            }
            if (j == -1) {
                return true;
            } else {
                var key = line[i] + "," + j;
                var value = matrixMap.get(key);
                if (value == null) {
                    i += matrixMap.get("*," + j);
                } else {
                    i += value;
                }
            }
        }
        return false;
    }

    public static void readFileToHashMap(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // 读取第一行
            String[] columns = line.split(","); // 分割第一行获取列名
            // 拼接pattern
            for (int i = 1; i < columns.length; i++) {
                pattern = pattern.concat(columns[i]);
            }

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(","); // 分割其他行获取值
                String rowName = values[0]; // 第一列是行名
                for (int i = 1; i < values.length; i++) {
                    int value = Integer.parseInt(values[i]); // 将值转换为整数
                    matrixMap.put(rowName + "," + (i - 1), value); // 组合行名和列名作为键，值作为值
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String tableFilePath = args[0]; // 替换为实际文件路径
        String textFilePath = args[1];
        readFileToHashMap(tableFilePath);
        BufferedReader reader;
        char[] p = pattern.toCharArray();

        try {
            reader = new BufferedReader(new FileReader(textFilePath));
            String line = reader.readLine();

            while (line != null) {
                if (searchInLine(line.toCharArray(), p)) {
                    System.out.println(line);
                }
                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
