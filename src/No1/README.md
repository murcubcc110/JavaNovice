# Java コネクション（JDBC）とジェネリクス解説

Java SE 17 Gold試験対策として重要なポイントを簡潔にまとめています。

---

## 1. コネクション (JDBC)

### 接続の取得と自動クローズ
- **接続方法**: `DriverManager.getConnection(url, user, password)` を使用。
- **リソース管理**: `Connection`, `Statement`, `ResultSet` は `AutoCloseable` を実装しているため、**`try-with-resources`** 文で自動クローズする。
- **クローズ順序**: 宣言されたリソースは**逆順**で自動クローズされる（`ResultSet` -> `Statement` -> `Connection`）。

```java
try (Connection conn = DriverManager.getConnection(url, user, pass);
     PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
    pstmt.setInt(1, 100); // プレースホルダは1始まり（0ではない）
    try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
    }
} // ResultSet, PreparedStatement, Connection の順に自動クローズされる
```

### トランザクション制御
- **自動コミット**: デフォルトは `true`（自動コミット有効）。
- **手動制御**: `conn.setAutoCommit(false)` でトランザクションを開始し、`conn.commit()` または `conn.rollback()` を明示的に呼び出す。
- **セーブポイント**: `conn.setSavepoint()` を使用して、トランザクション内の特定の地点までロールバック可能。

---

## 2. ジェネリクス (Generics)

### ワイルドカードの読み書き規則（PECS原則）
ジェネリクスで最も試験に出題されるのは、ワイルドカードに対する「読み込み (Get)」と「書き込み (Add)」の可否です。
- **PECS**: **P**roducer **E**xtends, **C**onsumer **S**uper

| 宣言 | 読み込み（Get）時の型 | 要素の追加（Add）可否 | 概要 |
| :--- | :--- | :--- | :--- |
| `List<?>` | `Object` としてのみ取得可 | `null` 以外不可 | 非境界ワイルドカード |
| `List<? extends T>` | **`T`** として取得可 | **不可**（`null` のみ可） | 上限境界（共変 / Producer） |
| `List<? super T>` | `Object` としてのみ取得可 | **`T` およびそのサブクラス**を追加可 | 下限境界（反変 / Consumer） |

### ジェネリックメソッドの宣言
- 型パラメータ `<T>` の位置は、**修飾子（public等）の後、戻り値の型（void等）の前**。

```java
// 正しい宣言
public static <T> void print(T item) { ... }
```

### 型消去 (Type Erasure) と制限
Javaのジェネリクスはコンパイル時に型情報が消去される（後方互換性のため）。これにより以下の制限があります。
- **インスタンス化不可**: `new T()` や `new T[5]` はコンパイルエラー。
- **実行時型チェック不可**: `list instanceof List<String>` はコンパイルエラー（`List<?>` は可能）。
- **staticメンバーの制限**: クラスで宣言された型パラメータ `T` は、`static` フィールドや `static` メソッドのシグネチャ・処理内で使用不可（`static` メソッド自体で定義した `<T>` は使用可能）。
