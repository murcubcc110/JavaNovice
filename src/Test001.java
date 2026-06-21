/**
 * 個別実行の検証用クラス。
 */
public class Test001 {
    public static void main(String[] args) {
        System.out.println("Test001が正常に実行されました！");
        System.out.println("受け取った引数の数: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.printf("  引数[%d]: %s%n", i, args[i]);
        }
    }
}
