import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

/*
TO DO:

-Casos especiais de html invalido
-Funções: isTag, isOpeningTag e isClosureTag
-tratamento de excessoes
-Otimizar codigo
-Analisar questões de segurança do codigo
- Apagar trechos irrelevantes
*/

public class HtmlAnalyzer{

    public static void main(String[] args){

        if(args.length!=1){
            //missing or too many args
            System.out.println("Missing or too many args. Try 'java HtmlAnalyzer <URL>'");
            return;
        }  
        
        try {

            String result = getMaxDepthText(getContent(new URL(args[0])));     //Gets the max depth text or HTML malformation
            System.out.println(result);

        } catch (MalformedURLException e) {
            e.getMessage();
            System.out.println("URL not valid");
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Error");
        }

    }

    public static String[] getContent(URL url) throws IOException{  //Transform the HTML content into an array of terms

        BufferedReader htmlContent = new BufferedReader(new InputStreamReader(url.openStream()));    //Reding HTML content
        
        String line;  
        String content = "";

        
        while ((line=htmlContent.readLine()) != null){     //Transforming HTML content into a single string
            content = content.concat(line);
        }

        // v-- APAGAR AQUI QUANDO PRONTO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        content = """
<html>
<head>
<title>
Este e o titulo.
</title>
</head>
<body>
<div>
<p>
Texto mais profundo
</p>
</div>
</body>
</html>
""";




        // transforming content in a vector of terms
        String[] terms = content.replaceAll("<", " <").replaceAll(">", "> ").replaceAll("\\s+", " ").trim().split(" ");
        
        //for(String term:terms){
        //    System.out.println(term);
        //}

        return terms;
    }

    public static String getMaxDepthText(String[]terms){

        Stack<String> stack = new Stack<>();             // Stacking tags to control text depths and html malformation
        int maxDepth = 0;                                // Depth of the deepest text
        String maxDepthText = "";                        // maximun depth text
        Boolean lastTermWasMaxDepthText = false;         // Checks if the last term was part of the max depth text

        System.out.println("Current depth: " + stack.size());   //0

        for(String term:terms){      //Checks term by term
            
            if(term.startsWith("</")){      //Term is a closure tag

                if(!stack.isEmpty() && stack.pop().equals(term.replace("</", "<"))){   //Checks if Stack is not empty and if closing tag matches last opening tag
                //stack.pop removes stacks's last opening tag

                    lastTermWasMaxDepthText = false;   //Term is no longer part of the maxDepthText

                    System.out.println("Current depth: " + stack.size());

                }else{      // Closing tag doesn't match last opening tag or stack had no opening tags in it 
                    return "malformed HTML";
                }
                
            }else if(term.startsWith("<")){     //Term is an opening tag

                stack.push(term);               //Adds tag to the stack

                lastTermWasMaxDepthText = false;   //Term is no longer part of the maxDepthText

                System.out.println("Current depth: " + stack.size());

            }else {                         //Term is a Text

                if(stack.size() > maxDepth){        //This text is the max depth text
                    maxDepth = stack.size();        //Saves current depth in maxDepth
                    maxDepthText = term;            //maxDepthTExt becomes the term
                    lastTermWasMaxDepthText = true;    //If next term is also a text, it is also a part of the max depth text

                    System.out.println(" -New Max Depth Text found (depth: " + stack.size() + ")");

                }else if(lastTermWasMaxDepthText){      //Last term was part of the max Depth text
                    //This means the current term is also a part of the max depth text

                    maxDepthText = maxDepthText.concat(" " + term);     //concatenates the term to the max depth text

                }

            }

        }

        // Special cases:
        // Primeiro termo não pode ser texto
        // Ultimo termo tem que ser tag de fechamento
        // Pilha tem que terminar vazia
        // não pode haver texto sem tag aberta (verificar se está em tag de texto)

        if(!(terms[0].startsWith("<") && terms[0].endsWith(">"))){ //HTML starts with a text (no opening tags)
            return "malformed HTML";
        }
        if(!(terms[terms.length -1].startsWith("<") && terms[terms.length -1].endsWith(">"))){ //HTML ends with a text (tags opened)
            return "malformed HTML";
        }
        if(!stack.isEmpty()){            //Opening tags were not closed
            return "malformed HTML";
        }

        return maxDepthText;
    }

}