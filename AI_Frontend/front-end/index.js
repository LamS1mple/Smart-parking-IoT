
const videoElement = document.querySelector("#localVideo");
const canvas = document.querySelector("#canvas");
const context = canvas.getContext("2d");

const xe = document.querySelectorAll(".xe")

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

async function getData(url ){
  const response = await fetch(url)
  return response.json()
}

navigator.mediaDevices
  .getUserMedia({ video: true })
  .then((stream) => {
    videoElement.srcObject = stream;

    setInterval(captureAndSendFrame, 10000); // Cứ sau 100ms cắt và gửi frame
  })
  .catch((error) => {
    console.error("Không thể truy cập webcam: " + error);
  });

async function captureAndSendFrame() {
  // Đặt kích thước canvas để phù hợp với video
  canvas.width = videoElement.videoWidth;
  canvas.height = videoElement.videoHeight;

  // Vẽ frame hiện tại từ video lên canvas
  context.drawImage(videoElement, 0, 0, canvas.width, canvas.height);

  // Trích xuất dữ liệu hình ảnh từ canvas dưới dạng base64
  const frameDataUrl = canvas.toDataURL("image/jpg");
  const frame = frameDataUrl.split(',')[1]
  // console.log(frame, frameDataUrl)

  const data = await postData("http://127.0.0.1:5000", { img: frame });
  console.log(data)
  if (data.name != ''){
    
    document.querySelector("#text").innerHTML = "Biển số xe: "  + data.name
    const post = await postData("http://localhost:8080/", data)

  }
}

function check(data){
  ["ba","bon","nam","sau"].forEach((element, index) => {
    if(  data?.[element] == 1){
      xe[index].setAttribute("src",`${index + 1}.png`) 
    }
    else{
      xe[index].setAttribute("src","OIP.png")  
    }
  });
}
async function getTrangThai(){
  const data = await getData("http://localhost:8080/")
  console.log(data)
  check(data)
 
  setTimeout(getTrangThai , 1000);
}
getTrangThai()


document.querySelector("#dong").addEventListener("click",async function(e){
  let data =  await postData("http://localhost:8080/dong-mo" ,{trangThai: "Dong"})
  console.log(data)

  
})
document.querySelector("#mo").addEventListener("click",async function(e){
  let data =  await postData("http://localhost:8080/dong-mo" ,{trangThai: "Mo"})
  console.log(data)

})