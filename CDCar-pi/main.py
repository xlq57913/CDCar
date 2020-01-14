#from action import action
from graphicServer import graphicServer
#from addressServer import addressServer
from actionClient import actionClient
import socket

import _thread
import time

def get_host_ip():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(('8.8.8.8', 80))
        ip = s.getsockname()[0]
    finally:
        s.close()
        
    return ip

if __name__ == "__main__":
    print("device ip is: " + get_host_ip())
    #myAction = action()
    myGraphicServer = graphicServer()
    #myAddressServer = addressServer()
    myActionClient = actionClient()
    myActionClient.start()

    print("init action")
    #_thread.start_new_thread(myActionClient.testClient,("actionClient",))
    #_thread.start_new_thread(myActionClient.action.test,(2,))
    #_thread.start_new_thread(myAddressServer.run,("addressServer",))
    #myAction.test()
    myGraphicServer.start()

    while True:
        time.sleep(5)
    
    #myGraphicServer.run()
     #myGraphicServer.testVideo()
    #myGraphicServer.testServer()
    
