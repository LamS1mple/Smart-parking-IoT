



// Tro ly ao
var SpeechRecognition = SpeechRecognition || webkitSpeechRecognition;

const recognition = new SpeechRecognition();
const synth = window.speechSynthesis;
recognition.lang = 'vi-VI';
recognition.continuous = false;

const microphone = document.querySelector('.microphone');

async function postData(url = "", data = {}) {
    // Default options are marked with *
    const response = await fetch(url, {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      mode: "cors", // no-cors, *cors, same-origin
      cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
      credentials: "same-origin", // include, *same-origin, omit
      headers: {
        "Content-Type": "application/json",
      },
      redirect: "follow", // manual, *follow, error
      referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
      body: JSON.stringify(data), // body data type must match "Content-Type" header
    });
    return response.json(); // parses JSON response into native JavaScript objects
  }

const giongNoi = (data) =>{
    if (data == 0) return ;
    var audio = new Audio(`audio/${data}.mp3`  );
    console.log(data)
    audio.play();
}

const xuLy =async (text) =>{
    const xu = text.trim().split(" ")
    let vanBan = ""
    xu.forEach(element => {
        vanBan += element;
    });
    console.log(vanBan)
    const data = await postData("http://localhost:8080/timkiem",{name:vanBan})
    giongNoi(data.viTri)
}

microphone.addEventListener('click', (e) => {
    e.preventDefault();

    recognition.start();
    microphone.classList.add('recording');
});

recognition.onspeechend = () => {
    recognition.stop();
    microphone.classList.remove('recording');
}

recognition.onerror = (err) => {
    console.error(err);
    microphone.classList.remove('recording');
}

recognition.onresult = (e) => {
    console.log('onresult', e);
    const text = e.results[0][0].transcript;
    xuLy(text)
}


async function getTrangTha(){
    const data = await getData("http://localhost:8080/timkiem")
    giongNoi(data.viTri)
   
    setTimeout(getTrangTha , 3000);
  }
  getTrangTha()


