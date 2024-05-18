package libraries.geometry;

public class IncorrectBinding extends RuntimeException {
    IncorrectBinding(String message) {
        super(message);
    }

    IncorrectBinding() {
        super();
    }

}
