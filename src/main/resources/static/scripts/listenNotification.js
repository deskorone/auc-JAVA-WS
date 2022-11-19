let requestGet = (url)=>{
    return fetch(url).then(response => response.json());
}

let notificationStomp = null;

let connectFun = (arr) =>{

    var socket = new SockJS('/ws');
    notificationStomp = Stomp.over(socket);
    notificationStomp.connect({}, function (frame) {
        console.log('Connected: ');
        arr.forEach(e=>{
            notificationStomp.subscribe('/lotpage/'+ e + '/private', generateMessage);
        })
        // notificationStomp.debug = false;
    });
}

requestGet(URL + '/subs').then(e=>{
    connectFun(e);
})



const notification = document.querySelector('.notification');

function dismissMessage() {
    notification.classList.remove('adder');
}

function showMessage() {
    notification.classList.add('adder');
    const button = document.getElementById("closeBut")
    button.addEventListener('click', dismissMessage, { once: true });
}


let  i = 0;
function generateMessage(payload) {
        const message = document.querySelector('.notification');
        let body = JSON.parse(payload.body);
        console.log(body)
        console.log(body)
        message.querySelector('p').textContent = `С лотом ${body.name} идут торги цена(${body.value})`;
        message.className = `notification`;
        showMessage();
}

