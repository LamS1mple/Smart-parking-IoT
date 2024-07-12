import cv2
from matplotlib import pyplot as plt
import numpy as np
import easyocr
from ultralytics import YOLO
from PIL import Image
def nhan_dien_anh(resize):
    img = resize
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    reader = easyocr.Reader(['en'])
    result = reader.readtext(gray)
    text = ''
    for x in result:
        text = text + " " + x[1].strip()
    text = text.strip()
    print(text)

model = YOLO("runs/detect/train/weights/best.pt")
image = cv2.imread("img2.jpg")
results = model(image)

r = results[0]
# im_array = r.plot()
# print(r.boxes.xyxy)
x_top, y_top, x_bottom, y_bottom = r.boxes.xyxy[0]
x_top, y_top, x_bottom, y_bottom = int(x_top), int(y_top), int(x_bottom), int(y_bottom)

# im = Image.fromarray(im_array)
cropped_im = image[y_top:y_bottom, x_top:x_bottom]
nhan_dien_anh(cropped_im)
    

  
  
