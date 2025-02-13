import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;



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
            String result = getMaxDepthText(terms); //Gets the max depth Text

            System.out.println(result);

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

        content = """
<html>

    <head>
        <title>
            Titulo
        </title>
    </head>

    <body>

        <div>

            <p>
                Texto no fundo
            </p>

        </div>

    </body>

</html>
""";



        String[] terms = content.replaceAll("<", " <").replaceAll(">", "> ").replaceAll("\\s+", " ").trim().split(" ");
        
        //for(String term:terms){
        //    System.out.println(term);
        //}

        return terms;
    }

    public static String getMaxDepthText(String[]terms){

        Stack<String> stack = new Stack<>();
        int depthCount = 0;
        int maxDepthWithText = 0;
        String maxDepthText = "";
        Boolean lastTermWasText = false;
        Boolean startedConcatMaxDepthText = false;

        System.out.println("Current depth: " + depthCount);

        for(String term:terms){
            
            if(term.startsWith("</")){      //Closure tag

                stack.pop();
                depthCount--;
                lastTermWasText = false;
                startedConcatMaxDepthText = false;

                System.out.println("Current depth: " + depthCount);
                

            }else if(term.startsWith("<")){     //Opening tag

                stack.push(term);
                depthCount++;
                lastTermWasText = false;
                startedConcatMaxDepthText = false;

                System.out.println("Current depth: " + depthCount);

            }else {      //Text

                if(!lastTermWasText){

                    System.out.println(" -New Text found (depth: " + depthCount + ")");

                    if(depthCount > maxDepthWithText){
                        maxDepthWithText = depthCount;
                        maxDepthText = term;
                        startedConcatMaxDepthText = true;
                    }
                    

                }else if(startedConcatMaxDepthText){      //Last term was a max Depth text 

                    maxDepthText = maxDepthText.concat(" " + term);

                }

                lastTermWasText = true;
                

            }

        }

        return maxDepthText;
    }

}