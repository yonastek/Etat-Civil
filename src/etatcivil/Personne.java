package etatcivil;

import java.util.*;
import java.io.*;

public abstract class Personne implements Comparable<Personne>, Serializable {

    private String nom;
    private String prenom;
    private Calendar naissance;
    private Calendar deces;
    private Homme pere;
    private Femme mere;
    private LinkedList<Mariage> mariages = new LinkedList<Mariage>();

    public static Collection<Personne> population = new TreeSet<Personne>();

    /**
     * permet de rechercher dans la population une personne dont on connait le
     * prénom et le nom
     */
    public static Personne recherchePersonne(String nom, String prenom) {
        for (Personne p : population) {
            if (p.getNom().equals(nom) && p.getPrenom().equals(prenom)) {
                return p;
            }
        }
        return null;
    }

    public void addMariage(Mariage m) {
        mariages.add(m);
    }

    public static void sauvegardePopulation(String s) {
        try {
            FileOutputStream f = new FileOutputStream(new File(s));
            ObjectOutputStream oos = new ObjectOutputStream(f);
            oos.writeObject(population);
            oos.close();
        } catch (Exception e) {
            System.out.println("Erreur " + e);
        }
    }

    public static void relecturePopulation(String s) {
        try {
            FileInputStream f = new FileInputStream(new File(s));
            ObjectInputStream oos = new ObjectInputStream(f);
            population = (Collection) oos.readObject();
            oos.close();
        } catch (Exception e) {
            System.out.println("Erreur " + e);
        }
    }

    /**
     * nbMariagesActifs indique le nombre de conjoints de la personne Ã  une
     * date donnÃ©e
     */
    public int nbMariagesActifs(Calendar date) {
        int nbMariageActifs = 0;
        for (Mariage m : mariages) {    // on parcourt la liste des mariages
            /* pour chaque mariage, on teste si le mariage a eu lieu
           avant la date considÃ©rÃ©e (passÃ©e en paramÃ¨tre)
             */
            if (m.getDateMariage().before(date)) {/* si le mariage est en cours ou si le divorce a eu lieu
                aprÃ¨s la date considÃ©rÃ©e*/
                if (m.getDateDivorce() == null
                        || m.getDateDivorce().after(date)) /* on teste si l'autre Ã©poux est dÃ©cÃ©dÃ© apres la date considÃ©rÃ©e*/ {
                    if (m.getEpoux1() != this) {
                        if (m.getEpoux1().deces == null
                                || m.getEpoux1().deces.after(date)) {
                            nbMariageActifs = nbMariageActifs + 1;
                        }
                    } else {
                        if (m.getEpoux2().deces == null
                                || m.getEpoux2().deces.after(date)) {
                            nbMariageActifs = nbMariageActifs + 1;
                        }
                    }
                }
            }
        }
        return nbMariageActifs;
    }

    public Personne(String nom, String prenom, Calendar naissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.naissance = naissance;
        population.add(this);
    }

    public Personne(Homme pere, Femme mere, String prenom, Calendar naissance)
            throws BiologiqueException {
        if (pere.estFertile(naissance)
                && mere.estFertile(naissance)) {
            this.nom = pere.getNom();
            this.prenom = prenom;
            this.naissance = naissance;
            this.pere = pere;
            this.mere = mere;
        } else {
            throw new BiologiqueException("un des parents ne peut pas avoir d'enfant Ã  cette date");
        }
        population.add(this);
    }

    public Personne(Femme mere, String prenom, Calendar naissance)
            throws BiologiqueException {
        if (mere.estFertile(naissance)) {
            this.nom = mere.getNom();
            this.prenom = prenom;
            this.naissance = naissance;
            this.mere = mere;
        } else {
            throw new BiologiqueException("la mere ne peut pas avoir d'enfant Ã  cette date");
        }
        population.add(this);
    }

    public Calendar getDeces() {
        return deces;
    }

    public boolean estFertile(Calendar date) {
        if (deces != null) //une femme ne peut pas avoir d'enfant aprÃ¨s sa mort   
        {
            if (this instanceof Femme
                    && deces.before(date)) {
                return false;
            } else if (this instanceof Homme) //un homme ne peut pas avoir d'enfant 9 mois aprÃ¨s sa mort   
            {
                Calendar d = (Calendar) deces.clone();
                d.add(Calendar.MONTH, 9);
                if (d.before(date)) {
                    return false;
                }
            }
        }
        Calendar dn = (Calendar) naissance.clone();
        dn.add(Calendar.YEAR, 12);
        //il faut avoir au moins 12 ans pour avoir des enfants
        if (date.before(dn)) {
            return false;
        }
        if (this instanceof Femme) {
            //une femme ne peut pas avoir d'enfant aprÃ¨s 60ans
            Calendar dm = (Calendar) naissance.clone();
            dm.add(Calendar.YEAR, 60);
            if (date.after(dm)) {
                return false;
            }
        }
        return true;
    }

    public Personne() {
        nom = "";
        prenom = "";
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Homme getPere() {
        return pere;
    }

    public Femme getMere() {
        return mere;
    }

    public Calendar getNaissance() {
        return naissance;
    }

    public void decede(Calendar dateDeces)
            throws BiologiqueException {
        if (deces != null) {
            throw new BiologiqueException(nom + " " + prenom + " est dÃ©ja dÃ©cÃ©dÃ© le " + deces);
        } else if (naissance.after(dateDeces)) {
            throw new BiologiqueException(nom + " " + prenom + " ne peut pas dÃ©cÃ©der avant d'Ãªtre nÃ©");
        } else {
            deces = dateDeces;
        }
    }

    public boolean estParent(Personne p) {
        if (p == null) {
            return false;
        } else {
            return (this == p.pere || this == p.mere);
        }
    }

    public boolean estGrandParent(Personne p) {
        return estParent(p.pere) || estParent(p.mere);
    }

    /**
     * @return le nombre de gÃ©nÃ©ration entre les personnes ou -1 si il
     * n'existe pas de relation d'ancetre
     */
    public int estAncetre(Personne p) {
        if (this == p) {
            return 0;
        }
        if (p.pere == null && p.mere == null) {
            return -1;
        }
        int ancetrePere = estAncetre(p.pere);
        if (ancetrePere >= 0) {
            return ancetrePere + 1;
        }
        int ancetreMere = estAncetre(p.mere);
        if (ancetreMere >= 0) {
            return ancetreMere + 1;
        }
        return -1;
    }

    public boolean estFrereOuSoeur(Personne p) {
        return (this.pere != null && this.pere == p.pere
                && this.mere != null && this.mere == p.mere);
    }

    public boolean estOncleOuTante(Personne p) {
        return estFrereOuSoeur(p.pere) || estFrereOuSoeur(p.mere);
    }

    /**
     * Cousins : personnes ayant un ancetre commun
     *
     * @param p
     * @return le nombre de gÃ©nÃ©ration de l'ancetre commun ou -1 si il
     * n'existe pas de relation de cousin
     */
    public int estCousin(Personne p) {
        if (this == p) {
            return 0;
        }
        if (p == null) {
            return -1;
        }
        int na = this.estAncetre(p);
        if (na >= 0) {
            return na;
        }
        int nb = p.estAncetre(this);
        if (nb >= 0) {
            return nb;
        }
        if (p.pere == null && p.mere == null) {
            return -1;
        }
        if (this.pere != null) // a-t-on un pere ?
        {
            int np = this.pere.estAncetre(p); // est il un ancetre de p
            if (np >= 0) {
                return np;
            } else {
                np = this.pere.estCousin(p);// est-il un cousin de p
                if (np >= 0) {
                    return np;
                }
            }
        }
        if (this.mere != null) {
            int nm = this.mere.estAncetre(p);
            if (nm >= 0) {
                return nm;
            } else {
                nm = this.mere.estCousin(p);
                if (nm >= 0) {
                    return nm;
                }
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return prenom + " " + nom.toUpperCase();
    }

    public void bonjour() {
        System.out.println("Bonjour !");
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.nom);
        hash = 79 * hash + Objects.hashCode(this.prenom);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Personne other = (Personne) obj;
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.prenom, other.prenom)) {
            return false;
        }
        if (this.naissance != other.naissance) {
            return false;
        }
        if (this.pere != other.pere) {
            return false;
        }
        if (this.mere != other.mere) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Personne o) {
        if (this.equals(o)) {
            return 0;
        } else if (this.naissance.compareTo(o.naissance) == 0) {
            if (this.nom.compareTo(o.nom) == 0) {
                return this.prenom.compareTo(o.prenom);
            } else {
                return this.nom.compareTo(o.nom);
            }
        } else {
            return this.naissance.compareTo(o.naissance);
        }
    }

    public void arbreGenealogique(int generation) {
        for (int i = 0; i < generation; i++) {
            System.out.print("   ");
        }
        System.out.println(this);
        if (pere != null) {
            pere.arbreGenealogique(generation + 1);
        }
        if (mere != null) {
            mere.arbreGenealogique(generation + 1);
        }
    }
}
