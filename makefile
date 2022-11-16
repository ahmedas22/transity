
build: Transity.class

CallTheManager.class: Transity.java
	javac Transity.java

run: CallTheManager.class
	java -cp .:sqlite-jdbc-3.39.3.0.jar Transity

clean:
	rm Transity.class
