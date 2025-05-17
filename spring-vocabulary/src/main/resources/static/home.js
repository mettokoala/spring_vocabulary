// DOMが読み込まれたら実行する
document.addEventListener('DOMContentLoaded', () => {
  const importCsvButton = document.getElementById('importCsvButton');
  importCsvButton.addEventListener('click', importCsv);
});

// CSVインポート関数
function importCsv() {
  // 1. ファイル入力要素を動的生成＆非表示
  const fileInput = document.createElement('input');
  fileInput.type = 'file';
  fileInput.accept = '.csv';
  fileInput.style.display = 'none';

  // 2. DOMに追加してからクリック
  document.body.appendChild(fileInput);
  fileInput.click();

  // 3. changeイベントで処理＆後片付け
  fileInput.addEventListener('change', async function () {
    const file = fileInput.files[0];

    // （1）ユーザーが何も選ばずキャンセルした場合
    if (!file) {
      cleanup();
      return;
    }

    // （2）拡張子チェック（大文字小文字を区別しない）
    const fileName = file.name;
    if (!fileName.toLowerCase().endsWith('.csv')) {
      alert('CSVファイルを選択してください。');
      cleanup();
      return;
    }

    // （3）サーバーにアップロード
    try {
      await uploadCsvFile(file);
      alert('CSVファイルがアップロードされました');
    } catch (error) {
      console.error('アップロードエラー:', error);
      alert(`CSVファイルのアップロードに失敗しました\n${error.message}`);
    }

    // 4. 処理後は必ず要素を削除
    cleanup();
  });

  // ファイル入力要素を削除するヘルパー
  function cleanup() {
    fileInput.remove();
  }
}

// ファイルアップロード関数
async function uploadCsvFile(file) {
  const url = '/import';
  const formData = new FormData();
  formData.append('file', file);

  try {
    const response = await fetch(url, {
      method: 'POST',
      body: formData,
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'サーバーエラーが発生しました。');
    }
  } catch (error) {
    throw new Error(error.message || 'ネットワークエラーが発生しました。');
  }
}