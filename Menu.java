import java.util.ArrayList;
import java.util.Iterator;

/**
 * Menu class implements a generic menu with title and options that can be displayed to the user
 * 
 * @author (Bhavik maneck) 
 * @version (v1)
 */
public class Menu
{
    private String menuTitle;
    private ArrayList<String> menuOptions;

    /**
     * Constructor for objects of class Menu
     * Takes a title and ArrayList of option Strings to initialize the class fields with
     * 
     * @throws IllegalStateException if title is blank or there are less than 2 options
     */
    public Menu(String title, ArrayList<String> options)
    {
        if ((title.trim().length() == 0) || (options.size() < 2))
        {
            throw new IllegalStateException("Must provide non-empty title and at least 2 options");
        }
        
        menuTitle = title;
        menuOptions = options;
    }
    
    /*
     * Method for adding a new option string to the menu
     *
     * Takes as argument a string for the option
     *
     * @throws IllegalStateException
     *
     */
    public void addOptionToMenu(String option)
    {
        if (option.trim().length() == 0)
        {
            throw new IllegalStateException("Must provide non-blank option.\n");
        }
        
        menuOptions.add(option);
    }
    
    //Displays menu to screen
    public void displayMenu()
    {
        System.out.print(menuTitle);
        System.out.print("============================\n");   // Breaks up the menu header and options
        for (int i = 0; i < menuOptions.size(); i++)          // Prints each menu option prefixed with a numberic option
        {
            System.out.print("(" + (i+1) + "): " + menuOptions.get(i));
        }
    }
    
    
    public String getTitle()
    {
        return menuTitle;
    }
    
    /*
     * Set the menu title
     *
     * Takes as argument a string to set to the title
     *
     * @throws IllegalStateException
     *
     */
    public void setTitle(String title)
    {
        if (title.trim().length() == 0)
        {
            throw new IllegalStateException("Must provide non-blank title.\n");
        }
        
        menuTitle = title;
    }
}
