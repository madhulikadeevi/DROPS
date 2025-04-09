
from tkinter import *
import tkinter
import socket 
from threading import Thread 
from socketserver import ThreadingMixIn
import os
import pickle
import sys

mainGUI = tkinter.Tk()
mainGUI.title("Cloud Server1") #designing main screen
mainGUI.geometry("900x700")
running = True

def startDistributedCore():
    class CoreThread(Thread): 
 
        def __init__(self,ip,port): 
            Thread.__init__(self) 
            self.ip = ip 
            self.port = port 
            text.insert(END,'Request received from Client IP : '+ip+' with port no : '+str(port)+"\n") 
 
        def run(self): 
            data = conn.recv(9900000)
            dataset = pickle.loads(data)
            request_type = dataset[0]
            if request_type == "filenames":
                user = dataset[1]
                lists = os.listdir('users/'+user)
                names = []
                for i in range(len(lists)):
                    if user not in lists[i]:
                        names.append(lists[i])
                print(names)
                features = pickle.dumps(names)
                conn.send(features)        
            if request_type == 'readblocks':
                username = dataset[1]
                filename = dataset[2]
                blocks = {}
                features = []
                for root, dirs, directory in os.walk('users/'+username+'/'+filename):
                    for j in range(len(directory)):
                        fileobj  = open('users/'+username+'/'+filename+'/'+directory[j], 'rb')
                        content = fileobj.read()
                        fileobj.close()
                        blocks[directory[j]] = content
                features.append(blocks)
                features = pickle.dumps(features)
                conn.send(features)
                text.insert(END,"All blocks from file sent to user "+str(list(blocks.keys()))+"\n")
                        
            if request_type == 'blocks':
                username = dataset[1]
                filename = dataset[2]
                blocks = dataset[3]
                names = dataset[4]
                if os.path.exists('users/'+username+'/'+filename) == False:
                    os.mkdir('users/'+username+'/'+filename)
                fileobj  = open('users/'+username+'/'+filename+'/'+str(names), 'wb')
                fileobj.write(blocks)
                fileobj.close()
                conn.send("Blocks saved".encode())
                text.insert(END,"Received blocks = "+str(blocks)+"\n")
            if request_type == 'signup':
                username = dataset[1]
                password = dataset[2]
                confirm = dataset[3]
                if os.path.exists('users/'+username) == False:
                    os.mkdir('users/'+username)
                    f = open('users/'+username+'/'+username+'.txt', "w")
                    f.write(password)
                    f.close()
                    conn.send("Signup process completed".encode())
                    text.insert(END,"Signup process completed\n")
                else:
                    conn.send("Username already exists".encode())
                    text.insert(END,username+" Username already exists\n")
            if request_type == 'login':
                username = dataset[1]
                password = dataset[2]
                status = 'invalid login'
                if os.path.exists('users/'+username) == True:
                    with open('users/'+username+'/'+username+'.txt', "r") as file:
                        for line in file:
                            line = line.strip('\n')
                            line = line.strip()
                            if line == password:
                                status = "success"
                                text.insert(END,"Login successful\n")
                                break
                    file.close()
                conn.send(status.encode())
                if status == 'none':
                    text.insert(END,status+"\n")
                    
                
            
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) 
    server.bind(('localhost', 2222))
    threads = []
    text.insert(END,"Cloud Server1 waiting for incoming connections\n\n")
    while running:
        server.listen(4)
        (conn, (ip,port)) = server.accept()
        newthread = CoreThread(ip,port) 
        newthread.start() 
        threads.append(newthread) 
    for t in threads:
        t.join()

def startCore():
    Thread(target=startDistributedCore).start()


gfont = ('times', 16, 'bold')
gtitle = Label(mainGUI, text='Cloud Server1')
gtitle.config(bg='LightGoldenrod1', fg='medium orchid')  
gtitle.config(font=gfont)           
gtitle.config(height=3, width=120)       
gtitle.place(x=0,y=5)

gfont1 = ('times', 12, 'bold')

text=Text(mainGUI,height=28,width=130)
scroll=Scrollbar(text)
text.configure(yscrollcommand=scroll.set)
text.place(x=10,y=100)
text.config(font=gfont1)

startCore()

mainGUI.config(bg='OliveDrab2')
mainGUI.mainloop()


