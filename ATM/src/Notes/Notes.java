package Notes;

public abstract class Notes implements Cloneable {
    private String note; // Type of note (e.g., denomination)
    private long count;  // Count of notes

    // Constructor
    public Notes(String note, long count) {
        this.note = note;
        this.count = count;
    }
    // Getter and Setter for note
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    // Getter and Setter for count
    public long getCount() {
        return count;
    }
    public void setCount(long count){
        this.count = count;
    }
    // Overridden clone() method
    @Override
    public Notes clone() {
        try { // Try to copy the Notes object using the default cloning method.
            return (Notes) super.clone(); // Calls Object's clone method
        } catch (CloneNotSupportedException e) { // If cloning fails, handle the error.
            throw new AssertionError("Cloning not supported", e); // Throw an error because cloning should work here.
        }
    }
    // Overridden toString() method
    @Override
    public String toString() {
        return "Notes{" +
                "note='" + note + '\'' +
                ", count=" + count +
                '}';
    }
}
