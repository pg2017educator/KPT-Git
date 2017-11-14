function reload() {
	console.log("reload()")
	const paramsGetKpt = {
		'method' : 'GET',
		'url' : 'http://localhost:8080/api/nobody/items',
		'callback' : setDateList // データを取得し終わったらsetDateList関数を呼び出す
	}

	execute(paramsGetKpt)
}

var kpts = {}
function setDateList(resText) {
	console.log(resText)

	kpts = JSON.parse(resText)
	const dateSelector = document.getElementById('date_selector')

	for ( let date in kpts) {
		let kpt = kpts[date]
		console.log(kpt)

		const option = document.createElement('option')
		option.setAttribute("value", date)
		option.innerText = date
		dateSelector.appendChild(option)
	}

	change()
}

function change() {
	const dateSelector = document.getElementById('date_selector')
	const kpt = kpts[dateSelector.value]
	console.log(kpt)

	document.getElementById('keep').value = kpt.K.join('\r')
	document.getElementById('problem').value = kpt.P.join('\r')
	document.getElementById('try').value = kpt.T.join('\r')
}

function getFieldValue(id) {
	return document.getElementById(id).value
}

function clearFieldValue(id) {
	return document.getElementById(id).value = ''
}

function add() {
	const date = getFieldValue('date')

	kpts[date] = {
		'K':[],
		'P':[],
		'T':[]
	}

	const dateSelector = document.getElementById('date_selector')

	const option = document.createElement('option')
	option.setAttribute("value", date)
	option.innerText = date
	dateSelector.appendChild(option)

	dateSelector.selectedIndex = dateSelector.length - 1
	change()

	clearFieldValue('date')
}

function save() {
	const date = getFieldValue('date')
	console.log(date)
}

function execute(params) {
	const xhr = new XMLHttpRequest()
	xhr.onreadystatechange = function() {
		switch (xhr.readyState) {
		case 0: // 未初期化状態.
			console.log('uninitialized!')
			break
		case 1: // データ送信中
			console.log('loading...')
			break
		case 2: // 応答待ち.
			console.log('loaded.')
			break
		case 3: // データ受信中
			console.log(`interactive... ${ + xhr.responseText.length } bytes.`)
			break
		case 4: // データ受信完了.
			if (xhr.status == 200 || xhr.status == 201 || xhr.status == 304) {
				console.log(xhr.getAllResponseHeaders())
				if (params.callback) {
					params.callback(xhr.responseText)
				}
			} else {
				console.log(`Failed. HttpStatus: (${xhr.status})${xhr.statusText}`)
			}
			break
		}
	}

	// HTTPでアクセスする
	console.log(params)
	xhr.open(params.method, params.url, true)
	for (let name in params.headers) {
		xhr.setRequestHeader(name, params.headers[name])
	}
	if (params.data) {
		xhr.send(JSON.stringify(params.data))
	} else {
		xhr.send(null)
	}

}
