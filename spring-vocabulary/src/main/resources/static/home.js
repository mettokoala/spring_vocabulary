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
	
	const createButton = document.getElementById('createButton');
	createButton.addEventListener('click', createWord);
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

async function getAllWords(){
	try{
		const response = await fetch('/words');
		if (!response.ok){
			const errorText = await response.text();
			throw new Error(errorText || 'サーバーエラー');
		}
		const data = await response.json();
		createWordsList(data);
	}catch(error){
		console.error(error);
		alert('単語の取得に失敗しました。');
	}
}

async function updateStatus(wordId){
	try{
		const response = await fetch(`/words/${wordId}`,{
			method: 'PUT'
		});
		
		if (!response.ok){
			const errorText = await response.text();
			throw new Error(errorText || 'サーバーエラー');
		}
		removeWordFromList(wordId);
	}catch(error){
		console.error(error);
		alert('ステータスの更新に失敗しました。');
	}
	
}

async function createWord(){
	const question = document.getElementById('question').value.trim();
	const answer = document.getElementById('answer').value.trim();
	
	if(!question || !answer) {
		alert('問題と解答は両方入力してください。');
		return;
	}
	
	try{
		const response = await fetch(`/words`,{
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({ question, answer})
		});
		
		if (!response.ok){
			const errorText = await response.text();
			throw new Error(errorText || 'サーバーエラー');
		}
		document.getElementById('createForm').reset();
		getAllWords();
	}catch(error){
		console.error(error);
		alert('単語の登録に失敗しました。');
	}
	
}

async function deleteWord(wordId) {
	if (!confirm('本当に削除しますか？')) return;
	try{
		const response = await fetch(`/words/${wordId}`,{
			method: 'DELETE'
		});
		
		if (!response.ok){
			const errorText = await response.text();
			throw new Error(errorText || 'サーバーエラー');
		}
		removeWordFromList(wordId);
	}catch(error){
		console.error(error);
		alert('削除に失敗しました。');
	}
}

function createWordsList(words){
	const wordListElement = document.querySelector('ul');
	wordListElement.innerHTML = '';
	
	words.forEach(word => {
		const listItem = document.createElement('li');
		listItem.className = 'word-item';
		listItem.dataset.wordId = word.id;
		
		const toggleButton = document.createElement('button');
		toggleButton.className = 'toggle-text-button';
		toggleButton.textContent = word.question;
		toggleButton.addEventListener('click', () =>{
			toggleButton.textContent = toggleButton.textContent === word.question ? word.answer : word.question;
		})
		
		const statusButton = document.createElement('button');
		statusButton.className = 'status-button';
		statusButton.textContent = 'OK';
		statusButton.addEventListener('click', () => {
			updateStatus(word.id);
		})
		
		const deleteButton = document.createElement('button');
		deleteButton.className = 'delete-button';
		deleteButton.textContent = '削除';
		deleteButton.addEventListener('click', () => {
			deleteWord(word.id);
		})
		
		listItem.appendChild(toggleButton);
		listItem.appendChild(statusButton);
		listItem.appendChild(deleteButton);
		
		wordListElement.appendChild(listItem);
	})
}

function removeWordFromList(id) {
	const wordList = document.querySelector('ul');
	const listItem = wordList.querySelector(`li.word-item[data-word-id="${id}"]`);
	if(listItem) {
		listItem.remove();
	}
}