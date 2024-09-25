CMD /C mvn clean install -U -DskipCompileReports=true
cd lms-ear\
CMD /C mvn clean install -Ddeploy=true
cd ..
