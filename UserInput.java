import java.util.Scanner;

/**
 * UserInput class implements generic methods for taking input from the user.
 * Its two main methods are for reading a string from the user and reading an number from the user
 * 
 * @author (Bhavik maneck) 
 * @version (v1)
 */
public class UserInput
{
    final static String ESC = "\033[";

    /**
     * Constructor for UserInput
     */
    public UserInput()
    {
        //Nothing to set or create
    }
    
    // Clears the terminal screen for user, requires console to support ANSI escape sequences
    // Taken from: http://stackoverflow.com/questions/4888362/commands-in-java-to-clear-the-screen
    public void clearScreen()
    {
        System.out.print(ESC + "2J"); 
    }
    
    /*
     * Check if every character in a given string is numeric
     *
     * Takes as argument a string for checking
     *
     * Returns false to indicate string is not a numeric otherwise true to indicate it is numeric
     *
     */
    private boolean isStringNumeric(String stringToCheck)
    {
        for (int i = 0; i < stringToCheck.length(); i++) //Iterate through the string character by character and check if it is a number
        {
            //Consider one character at a time
            String c = stringToCheck.substring(i,i+1);
            
            //Check if this character is a number
            if (!(c.equals("0") || c.equals("1") || c.equals("2") || c.equals("3") || 
                  c.equals("4") || c.equals("5") || c.equals("6") || c.equals("7") || 
                  c.equals("8") || c.equals("9")))
            {
                return false;
            }
        }
       
        return true;
    }
    
    // Generic method that pauses execution until user presses enter
    public void pressEnterToContinue()
    {
       Scanner console = new Scanner(System.in);
       System.out.print("Press enter to continue...");
       console.nextLine();
    }
    
    /*
     * Generic method for reading a single integer from user between 1 and maxNumber
     * 
     * Arguments:
     *      maxNumber - maximum number to accept, set maxNumber <= 0 if no check for maximum required
     *      inputPrompt - string to display to user when asking for a number
     *      
     * If input is a not a valid number within the required range a 0 is returned to indicate error,
     * otherwise valid int from user is returned
     * 
     */
    public int readIntegerFromUser(int maxNumber, String inputPrompt)
    {
        Scanner console = new Scanner(System.in);  // Create a scanner object read input from user
        boolean error = true;  //error is true to begin as we have no number from user initiall
        int numberChosen = 0;
        int inputNumber = -1;

        System.out.print("\n"+inputPrompt);   // Display the custom prompt specified asking for the input to the user
        String input = console.nextLine();    // Read the line inputted by user
        input = input.trim();   // Remove spaces from either end of the string
        
        //First check if something was at least entered by user, otherwise error
        if (input.length() == 0)
        {
           System.out.print("\nError - you must enter something!\n");
           pressEnterToContinue();
           error = true;
        }
        else 
        {
            //Check if input is a number first
            boolean inputIsNumber = isStringNumeric(input); 
            
            //If Input is a number check if its a valid number, otherwise tell user we need a number
            if (inputIsNumber)
            {
                // We know string is a number so convert it to an integer now
                inputNumber = Integer.parseInt(input);
                
                // Only do more checks if a maximum number to check for has been set (maxNumber <= 0 indicates no check for maximum)
                if (maxNumber > 0)
                {
                    // Check that the input number is greater than 0 and less than or equal to the maximum provided
                    if ((inputNumber <= maxNumber) && (inputNumber > 0))
                    {
                        numberChosen = inputNumber;
                        error = false;
                    }
                    else
                    {
                        System.out.print("\nError! Entered number must be between 1 and " + maxNumber + "\n");
                        pressEnterToContinue();
                        error = true;
                    }
                }
                else 
                {
                    numberChosen = inputNumber;
                    error = false;
                }
            } 
            else
            { 
                // Input string wasn't a number
                System.out.print("\nError! Input must be a number!\n");
                pressEnterToContinue();
                error = true;
            }
        }
        
        if (error)
        {
            // There was some error in the input from user, 0 is returned to indicate error
            return 0;
        }
        else
        {
            // no error than return the number, guarunteed to be between 1 and maxNumber provided
            return numberChosen;
        }
    }
    
    /*
     * Read an optionally non-empty string from user, with message prompt
     *
     * Takes as argument a string for checking
     *
     * Returns false to indicate string is not a numeric otherwise true to indicate it is numeric
     *
     */
    public String readStringFromUser(String messagePrompt, boolean emptyStringAllowed)
    {
       Scanner console = new Scanner(System.in); // Create a scanner object read input from user
       System.out.print(messagePrompt);
       String inputName = console.nextLine();
       inputName = inputName.trim();   // Remove spaces from either end of the string so whitespace alone isn't accepted
       
       if (!emptyStringAllowed)
       {
           while (inputName.length() == 0) // If user enters nothing for input keep asking for name until get back string of length greater than 0
           {
               clearScreen();
               System.out.print("Error - input must be at least one character long.\n");
               System.out.print(messagePrompt);
               inputName = console.nextLine();
               inputName = inputName.trim();   // Remove spaces from either end of the string
           }
       }
       
       return inputName;
    }

}
