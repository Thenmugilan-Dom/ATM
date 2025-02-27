package Notes;

public class Note<N extends Notes> {
    // Array to hold notes, assuming a default size of 4.
    N[] arr = (N[]) new Notes[4]; // Cast to a generic type
    private int counter = 0; // Keep track of the current number of notes in the array

    // Method to add a note to the array
    public void add(N note) {
        if (counter < arr.length) {
            arr[counter] = note; // Add note to the array
            counter++;
        }
//        } else if (){
//            System.out.println("Array is full! Consider resizing the array.");
//        }
    }

    // Method to get a note by its denomination
    public N getNote(String denomination) {
        for (int i = 0; i < counter; i++) {
            if (arr[i].getNote().equals(denomination)) {
                return arr[i]; // Return the note if the denomination matches
            }
        }
        return null; // Return null if no note is found
    }

    // Method to get all notes
    public N[] getAll() {
        return arr; // Return the array of notes (it may contain null values if there are gaps)
    }

}
