/*
 * Cours de remise a niveau Java.
 * UniversitÃ© de Franche-ComtÃ©.
 */
package etatcivil;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author yonas
 */
public class EtatCivilGUI extends JFrame implements ActionListener {

    public static final String QUITTER = "Quitter";
    public static final String OUVRIR = "Ouvrir";
    public static final String ENREGISTRER = "Enregistrer";
    public static final String RECHERCHER_PERSONNE = "Rechercher une personne";
    JPanel panneauRecherche = new JPanel();

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case QUITTER:
                System.exit(0);
                break;
            case OUVRIR:
                ouvrir();
                break;
            case ENREGISTRER:
                enregistrer();
                break;
            case RECHERCHER_PERSONNE:
                this.getContentPane().add(panneauRecherche);
                this.setVisible(true);
                break;
        }
    }

    public void ouvrir() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Personne.relecturepopulation(fc.getSelectedFile().getAbsolutePath());
            JOptionPane.showMessageDialog(this, Personne.population.size() + " personnes ont Ã©tÃ© chargÃ©es");

        }
    }

    public void enregistrer() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            Personne.sauvegardePopulation(fc.getSelectedFile().getAbsolutePath());
            JOptionPane.showMessageDialog(this, Personne.population.size() + " personnes ont Ã©tÃ© enregistrÃ©es");
        }
    }

    EtatCivilGUI() {
        super("Logiciel d'Ã©tat civil");

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Fichier");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Ouvrirድድ");
        menu.add(menuItem);
        menuItem.addActionListener(this);
        menuItem = new JMenuItem("Enregistrer");
        menu.add(menuItem);
        menuItem.addActionListener(this);
        menuItem = new JMenuItem("Quitter");
        menu.add(menuItem);
        menuItem.addActionListener(this);

        menu = new JMenu("Edition");
        menuBar.add(menu);

        menu = new JMenu("Rechercher");
        menuBar.add(menu);

        JMenuItem freebase = new JMenuItem("Freebase import");
        this.setJMenuBar(menuBar);

        panneauRecherche = new JPanel();
        panneauRecherche.setLayout(new BorderLayout());

        JPanel panneauNord = new JPanel();
        panneauNord.add(new JLabel("Rechercher une personne"));
        panneauRecherche.add(panneauNord, BorderLayout.NORTH);
        JPanel panneauSud = new JPanel();
        JButton boutonRechercher = new JButton("Rechercher");
        panneauRecherche.add(boutonRechercher, BorderLayout.SOUTH);

        JPanel panneauRecherche = new JPanel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        EtatCivilGUI fenetre = new EtatCivilGUI();
    }

}
