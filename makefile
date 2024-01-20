
build: Transity.class auth.cfg

Transity.class: Transity.java
	javac Transity.java

auth.cfg:
	touch auth.cfg
	echo "username=umID" >> auth.cfg
	echo "password=sID" >> auth.cfg

project.sql:
	echo "you need the project.sql file to run this project"

run: Transity.class auth.cfg project.sql
	java -cp .:mssql-jdbc-11.2.0.jre11.jar Transity

clean:
	rm Transity.class
	rm TransitDatabase.class
	rm auth.cfg