import time
import RPi.GPIO as GPIO
import math

class fan:
    
    high_pin=29
    
    def __init__(self):
        GPIO.setup(self.high_pin,GPIO.OUT)
    
    def run(self):
        GPIO.output(self.high_pin,GPIO.HIGH)
    
    def sleep(self):
        GPIO.output(self.high_pin,GPIO.LOW)
        
class motor:
    
    pin1 = 35 #right
    pin2 = 33 #right
    pin3 = 31 #left
    pin4 = 29 #left
    
    
    
    def __init__(self):
        GPIO.setup(self.pin1,GPIO.OUT)
        GPIO.setup(self.pin2,GPIO.OUT)
        GPIO.setup(self.pin3,GPIO.OUT)
        GPIO.setup(self.pin4,GPIO.OUT)
        
        #GPIO.output(self.pin1,GPIO.LOW)
        #GPIO.output(self.pin2,GPIO.LOW)
        #GPIO.output(self.pin3,GPIO.LOW)
        #GPIO.output(self.pin4,GPIO.LOW)
        
        self.p1 = GPIO.PWM(self.pin1,100)
        self.p2 = GPIO.PWM(self.pin2,100)
        self.p3 = GPIO.PWM(self.pin3,100)
        self.p4 = GPIO.PWM(self.pin4,100)
        
        self.p1.start(0)
        self.p2.start(0)
        self.p3.start(0)
        self.p4.start(0)
        
        
    def stop(self):
        GPIO.output(self.pin1,GPIO.LOW)
        GPIO.output(self.pin2,GPIO.LOW)
        GPIO.output(self.pin3,GPIO.LOW)
        GPIO.output(self.pin4,GPIO.LOW)
        
    def testRun(self):
        self.run()
        time.sleep(10)
        self.stop()
        time.sleep(10)
        
    def run(self,v=0,d=0):
        if(d>=0 and d<=180):
            d=d*math.pi/180
            v_x = int(v*math.sin(d)/(abs(math.sin(d))+abs(math.cos(d))))
            #print("v_x: "+ str(v_x))
            v_y = int(v*math.cos(d)/(abs(math.sin(d))+abs(math.cos(d))))
            #print("v_y: "+str(v_y))
            GPIO.output(self.pin1,GPIO.LOW)
            GPIO.output(self.pin2,GPIO.HIGH)
            GPIO.output(self.pin3,GPIO.HIGH)
            GPIO.output(self.pin4,GPIO.LOW)
            if(v_y>=0):
                if(v_x>v_y):
                    #self.p1.ChangeDutyCycle(v_x-v_y)
                    self.p2.ChangeDutyCycle(v_x-v_y)
                else:
                    #self.p1.ChangeDutyCycle(v_x)
                    self.p2.ChangeDutyCycle(v_x)
                self.p3.ChangeDutyCycle(v_x+v_y)
                #self.p4.ChangeDutyCycle(v_x+v_y)
            elif(v_y<0):
                if(v_x>abs(v_y)):
                    self.p3.ChangeDutyCycle(v_x+v_y)
                    #self.p4.ChangeDutyCycle(v_x+v_y)
                else:
                    self.p3.ChangeDutyCycle(v_x)
                    #self.p4.ChangeDutyCycle(v_x)
                
                self.p1.ChangeDutyCycle(0)
                self.p4.ChangeDutyCycle(0)
                self.p2.ChangeDutyCycle(v_x-v_y)
        elif(d>180 and d<360):
            d=d*math.pi/180
            v_x = abs(v*math.sin(d)/(abs(math.sin(d))+abs(math.cos(d))))
            v_y = v*math.cos(d)/(abs(math.sin(d))+abs(math.cos(d)))
            GPIO.output(self.pin1,GPIO.HIGH)
            GPIO.output(self.pin2,GPIO.LOW)
            GPIO.output(self.pin3,GPIO.LOW)
            GPIO.output(self.pin4,GPIO.HIGH)
            if(v_y>=0):
                if(v_x>v_y):
                    self.p1.ChangeDutyCycle(v_x-v_y)
                    #self.p2.ChangeDutyCycle(v_x-v_y)
                else:
                    self.p1.ChangeDutyCycle(v_x)
                    #self.p2.ChangeDutyCycle(v_x)
                #self.p3.ChangeDutyCycle(v_x+v_y)
                self.p4.ChangeDutyCycle(v_x+v_y)
            elif(v_y<0):
                if(v_x>abs(v_y)):
                    #self.p3.ChangeDutyCycle(v_x+v_y)
                    self.p4.ChangeDutyCycle(v_x+v_y)
                else:
                    #self.p3.ChangeDutyCycle(v_x)
                    self.p4.ChangeDutyCycle(v_x)
                
                self.p1.ChangeDutyCycle(v_x-v_y)
                self.p2.ChangeDutyCycle(0)
                self.p3.ChangeDutyCycle(0)

class arm:

    pin1 = 16
    pin2 = 18

    def __init__(self):
        GPIO.setup(self.pin1,GPIO.OUT)
        GPIO.setup(self.pin2,GPIO.OUT)

        self.p1 = GPIO.PWM(self.pin1,100)
        self.p2 = GPIO.PWM(self.pin2,100)

        self.p1.start(0)
        self.p2.start(0)

    def run(self,dir):
        if(dir>0):
            self.p1.ChangeDutyCycle(40)
            self.p2.ChangeDutyCycle(0)
        elif(dir<0):
            self.p1.ChangeDutyCycle(0)
            self.p2.ChangeDutyCycle(30)
        else:
            self.p1.ChangeDutyCycle(0)
            self.p2.ChangeDutyCycle(0)


class camera:
        
    def __init__(self):
        self.pinUp = 38
        self.pinRight = 36
        GPIO.setup(self.pinUp, GPIO.OUT)
        GPIO.setup(self.pinRight,GPIO.OUT)
        self.pUp = GPIO.PWM(self.pinUp,100)
        self.pRight = GPIO.PWM(self.pinRight,100)
        self.pUp.start(0)
        self.pRight.start(0)
        self.upDegree = 90
        self.rightDegree = 90
        self.turnTo(self.upDegree, self.pUp)
        self.turnTo(self.rightDegree, self.pRight)
        
    def turnTo(self,degree,pwm):
        pwm.ChangeDutyCycle(8.5 + 1.2 * degree / 18.)  # 设置转动角度
        time.sleep(0.2)
        #pwm.ChangeDutyCycle(0)
        #time.sleep(0.2)
        
    def run(self, camUp, camRight):
        self.upDegree = self.upDegree+5*camUp
        self.upDegree = max(0,self.upDegree)
        self.upDegree = min(self.upDegree,180)
        self.rightDegree = self.rightDegree - 5 * camRight
        self.rightDegree = max(0, self.rightDegree)
        self.rightDegree = min(self.rightDegree, 180)
        self.turnTo(self.upDegree,self.pUp)
        self.turnTo(self.rightDegree,self.pRight)

    def test(self):
        for i in range(125,180):
            self.pUp.ChangeDutyCycle(i/10.)
            time.sleep(2)
            self.pRight.ChangeDutyCycle(i/10.)
            time.sleep(2)
            print("i = "+str(i))

class light:

    lightPin = 32
    isOn = False

    def __init__(self):
        GPIO.setup(self.lightPin,GPIO.OUT)
        GPIO.output(self.lightPin,GPIO.LOW)

    def switchOn(self):
            GPIO.output(self.lightPin,GPIO.HIGH)

    def switchOff(self):
            GPIO.output(self.lightPin,GPIO.LOW)
    

class action:
    
    def __init__(self):
        #GPIO.cleanup()
        GPIO.setmode(GPIO.BOARD)
        #self.myFan = fan()
        self.myMotor = motor()
        self.myCamera = camera()
        self.myArm = arm()
        self.myLight = light()
        
    def _del__(self):
        GPIO.cleanup()
    
    def runFan(self):
        self.myFan.run()
    
    def sleepFan(self):
        self.myFan.sleep()
        
    def test(self,i):
        while True:
            self.myMotor.run(100,90)
            

