
const URL = "http://localhost:8080/api"

let request = (method,url, body) =>{
    const headers ={
        'Content-Type': 'application/json'
    }
    return fetch(url, {
        method: method,
        body: JSON.stringify(body),
        headers: headers
    })
        .catch(err=>{
            throw  err;
        });
}





let sub = (id) => {

    let button = document.getElementById('button' + id);
    button.disabled = true;
    request( 'POST', URL + `/sub/add?id=${id}`, {}).then((r) =>{
        if(r.status === 200){
            button.classList.add('unsub');
            button.classList.remove('sub')
            button.innerHTML = 'Отписаться'
            button.disabled = false;
            button.onclick = ()=>{ unsub(id)};
            if(notificationStomp){
                notificationStomp.subscribe('/lotpage/'+ id + '/private', generateMessage);
            }
        }
    })


}

let unsub = (id) => {
    let button = document.getElementById('button' + id);
    button.disabled = true;
    request( 'PUT', URL + `/sub/add?id=${id}`, {}).then((r) =>{
        if(r.status === 200){
            button.classList.add('sub');
            button.classList.remove('unsub')
            button.innerHTML = 'Подписаться'
            button.disabled = false;
            button.onclick = ()=>{sub(id)}
            if(notificationStomp){55
                notificationStomp.unsubscribe('/lotpage/'+ id + '/private');
            }

        }

    })
}


