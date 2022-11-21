
build: Transity.class

Transity.class: Transity.java
	javac Transity.java

run: Transity.class
	java -cp .:mssql-jdbc-11.2.0.jre11.jar Transity ./auth.cfg

clean:
	rm Transity.class