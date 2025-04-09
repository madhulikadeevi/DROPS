1. Project Overview
The DROPS project secures cloud data by dividing and replicating file fragments across multiple cloud servers. This prevents attackers from reconstructing the entire file from a single compromised node, thereby improving both security and system resilience.

2. Software Requirements
The project is built using Python 3.7+ and requires Flask, NumPy, Pandas, Matplotlib, Scikit-learn, and Cryptography libraries. MySQL is used for backend data handling, and a simple web interface is developed using HTML, CSS, and JavaScript.

3. Hardware Requirements
The project can run on basic systems with an Intel i3 processor, 4GB RAM, and 40GB hard drive. For better performance, it is recommended to use at least an i5 processor and 8GB RAM.

4. Environment Setup
Install Python and all dependencies using pip, including Flask and database connectors. Set up a MySQL server with proper tables and configure the Flask backend and cloud node scripts for proper communication and storage.

5. Execution Steps
Start the MySQL database and run the Flask server. Upload a file via the web app, which will fragment and replicate it across three cloud servers. The system supports file download by reconstructing fragments stored on different nodes.

6. Testing and Validation
Verify that file uploads result in proper fragmentation and replication across nodes. Test file reconstruction through the download feature and analyze graphs for resource consumption and system performance.

7. Expected Output
The user interface will show screens for signup, login, file upload, fragment storage, and file download. The system displays a performance graph and ensures that even after server failure, the file remains retrievable.
