// DOMが読み込まれたら実行する
document.addEventListener('DOMContentLoaded', () => {
  const importCsvButton = document.getElementById('importCsvButton');
  // あらかじめ HTML に用意した <input id="fileInput"> を取得
  const fileInput = document.getElementById('fileInput');

  // 「CSVインポート」ボタンがクリックされたらファイル処理を開始
  importCsvButton.addEventListener('click', async () => {
    // 1. ユーザーがファイルを選択しているか確認
    const file = fileInput.files[0];
    if (!file) {
      alert('ファイルが選択されていません。');
      return;
    }

    // 2. 拡張子チェック（.csv でない場合は弾く）
    const fileName = file.name;
    if (!fileName.toLowerCase().endsWith('.csv')) {
      alert('CSVファイル（.csv）を選択してください。');
      return;
    }

    // 3. サーバーへアップロード（非同期処理）
    try {
      await uploadCsvFile(file);
      alert('CSVファイルがアップロードされました');
      // 同じファイルを再度選べるように input の選択をクリア
      fileInput.value = '';
    } catch (error) {
      console.error('アップロードエラー:', error);
      alert(`アップロードに失敗しました\n${error.message}`);
    }
  });
});

// ファイルアップロード関数
async function uploadCsvFile(file) {
  const url = '/import';
  const formData = new FormData();
  formData.append('file', file);

  // fetch API を使って POST リクエスト
  const response = await fetch(url, {
    method: 'POST',
    body: formData,
  });

  // ステータスコードが 200 台でなければエラー扱い
  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'サーバーエラーが発生しました。');
  }
}
