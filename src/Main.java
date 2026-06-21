import java.util.Scanner;

/**
 * Java CLIアプリケーションのサンプルクラス。
 * コマンドライン引数の処理と、対話型入力を体験できる実装になっています。
 */
public class Main {

    private static final String VERSION = "1.0.0";

    public static void main(String[] args) {
        String targetName = "ゲスト";
        boolean interactiveMode = false;

        // 1. コマンドライン引数の解析
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                case "--help":
                    printHelp();
                    return;
                case "-v":
                case "--version":
                    System.out.println("Java CLI アプリケーション バージョン: " + VERSION);
                    return;
                case "-n":
                case "--name":
                    if (i + 1 < args.length) {
                        targetName = args[++i];
                    } else {
                        System.err.println("エラー: --name オプションには値を指定してください。");
                        printHelp();
                        return;
                    }
                    break;
                case "-i":
                case "--interactive":
                    interactiveMode = true;
                    break;
                default:
                    System.err.println("警告: 未知のオプションです: " + args[i]);
                    printHelp();
                    return;
            }
        }

        // 2. 対話モードまたは通常の挨拶処理
        if (interactiveMode) {
            runInteractiveMode();
        } else {
            System.out.printf("こんにちは、%sさん！Java CLI環境へようこそ！%n", targetName);
            System.out.println("※ 対話モードで実行するには `-i` または `--interactive` を指定してください。");
        }
    }

    /**
     * ヘルプメッセージを表示します。
     */
    private static void printHelp() {
        System.out.println("使用法: java Main [オプション]");
        System.out.println();
        System.out.println("オプション:");
        System.out.println("  -h, --help         このヘルプメッセージを表示して終了します。");
        System.out.println("  -v, --version      バージョン情報を表示して終了します。");
        System.out.println("  -n, --name <値>    挨拶する対象の名前を指定します。");
        System.out.println("  -i, --interactive  対話モード（インタラクティブ）を開始します。");
    }

    /**
     * Scannerを使用してユーザーと対話するモードを実行します。
     */
    private static void runInteractiveMode() {
        System.out.println("=== 対話モードを開始します ===");
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("何か入力してください (終了するには 'exit' または 'quit' と入力): ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    System.out.println("対話モードを終了します。さようなら！");
                    break;
                }

                if (input.isEmpty()) {
                    System.out.println("入力が空です。");
                    continue;
                }

                // 入力された文字数をカウントする簡単な処理
                System.out.printf("入力された文字列: 「%s」 (文字数: %d)%n", input, input.length());
            }
        }
    }
}
