# Java ローカライズ（Localization）解説

Java SE 17 Gold試験対策として重要なローカライズに関するポイントを簡潔にまとめています。

---

## 1. Locale（ロケール）の生成

特定の言語や国・地域を表すオブジェクトです。

- **定数の利用**: `Locale.JAPAN`, `Locale.US`（国＋言語）、`Locale.JAPANESE`, `Locale.ENGLISH`（言語のみ）。
- **コンストラクタ**: `new Locale("ja", "JP")` (言語、国) 
  ※Java 19以降は非推奨となり `Locale.of` が推奨されますが、Java 17 Gold試験対策としてはコンストラクタや `Builder` の知識が求められます。
- **Locale.Builder**: `new Locale.Builder().setLanguage("ja").setRegion("JP").build()`。
- **デフォルトの取得**: `Locale.getDefault()`。

---

## 2. ResourceBundle と検索ルール（最重要）

多言語テキストを管理し、`ResourceBundle.getBundle("BaseName", locale)` で取得します。

### 1. リソースバンドルの種類
- **プロパティファイル形式**: `BaseName_ja_JP.properties` のように作成し、キーと値を `key=value` 形式で記述する。
- **Javaクラス形式 (`ListResourceBundle`)**: `ListResourceBundle` クラスを継承し、`getContents()` メソッドで `Object[][]` を返す。プロパティファイルとクラスが両方存在する場合、**Javaクラス形式が優先**されます。

```java
// クラス形式のリソースバンドル例
public class MyResources_ja_JP extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][] {
            { "hello", "こんにちは" }
        };
    }
}
```

### 2. リソースの検索（フォールバック）順序
指定されたロケールに最も近いファイルから検索し、見つからなければデフォルトロケール、最後に基底ファイルへとフォールバックします。

**例：指定ロケール `ja_JP`（日本語/日本）、デフォルトロケール `en_US`（英語/米国）の場合**
1. `BaseName_ja_JP` (指定ロケールの言語＋国)
2. `BaseName_ja` (指定ロケールの言語のみ)
3. `BaseName_en_US` (デフォルトロケールの言語＋国)
4. `BaseName_en` (デフォルトロケールの言語のみ)
5. `BaseName` (基底ファイル。ロケール名なし)
6. いずれにも見つからない場合は `MissingResourceException` が発生。

> [!WARNING]
> 一度特定したファイル（例: `BaseName_ja.properties`）のなかに**キーが存在しない場合でも、それより優先度の高いファイル（例: `ja_JP`）に遡ってキーを探し直すことはしません**。また、親ファイル（基底 `BaseName.properties`）へとキーを探しに行きます。

---

## 3. フォーマット処理 (NumberFormat / DateTimeFormatter)

### 1. 数値と通貨のフォーマット (NumberFormat)
ロケールに応じた数値や通貨の表現を行います。

```java
double amount = 12345.67;
// デフォルトまたは指定ロケールの数値フォーマッタ
NumberFormat nf = NumberFormat.getNumberInstance(Locale.US); 
System.out.println(nf.format(amount)); // 12,345.67

// 通貨フォーマッタ（通貨記号が付与される）
NumberFormat cf = NumberFormat.getCurrencyInstance(Locale.JAPAN);
System.out.println(cf.format(amount)); // ￥12,346 （四捨五入される）
```

### 2. 日時のフォーマット (DateTimeFormatter)
`DateTimeFormatter` に `withLocale(Locale)` を適用してロケールを反映します。

```java
LocalDateTime now = LocalDateTime.now();
DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                                               .withLocale(Locale.US);
System.out.println(now.format(formatter)); // Jun 21, 2026, 2:55:04 PM
```
