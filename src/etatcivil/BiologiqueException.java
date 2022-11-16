/*
 * Cours de remise a niveau Java.
 * Université de Franche-Comté.
 */
package etatcivil;

/**
 *
 * @author Expression yonas i
 */
public class BiologiqueException extends IllegalArgumentException {

    BiologiqueException(String message) {
        super(message);
    }
    
        BiologiqueException() {
        super();
    }
}
