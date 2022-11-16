/*
 * Cours de remise a niveau Java.
 * Université de Franche-Comté.
 */

package etatcivil;

/**
 *
 * @author yonas 
 */
public class LegalException extends IllegalArgumentException {

    public LegalException() {
    }

    public LegalException(String s) {
        super(s);
    }

}
