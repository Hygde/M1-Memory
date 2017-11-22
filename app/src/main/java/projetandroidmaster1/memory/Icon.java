package projetandroidmaster1.memory;

/**
 * Created by paulv on 21/11/2017.
 */

public class Icon {
    String name;
    int number;             // the icons goes by pair : is it the number 1 or the number 2 ?
    boolean revealed;       // the icon is currently revealed ; the game is waiting while the player is choosing another icon
    boolean discovered;     // if the icon has already been previously revealed but not found
    boolean found;          // if the icon is found : always show it

    public Icon(String name, int number) {
        this.name = name;
        revealed = false;
        discovered = false;
        found = false;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public String getName() {
        return name;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}
