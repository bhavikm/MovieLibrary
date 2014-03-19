import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Driver class is the main controller class that runs the program and controls the logic of reading input
 * from the user, displaying appropiate menu and text, creating and storing objects in the database. This is done 
 * through methods of appropiate classes for each function.
 * 
 * @author (Bhavik Maneck) 
 * @version (v1)
 */
public class Driver
{
    private Database movieDatabase;
    
    /**
     * Constructor for objects of class Driver
     * 
     * Loads a database of movies from file
     */
    public Driver()
    {
        movieDatabase = new Database();
        movieDatabase.loadDatabaseFromFile("myvideos.txt");
    }
    
    /*
     * Deletes a movie from the database, if a matching title read from user is found.
     * Also confirms with user before deleting.
     */
    private void deleteMovie()
    {
        UserInput userInput = new UserInput();
        String movieTitleToDelete = userInput.readStringFromUser("Enter movie title to delete: ", false);
        
        // Search for the movie to delete by its title
        ArrayList<Movie> moviesForDelete = movieDatabase.searchForMovie(movieTitleToDelete,"title",0);
        
        if (moviesForDelete.size() == 0) //If no matching movie is found
        {
            System.out.print("\nNo movies found matching that title.\n\n");
        }
        else //Movie was found to delete
        {
            System.out.print("\n");
            moviesForDelete.get(0).displayMovieInformation();  //There will only be one movie element as titles are unique so get it and display it
            
            try
            {
                //Set up the confirmation menu with yes or no options
                String deleteMenuTitle = "\nAre you sure you want to delete: " + moviesForDelete.get(0).getTitle()  + " ?\n";
                ArrayList<String> deleteMenuOptions = new ArrayList<String>();
                deleteMenuOptions.add("Yes\n");
                deleteMenuOptions.add("No\n");
                Menu confirmDeleteMenu = new Menu(deleteMenuTitle,deleteMenuOptions);
                
                //Initially set this to 0 so while loop will be entered
                int deleteOptionChosen = 0;
                //Keep asking for user to enter option until valid input received (readIntegerFromUser returns 0 for error)
                while (deleteOptionChosen == 0)
                {
                    userInput.clearScreen();
                    confirmDeleteMenu.displayMenu();
                    // Read which delete option the user wants (1 for yes 2 for no)
                    deleteOptionChosen = userInput.readIntegerFromUser(2, "Please choose an option between 1 and 2: ");
                }
                
                if (deleteOptionChosen == 1) //User wants to delete the movie
                {
                   /*
                    * We can just get the first movie in the ArrayList of movies because only movies of one title can exist
                    * and we know we found a movie in our search
                    */
                   movieDatabase.deleteMovie(moviesForDelete.get(0));
                   System.out.print("\nMovie successfuly deleted.\n\n");
                }
                else //User doesn't want to delete the movie
                {
                    System.out.print("\nOk. movie won't be deleted.\n\n");
                }
            }
            catch (IllegalStateException e)
            {
                System.out.print("\nError creating menu!\n");
            }
        }
    }
    
    /*
     * Displays all movies with information for above and including a minimum rating read from the user
     */
    private void displayFavouriteMovies()
    {
        UserInput userInput = new UserInput();
       
        int minFavRating = 0;   // Set to 0 initially so while loop asking for user to enter rating is entered
        while (minFavRating == 0)  // Keep asking user to enter number between 1 and 10
        {
            userInput.clearScreen();
            //readIntegerFromUser returns 0 if number between 1 and 10 is not entered (so loop keeps going)
            minFavRating = userInput.readIntegerFromUser(10, "Please choose a minimum favourite rating between 1 and 10: ");
        }
        
        ArrayList<Movie> favMovies = movieDatabase.searchForMovie("", "favourite", minFavRating);
        if (favMovies.size() == 0)
        {
            System.out.print("\nNo movies found above or equal to that rating.\n\n");
        }
        else 
        {
            System.out.print("\nFavourite Movies:\n\n");
            
            Iterator<Movie> it = favMovies.iterator();
            while (it.hasNext())
            {
                Movie movie = it.next();
                movie.displayMovieInformation();
            }
               
        }
    }

    /*
     *  Run the program on the command line after compiling with: java Driver
     */
    public static void main(String[] args){
        Driver movieLibrary = new Driver();
        movieLibrary.runDriver();
    }
    
    /*
     * Read new movie information from user
     * 
     * Returns a Movie object with information set
     */
    private Movie readMovieInformationFromUser()
    {
        UserInput userInput = new UserInput();

        String messagePromptForUser = "\nPlease enter the movie title: ";
        boolean titleAlreadyExists = true;  // Initially set to true so will enter while and start asking user to enter title
        String title = "";
        while (titleAlreadyExists)  // Keep asking user for a title until one is entered that isn't already in the database
        {
            userInput.clearScreen();
            title = userInput.readStringFromUser(messagePromptForUser, false);
            ArrayList<Movie> moviesMatchingTitle = movieDatabase.searchForMovie(title,"title",0);
            if (moviesMatchingTitle.size() == 0)
            {
                titleAlreadyExists = false;  //Movie title doesn't exist in database so exit out of loop
            }
            else 
            {
                System.out.print("\nError a movie with that title already exists!.\n\n");
                userInput.pressEnterToContinue();       
            }
        }
        
        //Read movie director
        messagePromptForUser = "\nPlease enter the movie director: ";
        String director = userInput.readStringFromUser(messagePromptForUser, false);
        
        //Read actor 1
        messagePromptForUser = "\nPlease enter the frist movie actor: ";
        String actor1 = userInput.readStringFromUser(messagePromptForUser, true);
        
        //Read actor 2
        messagePromptForUser = "\nPlease enter the second movie actor: ";
        String actor2 = userInput.readStringFromUser(messagePromptForUser, true);
        
         //Read actor 3
        messagePromptForUser = "\nPlease enter the third movie actor: ";
        String actor3 = userInput.readStringFromUser(messagePromptForUser, true);
                
        //Read movie rating from user
        messagePromptForUser = "Please enter the movie rating: ";
        int ratingFromUser = 0; // Set to 0 initially so while loop is entered
        while (ratingFromUser == 0)
        {
            //method returns 0 for error so will keep asking until valid integer is entered
            ratingFromUser = userInput.readIntegerFromUser(10, messagePromptForUser);  
        }
        
        try
        {
            Movie newMovie = new Movie(title, director, actor1, actor2, actor3, ratingFromUser);
            // Return the new movie object with all the read in information
            return newMovie;
        }
        catch (IllegalStateException e)
        {
            System.out.print(e.getMessage());
            return null;
        }
    }
    
    /*
     * Main method for continuously displaying the menu, reading option from the user and calling program option methods
     */
    public void runDriver()
    {
        boolean continueProgram = true;
        int optionChosen = 0;
        
        String menuTitle = "\nMovie Database\n";
        ArrayList<String> menuOptions = new ArrayList<String>();
        menuOptions.add("Search movie\n");
        menuOptions.add("Add a movie\n");
        menuOptions.add("Delete movie\n");
        menuOptions.add("Display Favourite Movies\n");
        menuOptions.add("Exit\n");
        try
        {
            Menu driverMenu = new Menu(menuTitle,menuOptions);
            UserInput userInput = new UserInput();
            while (continueProgram)
            {
                userInput.clearScreen();
                driverMenu.displayMenu();
                
                //Read the integer the user inputted, 0 is returned by readIntegerFromUser for error
                optionChosen = userInput.readIntegerFromUser(5, "Please choose an option between 1 and 5: ");
                userInput.clearScreen();
                
                switch (optionChosen) 
                {
                   // Search for movie by title or director
                    case 1:
                        searchForMovie();
                        userInput.pressEnterToContinue();
                        break;    
                        
                    // Read a new movie from the user and add it to the database of movies
                    case 2:
                        Movie newMovie = readMovieInformationFromUser();
                        movieDatabase.addMovie(newMovie);
                        break;
                    
                    // Delete a movie from the database, by matching title
                    case 3:
                        deleteMovie();
                        userInput.pressEnterToContinue();
                        break;    
                        
                    // Display all movies above and including a minimum rating taken from the user
                    case 4:
                        displayFavouriteMovies();
                        userInput.pressEnterToContinue();
                        break;
                        
                    // Exit the program, save all movies in database to file before finishing
                    case 5:
                        continueProgram = false;
                        movieDatabase.saveMoviesToFile("myvideos.txt");
                        movieDatabase.clearAll();
                        break;
                    
                    // Out of bounds option
                    default:
                        System.out.print("\nError! That option does not exist!\n");
                        break;
                }
            }
        }
        catch (IllegalStateException e)
        {
            System.out.print("\nError creating menu!\n");
        }
    }
    
    /*
     * Search for a movie by either title or director, search is case-insensitive
     * 
     * List of movies matching the search is displayed to the user
     */
    private void searchForMovie()
    {
        
        try 
        {
            //Set up the search menu with two options
            String searchMenuTitle = "\nMovie Search\n";
            ArrayList<String> searchMenuOptions = new ArrayList<String>();
            searchMenuOptions.add("Search by title\n");
            searchMenuOptions.add("Search by director\n");
            Menu searchOptionMenu = new Menu(searchMenuTitle,searchMenuOptions);
            
            UserInput userInput = new UserInput();

            int searchOptionChosen = 0;  //Initially set this to 0 so while loop will be entered
            //Keep asking for user to enter option until valid input received (readIntegerFromUser returns 0 for error)
            while (searchOptionChosen == 0)
            {
                userInput.clearScreen();
                searchOptionMenu.displayMenu();
                searchOptionChosen = userInput.readIntegerFromUser(2, "Please choose an option between 1 and 2: ");
            }
            
            // searchKey refers to the field in the Movie object we wish to search over
            String searchKey = "";
            if (searchOptionChosen == 1)
            {
                searchKey = "title";
            }
            else 
            {
                searchKey = "director";
            }
            
            // searchString will be the string the user wants to search for
            String searchString = userInput.readStringFromUser("Enter "+searchKey+" to search for: ", false);
            ArrayList<Movie> foundMovies = movieDatabase.searchForMovie(searchString,searchKey,0);
            if (foundMovies.size() == 0)
            {
                System.out.print("\nNo movies found.\n\n");
            }
            else 
            {
                System.out.print("\n");
                Iterator<Movie> it = foundMovies.iterator();
                while (it.hasNext())
                {
                    Movie movie = it.next();
                    movie.displayMovieInformation();
                } 
            }
        }
        catch (IllegalStateException e)
        {
            System.out.print("\nError creating menu!\n");
        }
    }
}
