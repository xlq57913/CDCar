import socket
import PIL.Image
import base64
from proto.python.message_image_pb2 import *
import matplotlib.pyplot as plt

address = ('172.18.27.204', 23223)
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(address)

s.send("hihi".encode('utf-8'))

data = s.recv(1024)
msgLen = data.decode("utf-8")
print(data)

s.send('hihi'.encode('utf-8'))

data = s.recv(1024000)

print(len(data))
print(data)

imageMsg = message_image()
imageMsg.ParseFromString(data)

img = PIL.Image.open(imageMsg.image_data)

plt.imshow(img)

s.close()
