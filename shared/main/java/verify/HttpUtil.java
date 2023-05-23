package verify;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;


public class HttpUtil {

    public static String sendGet(String url, String name) {
        String result = "";
        BufferedReader in = null;

        try {
            String urlNameString = url + "?" + name;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();

            connection.setDoOutput(true);
            connection.setReadTimeout(981);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map map = connection.getHeaderFields();

            String s;

            for (Iterator line = map.keySet().iterator(); line.hasNext(); s = (String) line.next()) {
                ;
            }

            String line1;

            for (in = new BufferedReader(new InputStreamReader(connection.getInputStream())); (line1 = in.readLine()) != null; result = result + line1 + "\n") {
                ;
            }
        } catch (Exception exception) {
            ;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception exception1) {
                ;
            }

        }

        return result;
    }

    public static String webget(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            response.append("\n");
        }

        in.close();
        return response.toString();
    }
}
