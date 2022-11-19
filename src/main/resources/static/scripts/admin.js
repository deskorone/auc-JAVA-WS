console.log("HELLO ADMIN! :)")


let closeAdminLot = (id) => {
    let button = document.getElementById('buttonc' + id);
    button.disabled = true;
    request("post", `http://localhost:8080/api/close/lot/${id}`)
        .then(e=>{
            if (e.status === 200){
                console.log("CLOSING")
            }
    })
}

let deleteLot = (id) => {
    console.log(id)
    let button = document.getElementById('button' + id);
    button.disabled = true;
    request("delete", `http://localhost:8080/api/delete/lot/${id}`)
        .then(e=>{
            if (e.status === 200){
                console.log("DELETE")
            }
        })
}
