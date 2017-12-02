function reload() {
	console.log("reload()")
	const paramsGetKpt = {
		'method' : 'GET',
		'url' : 'http://localhost:8080/api/nobody/items',
		'callback' : setDateList // データを取得し終わったらsetDateList関数を呼び出す
	}

	execute(paramsGetKpt)
}
var obj = {}
var kpts = {}
function setDateList(resText) {
	console.log(resText)

	obj=resText
	console.log(obj)
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
	console.log('保存ボタンが押されました')
	const date = getFieldValue('date_selector')
	console.log(date)
	const keep = getFieldValue('keep')
	console.log(keep)
	const problem = getFieldValue('problem')
	console.log(problem)
	const tryed = getFieldValue('try')
	
	console.log(tryed)	
	
//	var objorg = JSON.parse(obj)
kpts[date] = {
		'K':[keep],
		'P':[problem],
		'T':[tryed]
	}
	console.log(kpts)
	
	//var ooo = '{'+date+': { "K": [+'keep'+]}}
	//console.log(ooo)



/*var obj = {

 "2017/12/13": {
 "K": [
"良いことがあった",
"これから続けたい"
],
"P": ["不満がある"],
"T": ["がんばる"]
},
 "2017/12/14": {
"K": [
 "良いことがあった",
"これは続ける。"
],
 "P": [
 "これは問題"
  ],
 "T": [
  "次はがんばる"
 ]
 },
 "2017/12/15": {
 "K": [
  "はじめてのKPT"
  ],
 "P": [],
 "T": [
 "トライする"
]
}
} */


	
	const paramsPostKpt = {
	'method' : 'POST',
	'url' : 'http://localhost:8080/api/nobody/items',
	'data' : kpts,
	'date' : date
	}
//	console.log(paramsPostKpt)
	execute(paramsPostKpt)
}

function execute(params) {
	console.log(params.data)
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
				console.log(obj)
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
	//	xhr.setRequestHeader(name, params.headers[name])
	//	xhr.setRequestHeader(name, application/json)
		console.log("twetwtwetwtwt")
		cosole.log(params.headers[name])
	}
//	if (params.data) {
	if (params.method == "POST") {
		console.log("if")
		xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded')
		xhr.send(JSON.stringify(params.data))
	} else {
		console.log("else")
		xhr.send(null)
	}

}
