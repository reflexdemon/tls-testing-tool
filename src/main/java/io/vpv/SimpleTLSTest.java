package io.vpv;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Venkat on 4/27/18.
 * The Simple tls test Main Class.
 */
public class SimpleTLSTest {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36";

    /**
     * Main.
     *
     * @param args the args
     * @throws Exception the exception
     */
    public final static void main(String[] args) throws Exception {
        // System.setProperty("javax.net.debug", "ssl:handshake");

        SimpleTLSTest test = new SimpleTLSTest();
        printUsage();
        Map<String, String> argMap = parseArgs(args);

        //Required fields
        String endpoint = (argMap.get("endpoint") != null)? argMap.get("endpoint") : getInputFromConsole("Endpoint: ");
        String certPath = (argMap.get("keystore") != null)? argMap.get("keystore") : getInputFromConsole("Certificate Path: ");
        String certPasswd = (argMap.get("password") != null)? argMap.get("password") : getPasswordFromConsole("Password: ");

        //Optional Items
        String data = (argMap.get("data") != null)? readFile(argMap.get("data")) : "";
        String method = (argMap.get("method") != null)? argMap.get("method") : "GET";
        String contentType = (argMap.get("contentType") != null)? argMap.get("contentType") : "text/xml";
        test.tessTLSConnection(endpoint, certPath, certPasswd, method, contentType, data);
    }



    private static String getPasswordFromConsole(String input) {
        java.io.Console console = System.console();
        String password = new String(console.readPassword(input));
        return password;
    }

    private static String getInputFromConsole(String input) {
        java.io.Console console = System.console();
        String regular = console.readLine(input);
        return regular;
    }

    /**
     * Read file string.
     *
     * @param path the path
     * @return the string
     * @throws IOException the io exception
     */
    static String readFile(String path) throws IOException {
        return readFile(path, StandardCharsets.UTF_8);
    }

    /**
     * Read file string.
     *
     * @param path     the path
     * @param encoding the encoding
     * @return the string
     * @throws IOException the io exception
     */
    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    private static Map<String, String> parseArgs(String[] args) {
//        System.out.println("Arguments:" + Arrays.asList(args));
        int counter = 0;
        Map<String, String> map = new HashMap<>();
        while (counter < args.length) {
            String arg = args[counter++];
            if ("--endpoint".equalsIgnoreCase(arg) || "-e".equalsIgnoreCase(arg)) {
                map.put("endpoint", args[counter++]);
            }
            if ("--keystore".equalsIgnoreCase(arg) || "-k".equalsIgnoreCase(arg)) {
                map.put("keystore", args[counter++]);
            }
            if ("--password".equalsIgnoreCase(arg) || "-p".equalsIgnoreCase(arg)) {
                map.put("password", args[counter++]);
            }
            if ("--method".equalsIgnoreCase(arg) || "-m".equalsIgnoreCase(arg)) {
                map.put("method", args[counter++]);
            }
            if ("--data".equalsIgnoreCase(arg) || "-d".equalsIgnoreCase(arg)) {
                map.put("data", args[counter++]);
            }
            if ("--contentType".equalsIgnoreCase(arg) || "-c".equalsIgnoreCase(arg)) {
                map.put("contentType", args[counter++]);
            }
        }
        return map;
    }
    private static void printUsage() {
        System.out.println("java -jar tls-testing-tool-jar-with-dependencies.jar [options]");
        System.out.println();
        System.out.println("Required Options: If not supplied it will be prompted.");
        System.out.println("--endpoint or -e            Endpoint; example https://www.google.com");
        System.out.println("--keystore or -k            File Path to the JKS keystore");
        System.out.println("--password or -p            JKS keystore password");
        System.out.println();
        System.out.println("Optional Options: If not supplied it will be defaulted.");
        System.out.println("--method or -m              HTTP Method; default: POST");
        System.out.println("--data or -d                Payload file location to test XML requests, default: \"\"");
        System.out.println("--contentType or -c         Content Type of the request; default: text/xml");
        System.out.println();
    }
    private void tessTLSConnection(String endpoint, String certPath, String certPasswd, String method, String contentType, String data) throws Exception {
        HttpURLConnection httpsConn = null;
        OutputStreamWriter wr = null;
        BufferedReader in = null;
        try {

            initialzeTLSConnection(certPath, certPasswd);
            System.out.println("Trying to connect to " + endpoint);
            URL url = new URL(endpoint);
            httpsConn = (HttpsURLConnection) url.openConnection();

            httpsConn.setRequestMethod(method);

            httpsConn.setRequestProperty("User-Agent", USER_AGENT);
            httpsConn.setRequestProperty("Accept-Language", "UTF-8");
            httpsConn.setRequestProperty("Content-Type", contentType);
            httpsConn.setDoOutput(true);

            // Write the XML Payload
            wr = new OutputStreamWriter(httpsConn.getOutputStream());
            wr.write(data);
            wr.flush();

            int contentLength = httpsConn.getContentLength();
            // Get Response Headers
            System.out.println("HTTP Response Code: " + httpsConn.getResponseCode());
            System.out.println("HTTP Content Type : " + httpsConn.getContentType());
            System.out.println("HTTP Content Length : " + contentLength);
            System.out.println("Headers : " + httpsConn.getHeaderFields());

            if (contentLength > 0) {
                // Read the Response Body
                in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream()));
                System.out.println(readFromStream(in));
            } else {
                //Read error
                if (null != httpsConn.getErrorStream()) {
                    in = new BufferedReader(new InputStreamReader(httpsConn.getErrorStream()));
                    System.out.println(readFromStream(in));
                }
            }
        } catch (SSLHandshakeException e) {
            System.err.println("Unable to connect to " + endpoint + " using " + certPath);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unable to connect to " + endpoint + ". Please ensure the input values are correct.");
            e.printStackTrace();
        } finally {
            if (null != httpsConn) {
                httpsConn.disconnect();
            }
            if (null != wr) {
                wr.close();
            }
            if (null != in) {
                in.close();
            }
        }
    }

    private String readFromStream(BufferedReader in) throws IOException {
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\n");
        }
        return response.toString();
    }

    private SSLSocketFactory initialzeTLSConnection(String certPath, String certPasswd) throws Exception {
        SSLContext sc = SSLContext.getInstance("TLS");

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(certPath), certPasswd.toCharArray());

        kmf.init(ks, certPasswd.toCharArray());
        sc.init(kmf.getKeyManagers(), null, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        return sc.getSocketFactory();
    }
}
