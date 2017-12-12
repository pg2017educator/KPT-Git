function reload() {
	console.log("reload()")
/*
	const paramsGetKpt = {
		'method' : 'GET',
		'url' : 'http://localhost:8080/api/nobody/items',
		'callback' : setDateList // データを取得し終わったらsetDateList関数を呼び出す
	}

	execute(paramsGetKpt)
*/
}
var obj = {}
var kpts = {}
var user
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

	//kptsが空の場合
	if(!Object.keys(kpts).length){
		document.getElementById('keep').value = '\r'
		document.getElementById('problem').value = '\r'
		document.getElementById('try').value = '\r'
//		document.getElementById('date_selector').value = '\r'
		return;
	}

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

	if(ckDate(date)) {
	  kpts[date] = {
	 	'K':[],
	 	'P':[],
	 	'T':[]
	  }
	} else {
		alert("日付は　yyyy/mm/dd　で入力してください")
		document.getElementById('date').value = '\r'
		return
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

function ckDate(datestr) {
	console.log(datestr)
  if(!datestr.match(/^\d{4}\/\d{1,2}\/\d{1,2}$/)){
   console.log("err1")
   return false;
  }
  var vYear = datestr.substr(0, 4) - 0;
  var vMonth = datestr.substr(5, 2) - 1;
  var vDay = datestr.substr(8, 2) - 0;
  if(vMonth >= 0 && vMonth <= 11 && vDay >= 1 && vDay <= 31){
    var vDt = new Date(vYear, vMonth, vDay);
    if(isNaN(vDt)){
    	 console.log("err2")
      return false;
    }else if(vDt.getFullYear() == vYear && vDt.getMonth() == vMonth && vDt.getDate() == vDay){
      return true;
    }else{
    	 console.log("err3")
      return false;
    }
  } else{
	  console.log("err4")
    return false;
  }
 }

function login() {
	user=getFieldValue('user');

	//初期化
	var obj = {}
	var kpts = {}

	const paramsGetKpt = {
			'method' : 'GET',
			'url' : 'http://localhost:8080/api/'+user+'/items',
			'callback' : setDateList // データを取得し終わったらsetDateList関数を呼び出す
	}

	execute(paramsGetKpt)

}

function del() {
	const date = getFieldValue('date_selector')

	delete kpts[date];

	document.getElementById('date_selector').value = '\r'
	document.getElementById('keep').value = '\r'
	document.getElementById('problem').value = '\r'
	document.getElementById('try').value = '\r'

	const paramsPostKpt = {
		'method' : 'POST',
		'url' : 'http://localhost:8080/api/'+user+'/items',
		'data' : kpts
//		'date' : date
	}
	execute(paramsPostKpt)
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

	if(!ckDate(date)) {
		return;
	}

kpts[date] = {
		'K':[keep],
		'P':[problem],
		'T':[tryed]
	}
	console.log(kpts)


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
	'url' : 'http://localhost:8080/api/'+user+'/items',
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
