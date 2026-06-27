from datetime import datetime
import timezonefinder
import pytz
import urllib.request
import re
import json
import ssl

def fetch_macbook_air_m5_refurbished():
    """
    Apple認定整備済製品のMacページからMacBook Air M5の販売情報を取得する。
    """
    url = "https://www.apple.com/jp/shop/refurbished/mac"
    headers = {
        "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    }
    req = urllib.request.Request(url, headers=headers)
    
    # macOS等のPython環境でSSL証明書検証に失敗する場合があるため、非検証コンテキストを使用
    context = ssl._create_unverified_context()
    
    try:
        with urllib.request.urlopen(req, context=context) as response:
            html = response.read().decode("utf-8")
    except Exception as e:
        print(f"データの取得に失敗しました: {e}")
        return []
        
    # window.REFURB_GRID_BOOTSTRAP = {...} を探す
    match = re.search(r'window\.REFURB_GRID_BOOTSTRAP\s*=\s*(\{.*?\});', html, re.DOTALL)
    if not match:
        match = re.search(r'window\.REFURB_GRID_BOOTSTRAP\s*=\s*(\{.*?\}).*?<\/script>', html, re.DOTALL)
        
    if not match:
        print("商品データ (REFURB_GRID_BOOTSTRAP) が見つかりませんでした。")
        return []
        
    json_str = match.group(1)
    try:
        data = json.loads(json_str)
    except Exception as e:
        print(f"JSONのパースに失敗しました: {e}")
        return []
        
    products = data.get('tiles', [])
    m5_air_products = []
    
    for p in products:
        title = p.get('title') or ""
        # タイトルに "macbook air" と "m5" が含まれているかを確認
        if 'macbook air' in title.lower() and 'm5' in title.lower():
            # 価格情報を取得
            price_info = p.get('price', {})
            current_price = price_info.get('currentPrice', {}).get('amount', '価格情報なし')
            
            # 詳細URLを構築
            details_url = p.get('productDetailsUrl') or ""
            if details_url and not details_url.startswith('http'):
                details_url = "https://www.apple.com" + details_url
                
            m5_air_products.append({
                "title": title,
                "price": current_price,
                "part_number": p.get('partNumber'),
                "url": details_url
            })
            
    return m5_air_products

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
    
    # MacBook Air M5 の販売情報を取得
    print("Apple認定整備済製品ページからMacBook Air M5の販売情報を取得中...")
    m5_products = fetch_macbook_air_m5_refurbished()
    
    product_log = ""
    if m5_products:
        product_log += f"\n【MacBook Air M5 整備済製品 販売情報】\n"
        for i, p in enumerate(m5_products, 1):
            product_log += f"{i}. {p['title']}\n"
            product_log += f"   価格: {p['price']}\n"
            product_log += f"   型番: {p['part_number']}\n"
            product_log += f"   URL: {p['url']}\n"
    else:
        product_log += "\n現在、MacBook Air M5 の整備済製品の在庫はありません。\n"
        
    print(product_log)
    log_message += product_log
    
    # 2. 動作確認用のファイルを作成して書き込み
    with open("log.txt", "w", encoding="utf-8") as f:
        f.write(log_message)
    print("log.txt を正常に作成しました。")

if __name__ == "__main__":
    main()

