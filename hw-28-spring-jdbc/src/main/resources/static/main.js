function postNewClient() {
    let name = document.getElementById('name-input').value
    fetch("/api/create-client", {
        method: 'POST',
          headers: {
            'Content-Type': 'text/plain;charset=utf-8'
          },
        body: name
    }).then(resp => window.location.reload())
}
