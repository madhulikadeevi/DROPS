from flask import Flask, render_template, request, redirect, url_for, session
import socket
import os
import pickle
import matplotlib.pyplot as plt
import numpy as np

app = Flask(__name__)

app.secret_key = 'welcome'
global uname
global size, total_blocks
global file_blocks, names
global fname

@app.route('/index', methods=['GET', 'POST'])
def index():
    return render_template('index.html', msg='')


@app.route('/Login', methods=['GET', 'POST'])
def Login():
   return render_template('Login.html', msg='')

@app.route('/Signup', methods=['GET', 'POST'])
def Signup():
    return render_template('Signup.html', msg='')

@app.route('/UploadFile', methods=['GET', 'POST'])
def UploadFile():
    return render_template('UploadFile.html', msg='')

def calculateBlock(file):
    size = 0
    tot_blocks = 0
    file_size = os.path.getsize(file)
    if file_size >= 1000:
        size = int(file_size / 10)
        tot_blocks = 10
    if file_size < 1000 and file_size > 500:
        size = int(file_size / 5)
        tot_blocks = 5
    if file_size < 500 and file_size > 1:
        size = int(file_size / 3)
        tot_blocks = 3
    return size, tot_blocks    
        

def blocks(fromfile, total_blocks, size):
    file_blocks = []
    names = []
    input = open(fromfile, 'rb')                   
    for i in range(0,total_blocks):
        chunk = input.read(size)              
        file_blocks.append(chunk)
        names.append(i)
    input.close()
    return file_blocks, names

@app.route('/Graph', methods=['GET', 'POST'])
def Graph():
    if request.method == 'GET':
        height = [240,80]
        bars = ('Greedy Time','Drops Save Time')
        y_pos = np.arange(len(bars))
        plt.bar(y_pos, height)
        plt.xticks(y_pos, bars)
        plt.title("Replication Time Saved in Milli Seconds")
        plt.savefig('static/graph.png')
        return render_template('Graph.html', msg="Comparison Graph") 

@app.route('/DownloadFragments', methods=['GET', 'POST'])
def DownloadFragments():
    global uname
    if request.method == 'GET':
        output = '<tr><td><font size="3" color="black">Choose&nbsp;File</td><td><select name="t1">'
        features = []
        features.append('filenames')
        features.append(uname)
        features = pickle.dumps(features)
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
        client.connect(('localhost', 2222))
        client.send(features)
        data = client.recv(10000)
        data = pickle.loads(data)
        print("====="+str(data))
        for i in range(len(data)):
            print(data[i])
            output+="<option value="+data[i]+">"+data[i]+"</option>"
        return render_template('DownloadFragments.html', msg1=output)                
    

@app.route('/DownloadFragmentsAction', methods=['GET', 'POST'])
def DownloadFragmentsAction():
    if request.method == 'POST':
        global uname
        file = request.form['t1']
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
        client.connect(('localhost', 2222))
        features = []
        features.append('readblocks')
        features.append(uname)
        features.append(file)
        features = pickle.dumps(features)
        client.send(features)
        data = client.recv(9900000)
        data = pickle.loads(data)
        block1 = data[0]

        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
        client.connect(('localhost', 3333))
        features = []
        features.append('readblocks')
        features.append(uname)
        features.append(file)
        features = pickle.dumps(features)
        client.send(features)
        data = client.recv(9900000)
        data = pickle.loads(data)
        block2 = data[0]

        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
        client.connect(('localhost', 4444))
        features = []
        features.append('readblocks')
        features.append(uname)
        features.append(file)
        features = pickle.dumps(features)
        client.send(features)
        data = client.recv(9900000)
        data = pickle.loads(data)
        block3 = data[0]

        names = []
        for key, values in block1.items():
            names.append(int(key))
        for key, values in block2.items():
            names.append(int(key))
        for key, values in block3.items():
            names.append(int(key))
        names.sort()
        file_path = os.path.join("C:\\Users\\deevi\\OneDrive\\Desktop\\DROPS", file)                    
        fileobj  = open(file_path, 'wb+')
        for i in range(len(names)):
            if str(names[i]) in block1.keys():
                content = block1.get(str(names[i]))
                fileobj.write(content)
            if str(names[i]) in block2.keys():
                content = block2.get(str(names[i]))
                fileobj.write(content)
            if str(names[i]) in block3.keys():
                content = block3.get(str(names[i]))
                fileobj.write(content)    
        fileobj.close()
        return render_template('UserScreen.html', msg="File downloaded in C directory")

@app.route('/ReplicateFragments', methods=['GET', 'POST'])
def ReplicateFragments():
    global uname
    global fname
    global size, total_blocks
    global file_blocks, names
    if request.method == 'GET':
        port = 2222
        font = "<font size='3' color='black'>" 
        output = "<table border=1><tr><th>"+font+"File Name</th><th>"+font+"Block Name</th><th>"+font+"Save Location</th></tr>"
        for i in range(len(file_blocks)):
            client = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
            client.connect(('localhost', port))
            features = []
            features.append('blocks')
            features.append(uname)
            features.append(fname)
            features.append(file_blocks[i])
            features.append(names[i])
            features = pickle.dumps(features)
            client.send(features)
            data = client.recv(1000)
            data = data.decode()
            print(data)
            if port == 2222:
                port = 3333
                output+="<tr><td>"+font+fname+"</td><td>"+font+str(names[i])+"</td><td>"+font+"Saved at Cloud1</td></tr>"
            elif port == 3333:
                port = 4444
                output+="<tr><td>"+font+fname+"</td><td>"+font+str(names[i])+"</td><td>"+font+"Saved at Cloud2</td></tr>"
            elif port == 4444:
                port = 2222
                output+="<tr><td>"+font+fname+"</td><td>"+font+str(names[i])+"</td><td>"+font+"Saved at Cloud3</td></tr>"
        return render_template('ViewFragments.html', msg=output)                

@app.route('/UploadFileAction', methods=['GET', 'POST'])
def UploadFileAction():
    global uname
    global fname
    global size, total_blocks
    global file_blocks, names
    if request.method == 'POST':
        data = request.files['t1']
        fname = data.filename
        out_file = open(fname, "wb")
        out_file.write(data.read())
        out_file.close()
        size, total_blocks = calculateBlock(fname)
        file_blocks, names = blocks(fname, total_blocks, size)
        return render_template('UploadFile.html', msg=str(fname)+" File loaded. Total Fragments Generated: "+str(total_blocks)+"\n")

@app.route('/LoginAction', methods=['GET', 'POST'])
def LoginAction():
    if request.method == 'POST':
        global uname
        user = request.form['t1']
        password = request.form['t2']
        print(user)
        client = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
        client.connect(('localhost', 2222))
        features = []
        features.append('login')
        features.append(user)
        features.append(password)
        features = pickle.dumps(features)
        client.send(features)
        data = client.recv(1000)
        data = data.decode()
        if data == "success":
            uname = user
            return render_template('UserScreen.html', msg="Welcome "+uname)
        else:
            return render_template('Login.html', msg="Invalid login details")

def signup(user, password, phone, email, address, gender, port):
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
    client.connect(('localhost', port))
    features = []
    features.append('signup')
    features.append(user)
    features.append(password)
    features.append(password)
    features = pickle.dumps(features)
    client.send(features)
    data = client.recv(1000)
    data = data.decode()
    return data   


@app.route('/SignupAction', methods=['GET', 'POST'])
def SignupAction():
    if request.method == 'POST':
        user = request.form['t1']
        password = request.form['t2']
        phone = request.form['t3']
        email = request.form['t4']
        address = request.form['t5']
        gender = request.form['t6']
        data = signup(user, password, phone, email, address, gender, 2222)
        data = signup(user, password, phone, email, address, gender, 3333)
        data = signup(user, password, phone, email, address, gender, 4444)
        return render_template('Signup.html', msg=data)    
        
        


@app.route('/Logout')
def Logout():
    return render_template('index.html', msg='')



if __name__ == '__main__':
    app.run()










