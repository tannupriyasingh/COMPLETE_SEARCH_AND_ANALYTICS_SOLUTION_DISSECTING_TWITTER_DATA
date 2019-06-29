import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@WebServlet(
        name = "Proc",
        urlPatterns = "/info")
public class Proc extends HttpServlet {



        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            resp.setContentType("text/html");
            String q = req.getParameter("txt").toString();
            String [] words = q.split ("\\W+");
            StringBuilder qr = new StringBuilder();
            for(int i=0; i<words.length-1;i++)
            {
                qr.append(words[i]);
                qr.append("%20");
            }
            qr.append(words[words.length-1]);
            String qr1 = qr.toString();
            String wt = "&wt=json";
            String ht = "http://localhost:8983/solr/IRF18P4/select?indent=on&q=";
            String url= ht+qr1+wt;
            URL inurl=new URL(url);
            HttpURLConnection con=(HttpURLConnection) inurl.openConnection();
            int respcode=con.getResponseCode();
            BufferedReader in =new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String op;
            StringBuffer sb=new StringBuffer();
            while((op=in.readLine())!=null){
                sb.append(op);
            }
            in.close();

            System.out.print(sb.toString());
            JsonParser parser = new JsonParser();
            JsonElement data = parser.parse(sb.toString());
            JsonObject jobject = data.getAsJsonObject();
            JsonElement respo = jobject.getAsJsonObject("response");
            jobject = respo.getAsJsonObject();
            JsonArray docs=jobject.get("docs").getAsJsonArray();
            List<String> tweets=new ArrayList<>();
            for(int i=0;i<docs.size();i++){
                jobject=docs.get(i).getAsJsonObject();
                String tweet="";
                if(jobject.get("tweet_text").getAsString()!=null)

                 tweets.add(jobject.get("tweet_text").getAsString());

                System.out.print(tweet);
            }
            req.setAttribute("tweets",tweets);
            req.setAttribute("query",q);
            RequestDispatcher rd= req.getRequestDispatcher("/WEB-INF/jsp/info.jsp");

            rd.forward(req,resp);


        }


}
