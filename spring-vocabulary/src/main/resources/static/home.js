document.addEventListener('DOMContentLoaded', () =>{
	getAllWords();
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

// データをHTMLの単語一覧に反映する関数
function createWordList(words) {
    const wordListElement = document.querySelector('ul');
    wordListElement.innerHTML = ''; 

    words.forEach(word => {
        const listItem = document.createElement('li');
        listItem.className = 'word-item';
        listItem.dataset.wordId = word.id;

        const questionSpan = document.createElement('span');
        questionSpan.className = 'word-question';
        questionSpan.textContent = word.question;
        // questionSpan.style.display = 'none';

        const answerSpan = document.createElement('span');
        answerSpan.className = 'word-answer';
        answerSpan.textContent = word.answer;
        // answerSpan.style.display = 'none';

        // 切り替えボタン
        const toggleButton = document.createElement('button');
        toggleButton.className = 'toggle-text-button';
        toggleButton.textContent = '問題＆答え';

        // ステータス更新ボタン
        const statusButton = document.createElement('button');
        statusButton.className = 'status-button';
        statusButton.textContent = 'OK';

        // 削除ボタン
        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-button';
        deleteButton.textContent = '削除';

        listItem.appendChild(questionSpan);
        listItem.appendChild(answerSpan);
        listItem.appendChild(toggleButton);
        listItem.appendChild(statusButton);
        listItem.appendChild(deleteButton);
        wordListElement.appendChild(listItem);
    });
}