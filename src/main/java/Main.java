import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) throws IOException {

        PrintWriter out = getPrintWriter();
        //getUrls
        //getResponses
        //handle responses
        out.write("[");
        boolean appendComma = false;

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        for (int i = 97; i <= 122; i++) {
            for (int j = 97; j <= 122; j++) {
                for (int k = 97; k <= 122; k++) {
                    String url = "https://trustarc.com/trusted_directory_query_081515.php?name="
                            + Character.toString((char) i)
                            + Character.toString((char) j)
                            + Character.toString((char) k);
                    Map<String, String> nameToResponse = new HashMap<>();
                    String json = getRequest(url);
                    nameToResponse.put(url, json);
                    if (Objects.nonNull(json) && !json.equals("[]")) {
                        json = json.substring(1, json.length() - 1);
                        if (appendComma) out.write(",");
                        appendComma = true;
                        out.write(json);
                    }
                }
            }
        }
        out.write("]");
        out.close();
    }

    private static PrintWriter getPrintWriter() throws IOException {
        File file = new File("TrustArc_DB.txt");
        FileWriter fw = new FileWriter(file);
        PrintWriter out = new PrintWriter(fw);
        return out;
    }


    public static String getRequest(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
            return response.toString();
        } else {
            System.out.println("GET request not worked");
            return null;
        }

    }

}
