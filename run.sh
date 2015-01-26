javac -d ./bin -cp ./bin:./lib/jsch-0.1.51.jar:./lib/core.jar:./lib/serial.jar:./lib/RXTXcomm.jar -sourcepath ./src src/swing/GUI.java

java -Djava.library.path=./lib -Dfile.encoding=US-ASCII -cp ./bin:./lib/jsch-0.1.51.jar:./lib/core.jar:./lib/serial.jar:./lib/RXTXcomm.jar swing.GUI
