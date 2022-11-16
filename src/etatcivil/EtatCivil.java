
package etatcivil;

import java.util.*;
        
public class EtatCivil {

    public static void main(String[] args) {
       
        Homme adam;
        Femme eve;
        Homme abel;
        Homme cain;
        Femme awan;
        Homme henoch;
        
        try {
            adam= new Homme("Dupont","Adam", new GregorianCalendar(-3000,1,1));
        
            eve= new Femme("Durand","Eve", new GregorianCalendar(-2999,1,1));

       // adam.decede(new GregorianCalendar(-2981,1,1));
        Mariage m= new Mariage(adam,eve,new GregorianCalendar(-2980,1,1),"Paradis");
        
        m.divorce(new GregorianCalendar(-2970,1,1));
        
        abel=new Homme(adam,eve,"Abel",new GregorianCalendar(-2980,8,10));
        cain=new Homme(adam,eve,"Cain",new GregorianCalendar(-2975,8,10));
        awan=new Femme("Lambert","Awan",new GregorianCalendar(-2974,9,30));


        henoch=new Homme(cain,awan,"HÃ©noch",new GregorianCalendar(-2924,1,10));
                
        m= new Mariage(awan,adam,new GregorianCalendar(-2950,1,1),"Paradis");
        
        henoch.arbreGenealogique(0);
        System.out.println(adam.estAncetre(henoch));
        System.out.println(eve.estAncetre(henoch));
        System.out.println(abel.estOncleOuTante(henoch));
        }
        catch (Exception e) {System.out.println(e);}
        
        Personne.sauvegardePopulation("bible.dat");
        //Personne.relecturePopulation("bible.dat");
        //System.out.println(Personne.population);
        
        
    }

}
