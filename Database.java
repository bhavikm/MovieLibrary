import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;
import java.util.Scanner;

/**
 * Database class holds an ArrayList of Movie objects.
 * The database can be saved to a text file and also read in Movie objects from text file
 * 
 * @author (Bhavik Maneck) 
 * @version (v1)
 */
public class Database
{
    private ArrayList<Movie> movies;

    /**
     * Constructor for objects of class Database
     */
    public Database()
    {
        movies = new ArrayList<Movie>();
    }
    
    /**
     * Constructor for objects of class Database
     * 
     * Takes in ArrayList of Movie objects that is set to the class field
     */
    public Database(ArrayList<Movie> newMovies)
    {
        movies = newMovies;
    }
    
    /**
     * Constructor for objects of class Movie with provided movie fields
     * 
     * Takes as arguments:
     *      movieTitle - title of movie, must not be blank
     *      movieDirector - director of movie, must not be blank
     *      movieActor1,movieActor2,movieActor3 - actor names of the movie, this implementation allows no actors to be provided
     *      movieRating - integer between 1 and 10 inclusive
     *      
     *  @throws FileNotFoundException if provided filename does not exist (in same directory as this program)
     *  @throws IllegalStateException if format of line with Movie information does not conform to what was expected
     *  @throws IOException if any file reading errors
     */
    public void loadDatabaseFromFile(String filename)
    {
        try 
        {
            FileReader fr = new FileReader(filename);
            
            try 
            {
                Scanner scan = new Scanner(fr);
                
                int lineNumber = 0;
        
                while (scan.hasNextLine()) 
                {
                    lineNumber++;
                    String line = scan.nextLine();    // Read one line of the text file into a string
                    
                    String[] parts = line.split(",");  // Split the line by comma into a String array
                    String title = parts[0];
                    String director = parts[1];
                    String actor1 = parts[2];
                    String actor2 = parts[3];
                    String actor3 = parts[4];
                    String rating = parts[5];
                    rating = rating.trim();
                    int ratingNumber = Integer.parseInt(rating); //conver the rating number string to an integer
                    
                    try
                    {
                        Movie movie =  new Movie(title,director,actor1,actor2,actor3,ratingNumber);
                        addMovie(movie);
                    }
                    catch (IllegalStateException e)
                    {
                        System.out.print("Error in movie information read from file.\n");
                    }
                }
            }
            finally
            {
                fr.close();
            }
        } 
        catch (FileNotFoundException e) 
        {
            System.out.print("File not found\n");
        }
        catch (IOException e)
        {
            System.out.print("Unexpected I/O exception\n");
        }
    }
    
    // Return whole internal database of Movie objects
    public ArrayList<Movie> getAllMovies()
    {
        return movies;
    }
    
    // Add a movie to the database, throws a NullPointerException if object in argument is not set
    public void addMovie(Movie newMovie)
    {
        if (newMovie == null)
        {
            throw new NullPointerException("Must provide a valid movie object\n");
        }
        
        movies.add(newMovie);
    }
    
    // Deletes all the Movie objects in the current database ArrayList
    public void clearAll()
    {
        movies.clear();
    }
    
    // Delete a specific movie from the database
    public void deleteMovie(Movie movieToDelete)
    {
        movies.remove(movieToDelete);
    }
    
    // Displays all movie titles in the database
    public void displayAllMovieTitles()
    {
        if (movies.size() == 0)
        {
            System.out.print("No movies\n");
        }
        else 
        {
            Iterator<Movie> it = movies.iterator();
            int i = 1;
            while (it.hasNext())
            {
                Movie movie = it.next();
                System.out.print("Movie number " + i + "\n");
                System.out.print("===============================\n");
                System.out.print("Title : " + movie.getTitle() + "\n\n");
                i++;
            }
        }
    }
    
    
    /*
     * Saves all movies in database to specified file. 
     * If file already exists it will be overwritten.
     * Writes to file one movie per line with consistent format:
     *    tite,director,actor1,actor2,actor3,rating
     * 
     * @param - filename - file to write to, if file does not exist it will be created
     * 
     * @throws IOException if there is error writing to file
     */
    public void saveMoviesToFile(String filename)
    {
        try 
        {
            PrintWriter writer = new PrintWriter(filename);
            
            try
            {
                Iterator<Movie> it = movies.iterator();
                while (it.hasNext())
                {
                    Movie movie = it.next();
                    String title = movie.getTitle();
                    String director = movie.getDirector();
                    ArrayList<String> actors = movie.getActors();
                    int rating = movie.getRating();
                    
                    String actorsOutputLine = "";
                    Iterator<String> itActors = actors.iterator();
                    int numberActors = 0;
                    while (itActors.hasNext())
                    {
                        String actor = itActors.next();
                        actor = actor.concat(",");  // add a comma to the actor string to be written to file
                        actorsOutputLine = actorsOutputLine.concat(actor);  // add the actor to all the others to be written to file
                        numberActors++;
                    }
                    // We know there can be only 3 actors so for the number of actors that were not in the object 
                    // add commas to ensure format of saving movie information to file is kept
                    for (int i = 0; i < (3-numberActors); i++)
                    {
                        actorsOutputLine = actorsOutputLine.concat(",");
                    }
                    
                    writer.write(title+","+director+","+actorsOutputLine+rating+"\n"); //Write line to file with all movie information
                }
            }
            finally
            {
                writer.close();
            }
            
        } 
        catch (IOException e)
        {
            System.out.print("Unexpected I/O exception\n");
        }
    }
    
    
    /*
     * Generic method for searching the database of movies (can search by title, director or minimum
     * rating). The search for strings is case-insensitive.
     * 
     * Takes as input:
     *      searchString - the string to look for (could be the movie or director)
     *      searchKey    - the field of the Movie class to search against (title, director or favourite)
     *      minRating    - Only required when searching for favourite, the minimum rating to accept
     * 
     * Returns ArrayList of Movie objects that matched the search criteria
     */
    public ArrayList<Movie> searchForMovie(String searchString, String searchKey, int minRating)
    {
        ArrayList<Movie> foundMovies = new ArrayList<Movie>();
        Iterator<Movie> it = movies.iterator();
        String searchStringLower = searchString.toLowerCase();  //conver to lowercase to ensure search is case-insensitive
        
        while (it.hasNext())
        {
            //get the next movie in the database
            Movie movie = it.next();
            
            //convert to lowercase to ensure search is case-insensitive
            String title = movie.getTitle().toLowerCase();         
            //convert to lowercase to ensure search is case-insensitive
            String director = movie.getDirector().toLowerCase();
            
            int rating = movie.getRating();
            
            if (searchKey.equals("title"))
            {
                if (title.equals(searchStringLower))
                {
                    foundMovies.add(movie);
                }
            } else if (searchKey.equals("director")) {
                if (director.equals(searchStringLower))
                {
                    foundMovies.add(movie);
                }
            } else if (searchKey.equals("favourite")) {
                if (rating >= minRating)
                {
                    foundMovies.add(movie);
                }
            } else {
                System.out.print("\nError! Can not search over that key!\n");
            }
        }
        
        return foundMovies;
    }

}
