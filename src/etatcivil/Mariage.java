package etatcivil;

import java.util.*;
import java.io.*;

public class Mariage implements Serializable {

    public final static int POLYGYNIE = 1;
    /* nombre maxi de conjoints*/
    public final static boolean MARIAGE_HOMOSEXUEL = false;
    public final static int AGE_LEGAL_MARIAGE = 16;
    public final static int CONSANGUNITE = 4;

    private Personne epoux1;
    private Personne epoux2;
    private Calendar dateMariage;
    private String lieu;
    private Calendar dateDivorce;

    public Mariage(Personne epoux1, Personne epoux2, Calendar dateMariage, String lieu) {
        // on teste qu'ils ne sont pas deja mariÃ©s
        if (epoux1.nbMariagesActifs(dateMariage) >= POLYGYNIE) {
            throw new LegalException(epoux1.getPrenom() + " est deja mariÃ©");
        }
        if (epoux2.nbMariagesActifs(dateMariage) >= POLYGYNIE) {
            throw new LegalException(epoux2.getPrenom() + " est deja mariÃ©");
        }

        // on teste la consanguinite des epoux
        int niveauCousin = epoux1.estCousin(epoux2);
        if (niveauCousin >= 0 && niveauCousin <= CONSANGUNITE) {
            throw new LegalException("Mariage consanguin interdit");
        }

        // on teste si le mariage homosexuel est interdit et si les epoux sont de sexe different
        if (MARIAGE_HOMOSEXUEL == false
                && ((epoux1 instanceof Homme && epoux2 instanceof Homme)
                || (epoux1 instanceof Femme && epoux2 instanceof Femme))) {
            throw new LegalException("Mariage homosexuel interdit");
        }

        // on teste que les epoux doivent etre vivant Ã  la date du mariage
        if (epoux1.getDeces() != null && epoux1.getDeces().before(dateMariage)) {
            throw new LegalException("On ne peut pas Ã©pouser un mort !");
        }

        if (epoux2.getDeces() != null && epoux2.getDeces().before(dateMariage)) {
            throw new LegalException("On ne peut pas Ã©pouser un mort !");
        }

        // on teste que les epoux ont l'age legal
        Calendar d = (Calendar) epoux1.getNaissance().clone();
        d.add(Calendar.YEAR, AGE_LEGAL_MARIAGE);
        if (d.after(dateMariage)) {
            throw new LegalException(epoux1.getPrenom() + " est trop jeune");
        }
        d = (Calendar) epoux2.getNaissance().clone();
        d.add(Calendar.YEAR, AGE_LEGAL_MARIAGE);
        if (d.after(dateMariage)) {
            throw new LegalException(epoux2.getPrenom() + " est trop jeune");
        }

        this.epoux1 = epoux1;
        this.epoux2 = epoux2;
        this.dateMariage = dateMariage;
        this.lieu = lieu;
        epoux1.addMariage(this);
        epoux2.addMariage(this);
    }

    public Personne getEpoux1() {
        return epoux1;
    }

    public Personne getEpoux2() {
        return epoux2;
    }

    public Calendar getDateMariage() {
        return dateMariage;
    }

    public String getLieu() {
        return lieu;
    }

    public Calendar getDateDivorce() {
        return dateDivorce;
    }

    public void divorce(Calendar dateDivorce) {
        if (dateDivorce.before(dateMariage)) {
            throw new LegalException("un divorce ne peut Ãªtre prononcÃ© avant le mariage");
        }
        if (epoux1.getDeces() != null && epoux1.getDeces().before(dateDivorce)) {
            throw new LegalException("on ne divorce pas d'un mort");
        }
        if (epoux2.getDeces() != null && epoux2.getDeces().before(dateDivorce)) {
            throw new LegalException("on ne divorce pas d'un mort");
        }
        this.dateDivorce = dateDivorce;
    }
}
