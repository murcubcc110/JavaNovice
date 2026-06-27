import java.util.ArrayList;
import java.util.List;

public class PrimitiveAndGenericsDemo {
    public static void main(String[] args) {

        // ==========================================
        // 1. プリミティブ型と参照型（ラッパークラス）の基本
        // ==========================================
        int primitiveInt = 100; // プリミティブ型（値そのものを保持、小文字）
        Integer wrapperInt = Integer.valueOf(100); // 参照型（オブジェクト、大文字）

        // ==========================================
        // 2. オートボクシング（自動変換）
        // ==========================================
        // プリミティブ型 -> 参照型 への自動変換 (Auto-boxing)
        Integer autoBoxed = primitiveInt;

        // 参照型 -> プリミティブ型 への自動変換 (Auto-unboxing)
        int autoUnboxed = wrapperInt;

        System.out.println("--- 1. 基本的な変換 ---");
        System.out.println("オートボクシングされた値: " + autoBoxed);
        System.out.println("アンボクシングされた値: " + autoUnboxed);
        System.out.println();

        // ==========================================
        // 3. ジェネリクスでの利用（プリミティブ型は指定不可）
        // ==========================================
        System.out.println("--- 2. ジェネリクスとラッパークラス ---");

        // List<int> list = new ArrayList<>(); // コンパイルエラー！プリミティブ型は使えない
        List<Integer> integerList = new ArrayList<>(); // OK: 参照型を指定

        // コレクションに追加する際、内部で int から Integer へ自動変換（ボクシング）される
        integerList.add(10);
        integerList.add(20);

        // 取り出す際、内部で Integer から int へ自動変換（アンボクシング）される
        for (int value : integerList) {
            System.out.println("リストから取り出したint値: " + value);
        }
        System.out.println();

        // ==========================================
        // 4. 【試験頻出】nullが絡むアンボクシングの罠
        // ==========================================
        System.out.println("--- 3. 試験で狙われるNullPointerExceptionの罠 ---");

        Integer nullableWrapper = null; // 参照型なので null を代入可能

        try {
            // null のオブジェクトを、プリミティブ型（int）の変数に代入しようとする
            // 内部で「nullableWrapper.intValue()」が呼び出されるため、エラーになる
            int trap = nullableWrapper;

            System.out.println("この行は実行されません: " + trap);
        } catch (NullPointerException e) {
            System.out.println("【警告】NullPointerException が発生しました！");
            System.out.println("理由: null の参照型をプリミティブ型に自動変換（アンボクシング）しようとしたため。");
        }
    }
}