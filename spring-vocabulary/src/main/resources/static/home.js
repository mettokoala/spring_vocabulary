document.addEventListener('DOMContentLoaded', () =>{
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