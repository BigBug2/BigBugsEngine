package engine;

public class Exceptions {
    public static class NoValidID extends RuntimeException {
        public NoValidID() {
            super("id number was not set to object before he was updated");
        }
    }
}
