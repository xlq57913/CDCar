import socket
import time
class addressServer:
    
    def __init__(self):
        
        self.senderAddress = ('0.0.0.0',1503)
        self.address = ('224.5.23.2', 23333)
        self.mySocket = socket.socket(socket.AF_INET,socket.SOCK_DGRAM,socket.IPPROTO_UDP)
        self.mySocket.bind(self.senderAddress)
        self.mySocket.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 255)
        self.hostName = socket.gethostname()
        self.hostAddress = socket.gethostbyname(self.hostName)
        
    
    def run(self,i):
        while 1:
            self.mySocket.sendto(self.hostAddress.encode('utf-8'),self.address)
            
            time.sleep(0.1)
            
            
        
        
