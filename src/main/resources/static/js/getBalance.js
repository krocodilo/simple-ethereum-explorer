

function getBalanceAtDate(){

    try {
        const date = document.getElementById("dateInput").value;

        if( ! date ) {
            throw new Error( "You must select a date first" );
        }

        document.getElementById("apiBalanceResponse").innerHTML = "";
        document.getElementById("Balancespinner").style.display = "inline-block";

        apiCallPOST( "/balance/" + thisAddress + "/" + date)
            .then((data) => {
                // Show balanceº
                console.log("Get Balance - " + data.resp)

                document.getElementById("Balancespinner").style.display = "none";
                document.getElementById("apiBalanceResponse").innerHTML = data.resp;
            })

    } catch(e) {
        showAlert(e);
    }
}

async function apiCallPOST( url ) {

    const resp = await fetch(url)

    if( ! resp.ok ) {
        console.log(resp.status)
        throw new Error("There was an error in the communication with the API.");
    }

    return resp.json();
}

function showAlert(e) {
    alert( e );
    console.log( e );
}