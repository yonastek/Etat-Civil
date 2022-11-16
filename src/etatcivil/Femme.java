/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etatcivil;

import java.util.Calendar;

/**
 *
 * @author francois
 */
public class Femme extends Personne {

    public Femme(String nom, String prenom, Calendar naissance) {
        super(nom, prenom, naissance);
    }

    public Femme(Homme pere, Femme mere, String prenom, Calendar naissance) {
        super(pere, mere, prenom, naissance);
    }

    public Femme(Femme mere, String prenom, Calendar naissance) {
        super(mere, prenom, naissance);
    }
    
    
}
