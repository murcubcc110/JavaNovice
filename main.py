from datetime import datetime
import timezonefinder
import pytz

def main():
    # 日本時間（JST）の現在時刻を取得
    jst = pytz.timezone('Asia/Tokyo')
    now_jst = datetime.now(jst)
    
    # ログ用のテキストを作成
    log_message = f"GitHub Actions 動作確認成功！実行日時 (JST): {now_jst.strftime('%Y-%m-%d %H:%M:%S')}\n"
    
    # 1. コンソール（GitHub Actionsのログ画面）に出力
    print("-" * 50)
    print(log_message.strip())
    print("-" * 50)
    
    # 2. 動作確認用のファイルを作成して書き込み
    with open("log.txt", "w", encoding="utf-8") as f:
        f.write(log_message)
    print("log.txt を正常に作成しました。")

if __name__ == "__main__":
    main()

