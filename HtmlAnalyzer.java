import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

/*
_______________HTML Analyzer________________

EASTER_EGG_URLS found :)

Author: Paulo Henrique Vedovatto Turquetti
GitHub Repo.: https://github.com/PTurquetti/HTML_Analyzer


TO DO:
-Casos especiais de html mal formado //REAVALIAR
-Analisar questões de segurança do codigo
-Testar
____________________________________________
*/

public class HtmlAnalyzer{

    public static void main(String[] args){

        if(args.length!=1){                         //missing or too many args

            System.out.println("Missing or too many args. Try 'java HtmlAnalyzer <URL>'");
            return;
        }  
        
        try {

            String result = getdeepestText(getContent(new URL(args[0])));       //Gets the max depth text or HTML malformation
            System.out.println(result);                                         //Shows the deepest text

        } catch(MalformedURLException e){                                       //Malformed URL
            //System.out.println("Malformed URL -" + e.getMessage());
            System.out.println("URL connection error");

        } catch(IOException e){                                                 //Trouble reading HTML content
            //System.out.println("URL connection error - " + e.getMessage());
            System.out.println("URL connection error");

        } catch(IllegalStateException e){                                       //Malformed HTML detected
            System.out.println("malformed HTML");

        } catch(Exception e){                                                   //Other exceptions
            //System.out.println("Unexpected error: " + e.getMessage());
            System.out.println("URL connection error");
        }

    }

    public static String[] getContent(URL url) throws IOException{  //Transform the HTML content into an array of terms

        StringBuilder content = new StringBuilder();                //Stores the entire HTML content

        try(BufferedReader htmlContent = new BufferedReader(new InputStreamReader(url.openStream()))){  //Reding HTML content from URL
            
            String line;  
            while ((line=htmlContent.readLine()) != null){     //Transforming HTML content into a single string
                content = content.append(line);
            }

        }

        String[] terms = content.toString()                 //Treating and spliting content into an array of terms
            .replaceAll("<", " <")                          //Adding space before tags
            .replaceAll(">", "> ")                          //Adding space after tags
            .replaceAll("\\s+", " ")                        //Removendo excessive spaces and other space characters
            .trim()                                         //Removing spaces at the beginning and end of the string
            .split(" ");                                    //Splitting content

        return terms;
    }

    public static String getdeepestText(String[]terms){     //Returns the max depth text or throws IllegalStateException(malformed HTML)

        Stack<String> stack = new Stack<>();                // Stack used for controlling depth and html malformation
        int maxDepth = 0;                                   // Depth of the deepest text
        StringBuilder deepestText = new StringBuilder();    // maximun depth text
        boolean lastTermWasdeepestText = false;             // Checks if the last term was part of the max depth text

        for(String term:terms){                             //Checks HTML content term by term
            
            if(isClosureTag(term)){                         //Term is a closure tag

                if(!stack.isEmpty() && stack.pop().equals(term.replace("</", "<"))){   //Checks if Stack is not empty and if closing tag matches last opening tag
                    
                                                            //stack pop removes the stacks's last opening tag
                    lastTermWasdeepestText = false;         //Term is not a part of the deepestText

                }else{                                      // Closing tag doesn't match last opening tag or stack had no opening tags in it 
                    throw new IllegalStateException("Closure tag doesn't match last opening tag or stack was empty when closure tag was found");
                }
                
            }else if(isOpeningTag(term)){                   //Term is an opening tag

                stack.push(term);                           //Adds tag to the stack
                lastTermWasdeepestText = false;             //Term is not a part of the deepestText

            }else{      //Term is a Text

                if(stack.isEmpty()){                        //Empty stack indicates tagless text
                    throw new IllegalStateException("Text found when stack was empty. Text without open tags");
                }

                if(stack.size() > maxDepth){                //This text is the max depth text
                    
                    maxDepth = stack.size();                //Saves current depth in maxDepth
                    deepestText.setLength(0);               //Cleans the deepest text string builder
                    deepestText.append(term);               //deepestText becomes the term
                    lastTermWasdeepestText = true;          //If the next term is also a text, it is also a part of the max depth text

                }else if(lastTermWasdeepestText){           //Last term was part of the max Depth text
                    
                                                                            //This means the current term is also a part of the max depth text
                    deepestText = deepestText.append(" ").append(term);     //concatenates the term to the max depth text

                }

            }

        }

        if(!stack.isEmpty()){            //Not all Opening tags were closed when finished reading HTML content
            throw new IllegalStateException("Stack not empty when finished reading content. Tags were not properly closed");
        }

        return deepestText.toString();
    }

    

    public static boolean isClosureTag(String term){    //Checks if term is an HTML closure tag
        return (term.startsWith("</") && term.endsWith(">"));
    }

    public static boolean isOpeningTag(String term){    //Checks if term is an HTML opening tag
        return (!term.startsWith("</") && term.startsWith("<") && term.endsWith(">"));
    }

}