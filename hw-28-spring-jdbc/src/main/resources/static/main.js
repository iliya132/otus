function postNewClient() {
    let name = document.getElementById('name-input').value;
    let address = document.getElementById('address-input').value;
    let phone = document.getElementById('phones-input').value;
    let data = {
        name: name,
        address: address,
        phone: phone
    };
    fetch('/api/create-client', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(resp => window.location.reload());
}
