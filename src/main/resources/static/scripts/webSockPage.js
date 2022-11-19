let stompClient = null;

const URL = "http://localhost:8080/api"

let price = document.getElementById("price");
let btn = document.getElementById("btn");
let inp = document.getElementById("inp");

let url = window.location.href.split("/");
let id = url[url.length - 1];


let request = (method, url, body) => {
    const headers = {
        'Content-Type': 'application/json'
    }
    return fetch(url, {
        method: method,
        body: JSON.stringify(body),
        headers: headers
    })
        .catch(err => {
            throw  err;
        });
}


let connectFun = () => {

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ');

        stompClient.subscribe('/lotpage/' + id + '/private', getChangePrice);

    });
}


let sendPrice = (userId) => {
    stompClient.send("/app/sub-lot", {}, JSON.stringify({
        productId: id,
        name: null,
        value: inp.value,
        status: "CHANGE_PRICE",
        userId: userId,
        time: null,
        username: null,
    }))
}


let historyPane = document.getElementById("history-cells");

let commentsPane = document.getElementById("comm-cells");

let getChangePrice = (payload) => {
    let body = JSON.parse(payload.body);
    if (body.status != 'ERROR') {
        price.innerHTML = body.value;
        let cell = document.createElement("div")
        cell.classList.add('com');
        let comUser = document.createElement("div")
        comUser.classList.add("com-user")
        console.log(body.username)
        comUser.innerText = body.username
        cell.appendChild(comUser);
        let comText = document.createElement("div")
        comText.classList.add("com-text");
        comUser.textContent = "Поднял цену до " + body.value;
        cell.appendChild(comText);
        historyPane.appendChild(cell);
    }

}

let commInput = document.getElementById("comm-inp")

let sendComment = (lotId) => {
    console.log(commInput.value)
    if(commInput.value) {
        request("POST", URL + `/lot/comment/${lotId}`, {text: commInput.value})
            .then((r) => {
                if (r.status === 200) {
                    r.json().then(e=>{
                        let cell = document.createElement("div")
                        cell.classList.add('com');
                        let comUser = document.createElement("div")
                        comUser.classList.add("com-user")
                        comUser.textContent = e.name + ':'
                        cell.appendChild(comUser);
                        let comText = document.createElement("div")
                        comText.classList.add("com-text");
                        comUser.textContent = e.text;
                        cell.appendChild(comText);
                        commentsPane.appendChild(cell);
                    })

                }
            })
    }
}


connectFun();