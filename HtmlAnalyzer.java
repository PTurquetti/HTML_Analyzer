import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;



public class HtmlAnalyzer{


    public static void main(String[] args){

        if(args.length!=1){
            //missing or too many args
            System.out.println("missing or too many args. Try 'java HtmlAnalyzer <URL>'");
            return;
        }  
        
        try {

            URL url = new URL(args[0]); //Getting URL from argvs
            String[] terms = getContent(url);   //Extracting content
            result = getMaxDepth

            

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String[] getContent(URL url) throws IOException{

        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        
        String inLine;
        String content = "";

        while ((inLine=in.readLine()) != null){
            content = content.concat(inLine);
        }

        
        String[] terms = content.trim().replaceAll("<", " <").replaceAll(">", "> ").replaceAll("\\s+", " ").split(" ");
        
        for(String term:terms){
            System.out.println(term);
        }

        return terms;
    }




}