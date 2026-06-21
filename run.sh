#!/bin/bash

# エラーが発生したら即座にスクリプトを終了する
set -e

# カレントディレクトリをスクリプトが存在する場所に固定
cd "$(dirname "$0")"

# 出力先ディレクトリの定義
OUT_DIR="out"
mkdir -p "$OUT_DIR"

# デフォルトのターゲットファイル
TARGET_FILE="src/Main.java"

# 引数の判定
# 第1引数が指定されており、かつ .java で終わるファイルが存在する場合
if [[ $# -gt 0 && "$1" == *.java && -f "$1" ]]; then
    TARGET_FILE="$1"
    shift
# または、第1引数がクラス名で、src/配下に .java ファイルが存在する場合
elif [[ $# -gt 0 && -f "src/$1.java" ]]; then
    TARGET_FILE="src/$1.java"
    shift
fi

# ターゲットファイルの存在チェック
if [ ! -f "$TARGET_FILE" ]; then
    echo "エラー: 対象のJavaファイルが見つかりません: $TARGET_FILE"
    exit 1
fi

# ファイル名からクラス名（拡張子なし）を取り出す
MAIN_CLASS=$(basename "$TARGET_FILE" .java)

# ビルド（コンパイル）処理
echo "[Build] $TARGET_FILE を $OUT_DIR にコンパイル中..."
javac -d "$OUT_DIR" "$TARGET_FILE"

# 実行処理
echo "[Run] $MAIN_CLASS を実行します..."
echo "----------------------------------------"
java -cp "$OUT_DIR" "$MAIN_CLASS" "$@"
echo "----------------------------------------"

