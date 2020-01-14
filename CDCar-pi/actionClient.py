from proto.python.command_pb2 import *

import socket
from action import action
import _thread
class actionClient:
    
    def __init__(self):
        self.address = ("0.0.0.0",20005)
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
        self.sock.bind(self.address)
        self.actionCommand = command()
        self.action = action()

        
    def recvAction(self):
        recv_data = self.sock.recvfrom(50020)
        if(not recv_data):
            return False
        recv_msg = recv_data[0]
        send_addr = recv_data[1]
        
        self.actionCommand.ParseFromString(recv_msg)
        return True

    def start(self):
        _thread.start_new_thread(self.testClient,(1,))
    
    def run(self):
        
        if(self.recvAction()):
            pass
        
    def testClient(self,i):
        while True:
            #print("start recv...")
            if(self.recvAction()):
                #print("command received:")
                #print("carCommand:",(self.actionCommand.car.velocity,self.actionCommand.car.degree))
                self.action.myMotor.run(self.actionCommand.car.velocity,self.actionCommand.car.degree)
                #print("cameraCommand:",(self.actionCommand.camera.up,self.actionCommand.camera.right))
                if(self.actionCommand.camera.up!=0 and self.actionCommand.camera.right!=0):
                    self.action.myCamera.run(self.actionCommand.camera.up,self.actionCommand.camera.right)
                #print("armCommand:",(self.actionCommand.arm.goUp,self.actionCommand.arm.goDown))
                if(self.actionCommand.arm.goUp):
                    self.action.myArm.run(1)
                elif(self.actionCommand.arm.goDown):
                    self.action.myArm.run(-1)
                else:
                    self.action.myArm.run(0)

                if(self.actionCommand.light.on):
                    self.action.myLight.switchOn()
                else:
                    self.action.myLight.switchOff()
        