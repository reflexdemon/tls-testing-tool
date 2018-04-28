# The TLS Testing Tool

This is a simple tool to test the 2-way SSL/TLS connection using the truststore.

## Usage

```
❯ java -jar target/tls-testing-tool-jar-with-dependencies.jar
java -jar tls-testing-tool-jar-with-dependencies.jar [options]

Required Options: If not supplied it will be prompted.
--endpoint or -e            Endpoint; example https://www.google.com
--keystore or -k            File Path to the JKS keystore
--password or -p            JKS keystore password

Optional Options: If not supplied it will be defaulted.
--method or -m              HTTP Method; default: POST
--data or -d                Payload file location to test XML requests, default: ""
--contentType or -c         Content Type of the request; default: text/xml
```

## Build

To build the jar file just run the `mvn package`.
```
❯ mvn package                                                                                                                                             ⏎
[INFO] Scanning for projects...
[INFO]
[INFO] ----------------------< io.vpv:tls-testing-tool >-----------------------
[INFO] Building tls-testing-tool 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ tls-testing-tool ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 0 resource
[INFO]
[INFO] --- maven-compiler-plugin:3.7.0:compile (default-compile) @ tls-testing-tool ---
[INFO] Changes detected - recompiling the module!
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] Compiling 1 source file to ~/Development/tls-testing-tool/target/classes
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ tls-testing-tool ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory ~/Development/tls-testing-tool/src/test/resources
[INFO]
[INFO] --- maven-compiler-plugin:3.7.0:testCompile (default-testCompile) @ tls-testing-tool ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ tls-testing-tool ---
[INFO] No tests to run.
[INFO]
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ tls-testing-tool ---
[INFO] Building jar: ~/Development/tls-testing-tool/target/tls-testing-tool.jar
[INFO]
[INFO] --- maven-assembly-plugin:3.1.0:single (make-assembly) @ tls-testing-tool ---
[INFO] Building jar: ~/Development/tls-testing-tool/target/tls-testing-tool-jar-with-dependencies.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.768 s
[INFO] Finished at: 2018-04-28T08:28:31-04:00
[INFO] ------------------------------------------------------------------------
```
# License
MIT