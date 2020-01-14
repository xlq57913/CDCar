from proto.python.message_image_pb2 import *
import cv2
import socket
import numpy as np
import time
import os
import sys
import _thread
import base64

class graphicServer:
    
    def __init__(self):
        
        self.address = ('0.0.0.0', 23223)
        self.mySocket = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        self.mySocket.bind(self.address)
        #self.mySocket.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 255)
        self.imageMessage = message_image()
        self.cap = cv2.VideoCapture(0)

    def start(self):
        _thread.start_new_thread(self.run,(1,))
    
    def run(self,i):
        
        self.mySocket.listen(5)
        
        while True:
            
            appSocket, addr = self.mySocket.accept()
            
            data = appSocket.recv(1024)
            #print("recv step 1")
            
            #print("server accept:"+str(addr))
            try:
                self.sendImage(appSocket)
            except:
                #print("something happen when sendImage")
                #print("Unexpected error:"+str(sys.exc_info()[1]))
                pass

            
    def testServer(self):
        for i in range(100):
            self.mySocket.sendto(('testServer:'+str(i)).encode('utf-8'),self.address)
            time.sleep(0.1)
            
    def testVideo(self):
        for i in range(10):
            ret, frame = self.cap.read()
            cv2.imshow("testVideo"+str(i), frame)
            cv2.waitKey(0)
            cv2.destroyAllWindows()
            print(frame.shape)
            
    def sendImage(self,appSocket):
        while True:
            
            #data = appSocket.recv(1024)
            #print("recv step 1")
            
            ret, frame = self.cap.read()

            frame = cv2.resize(frame, None, fx=0.3, fy=0.3, interpolation=cv2.INTER_AREA)
            
            #imgDir = cvWriteTemp(frame)

            #send_data = file_deal(img_encode)

            height = frame.shape[0]
            width = frame.shape[1]
            self.imageMessage.width = width
            self.imageMessage.height = height
            img_encode = cv2.imencode('.jpg',frame)[1]
            #data_encode = np.float(frame)

            data_encode = np.array(img_encode)
            #byte_encode = base64.b64encode(img_encode)
            byte_encode = data_encode.tobytes()
            self.imageMessage.image_data = byte_encode
                        
            imageData = self.imageMessage.SerializeToString()
            #imageData = byte_encode
            #imageData = send_data

            length = len(imageData)
            strLength = str(length)
            if(len(strLength)==4):
                strLength = "0"+strLength

            #print("data size: " + strLength)
            
            appSocket.send(strLength.encode('utf-8'))
            #print("send image len")
            
            data = appSocket.recv(1024)
            #print("recv step 2")

            leftLen = length
            while(leftLen>0):
                if(leftLen>2400):
                    appSocket.send(imageData[length-leftLen:length-leftLen+2400])
                    leftLen=leftLen-2400
                else:
                    appSocket.send(imageData[length-leftLen:])
                    leftLen=0
                data = appSocket.recv(1024)

            #print("send image data")
            
            data = appSocket.recv(1024)
            #print("recv step 1")

