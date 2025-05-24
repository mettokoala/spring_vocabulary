document.addEventListener('DOMContentLoaded', () =>{
	const fileInput = document.getElementById('fileInput');
	const importCsvButton = document.getElementById('importCsvButton');
	
	importCsvButton.addEventListener('click', () =>{
		const file = fileInput.files[0];
		if (!file){
			alert('ファイルが選択されていません');
			return
		}
		const fileName = file.name;
		if(!fileName.toLowerCase().endWith('.csv')){
			alert('CSVファイルを選択してください');
			return
		}
		
	})
})