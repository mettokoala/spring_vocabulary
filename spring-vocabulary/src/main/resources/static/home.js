document.addEventListener('DOMContentLoaded', () =>{
	getAllWords();
	
	// ボタンのイベント登録
	const createButton = document.getElementById('createButton');
	createButton.addEventListener('click', createWord);
	  
	const fileInput = document.getElementById('fileInput');
	const importCsvButton = document.getElementById('importCsvButton');
	
	importCsvButton.addEventListener('click', async () =>{
		const file = fileInput.files[0];
		if (!file){
			alert('ファイルが選択されていません');
			return
		}
		const fileName = file.name;
		if(!fileName.toLowerCase().endsWith('.csv')){
			alert('CSVファイルを選択してください');
			return
		}
		try{
			await uploadCsvFile(file);
			alert('アップロードを行いました');
			fileInput.value = '';
		}catch(error){
			console.error('アップロードエラー',error);
			alert('アップロードエラー', error);
		}
		
		
	})
})

// ステータス更新関数
async function updateStatus(wordId, status) {
    try {
        const response = await fetch(`/words/${wordId}/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status })
        });
        if (!response.ok) {
			const errorText = await response.text();
			throw new Error(errorText || 'ステータス更新に失敗しました');
        }
		removeWordFromList(wordId);
    } catch (error) {
        console.error(error);
        alert('ステータス更新に失敗しました');
    }
}

// 単語の削除
async function deleteWord(wordId) {
    if (!confirm('本当に削除しますか？')) return;
    try {
        const response = await fetch(`/words/${wordId}`, { method: 'DELETE' });
        if (!response.ok) {
			const errorText = await response.text();
			throw new Error(errorText || '削除に失敗しました');
        }
        removeWordFromList(wordId);
    } catch (error) {
        console.error(error);
        alert('削除に失敗しました');
    }
}

async function uploadCsvFile(file){
	const url = '/import';
	const formData = new FormData();
	formData.append('file', file);
	
	const response = await fetch(url, {
		method: 'POST',
		body: formData,
	});
	
	if (!response.ok){
		const errorText = await response.text();
		throw new Error(errorText || 'サーバーエラー');
	}
}

// 単語一覧を取得して表示する関数
async function getAllWords() {
    try {
        const response = await fetch('/words');
        if (!response.ok) {
			const errorText = await response.text();
			throw new Error(errorText || 'サーバーエラー');
        }
        const data = await response.json();
        createWordList(data); // データをHTMLに設定
    } catch (error) {
        // エラーをコンソールに表示
        console.error(error);
        // アラートでエラーメッセージを表示
        alert('単語の読み込みに失敗しました。');
    }
}

// 単語の新規作成
async function createWord() {
    // 入力内容を取得
    const question = document.getElementById('question').value.trim();
    const answer = document.getElementById('answer').value.trim();
	
	if (!question || !answer) {
	      alert('問題と解答は両方入力してください');
	      return;
	}

    try {
      const response = await fetch('/words', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ question, answer })
      });

      if (!response.ok) {
        // ステータスが200系以外の場合はエラーを投げる
		const errorText = await response.text();
		throw new Error(errorText || '作成に失敗しました');
      }
      // 成功時の表示・処理
      document.getElementById('createForm').reset(); // フォームのリセット
	  getAllWords();
    } catch (error) {
      console.error(error);
      alert('作成に失敗しました。');
    }
}

// データをHTMLの単語一覧に反映する関数
function createWordList(words) {
    const wordListElement = document.querySelector('ul');
    wordListElement.innerHTML = ''; 

    words.forEach(word => {
        const listItem = document.createElement('li');
        listItem.className = 'word-item';
        listItem.dataset.wordId = word.id;

        // 切り替えボタン
        const toggleButton = document.createElement('button');
        toggleButton.className = 'toggle-text-button';
        toggleButton.textContent = word.question;
		// イベントリスナー登録
        toggleButton.addEventListener('click', () => {
            toggleText(toggleButton, word.question, word.answer);
        });

        // ステータス更新ボタン
        const statusButton = document.createElement('button');
        statusButton.className = 'status-button';
        statusButton.textContent = 'OK';
		// イベントリスナー登録
        statusButton.addEventListener('click', () => {
            updateStatus(word.id, true);
        });

        // 削除ボタン
        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-button';
        deleteButton.textContent = '削除';
		// イベントリスナー登録
        deleteButton.addEventListener('click', () => {
			deleteWord(word.id);
        });

        listItem.appendChild(toggleButton);
        listItem.appendChild(statusButton);
        listItem.appendChild(deleteButton);
        wordListElement.appendChild(listItem);
    });
}

// 解答表示を切り替える関数
function toggleText(button, question, answer) {
	button.textContent = button.textContent === question ? answer : question;
}

// UL 内の該当 LI を削除する関数
function removeWordFromList(id) {
  // UL 要素を取得
  const wordList = document.querySelector('ul');
  // data-word-id 属性でマッチする LI を探す
  const selector = `li.word-item[data-word-id="${id}"]`;
  const listItem = wordList.querySelector(selector);

  // 見つかったら DOM から削除
  if (listItem) {
    listItem.remove();
  }
}