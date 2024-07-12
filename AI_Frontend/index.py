import json
from flask import Flask, jsonify, request
import cv2
import easyocr
import base64
from flask_cors import CORS, cross_origin
from ultralytics import YOLO

import numpy as np
def nhan_dien_anh(resize):
    img = resize
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    reader = easyocr.Reader(['en'])
    result = reader.readtext(gray)
    text = ''
    for x in result:
        text = text + " " + x[1].strip()
    text = text.strip()
    return text

app = Flask(__name__)
model = YOLO("runs/detect/train/weights/best.pt")

CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'


@app.route("/", methods=["POST"])
@cross_origin()
def hello():
    global re
    re = json.loads(request.data)
    img = base64.b64decode(re['img'])
    np_data = np.fromstring(img,np.uint8)
    img = cv2.imdecode(np_data,cv2.IMREAD_UNCHANGED)
    
    bienSoXe = nhan_dien_anh(img)
    bienSoXe = bienSoXe.upper()
    x = ""
    for i in bienSoXe:
        if i <= '9' and i >= '0' or i <= 'Z' and i >= 'A':
            x += i 
    # print(bienSoXe)
    return jsonify({"name": x})



if __name__ == '__main__':

   app.run()
