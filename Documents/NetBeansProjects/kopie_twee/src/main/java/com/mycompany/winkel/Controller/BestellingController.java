/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.winkel.Controller;


import com.mycompany.winkel.DAOGenerics.GenericDaoImpl;
import com.mycompany.winkel.DAOs.ArtikelDao;
import com.mycompany.winkel.DAOs.BestellingDao;
import com.mycompany.winkel.POJO.BestellingArtikel;
import com.mycompany.winkel.POJO.Bestelling;
import com.mycompany.winkel.POJO.BestellingArtikelId;
import com.mycompany.winkel.POJO.Klant;
import com.mycompany.winkel.POJO.Artikel;
import com.mycompany.winkel.View.BestellingView;
import com.mycompany.winkel.View.KlantView;
import com.mycompany.winkel.View.ArtikelView;
import com.mycompany.winkel.DAOGenerics.GenericDaoInterface;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 *
 * @author Excen
 */

@Component
public class BestellingController {
    
    BestellingController(){
        
    }
    
    // Data fields
    private static final Logger logger = (Logger) LoggerFactory.getLogger("com.webshop");
    private static final Logger errorLogger = (Logger) LoggerFactory.getLogger("com.webshop.err");
    private static final Logger testLogger = (Logger) LoggerFactory.getLogger("com.webshop.test");
    
    
    GenericDaoImpl<Bestelling, Long> bestellingDao; 
    GenericDaoImpl<Artikel, Long> artikelDao;
    GenericDaoImpl<Klant, Long> klantDao;
//    BestellingArtikelDao bestellingArtikelDao = new BestellingArtikelDao();
    
    BestellingView bestellingView; 
    ArtikelView artikelView; 
    KlantView klantView;
    // BestellingArtikelView?
    
    Scanner scanner = new Scanner(System.in);
    int userInput;
    Long artikelKeuze;
    int artikelAantal;
    
    
        
    public void bestellingMenu() {
        
        userInput = bestellingView.startBestellingMenu();
        
        switch (userInput) {
                // bestelling plaatsen
            case 1:
                plaatsBestelling();                   
                break;  
                
                // bestellinginfo ophalen
            case 2:      
                haalBestellingInfoOp();
                break;
                
                // Bestelling wijzigen
            case 3:
                wijzigBestelling();
                break;
                
                // Bestelling verwijderen
            case 4:
                verwijderBestelling();
                break;
                
                // Alle bestellingen tonen
            case 5:
                toonAlleBestellingen();       
                break;
                
                // verijder alle bestellingen
            case 6:
                verwijderAlleBestellingen();    
                break;
                
                // terug naar hoofdmenu
            case 7:
                terugNaarHoofdMenu();    
                break;
                    
                // default        
            default:    
                break;
        }
        terugNaarHoofdMenu();
    }
    
    public void terugNaarHoofdMenu() {
        HoofdMenuController hoofdMenu = new HoofdMenuController();
        hoofdMenu.start();
    }
    
    public void plaatsBestelling() {            

       
        // Bestaande klant opzoeken en toevoegen aan bestelling
        long klantId = bestellingView.voerKlantIdIn();
        Klant klant = (Klant) session.get(Klant.class, klantId);
        
        Bestelling bestelling = new Bestelling();
        bestelling.setKlant(klant);
        bestelling.setDatum(new java.util.Date());
       
        // Tonen beschikbare artikellen
        
        System.out.println("Beschikbare artikellen:");
        
        ArrayList<Artikel> artikelLijst = (ArrayList<Artikel>)artikelDao.readAll(Artikel.class);
        
        bestellingView.printArtikelLijst(artikelLijst);

        // BestellingArtikel toevoeg loop

        int anotherOne = 0;
        boolean checker = true;
        
        do{
            try{
                
            // welk artikel
            artikelKeuze = bestellingView.voerArtikelIdIn();
            
            // hoe vaak
            artikelAantal = bestellingView.voerAantalIn();
            
            }catch (InputMismatchException ex){
                System.out.println("Voer een integer in.");
            }
            
            Artikel artikel = (Artikel) session.get(Artikel.class, artikelKeuze);
            
            BestellingArtikel bestellingArtikel = new BestellingArtikel();
            bestellingArtikel.setArtikelAantal(artikelAantal);
            bestellingArtikel.setArtikel(artikel);
            
            BestellingArtikelId bestellingArtikelId = new BestellingArtikelId();
            bestellingArtikelId.setArtikel(artikel);
            bestellingArtikelId.setBestelling(bestelling);
            bestellingArtikel.setPk(bestellingArtikelId);
            
            bestelling.getBestellingArtikellen().add(bestellingArtikel);
            
            System.out.println("Wilt u nog een artikel aan de bestelling toevoegen?\n1 - Ja\n2 - Nee");
            anotherOne = scanner.nextInt();
            
            if (anotherOne == 1){
                checker = true;
            }
            else {
                checker = false;
            }
 
        }while(checker);

        // DB insert
        
        Long bestellingId = (Long)bestellingDao.insert(bestelling);
        System.out.println("Uw bestelling is toegevoegd en met bestellingId: " + bestellingId);
        
    }
    
    public void haalBestellingInfoOp() {
        
        
        int zoekKeuze;
        
        zoekKeuze = bestellingView.hoeWiltUZoeken();
        
        if (zoekKeuze == 1){
        
        
        Long bestellingId;
        bestellingId = bestellingView.voerBestellingIdIn();
        
        Bestelling bestelling = (Bestelling) session.get(Bestelling.class, bestellingId);
        bestellingView.printBestellingInfo(bestelling);
        System.out.println("---");
        Set <BestellingArtikel> artikelLijst = bestelling.getBestellingArtikellen();
            System.out.println("Artikellen in Bestelling " + bestellingId + ":");
        for (BestellingArtikel BS: artikelLijst){
            System.out.println(BS.getArtikel().getId() + " - " + BS.getArtikel().getArtikelNaam() + ": " + BS.getArtikelAantal() + " keer");
        }
       
        
        }
        else if (zoekKeuze == 2){
        
        
        Long klantId;
        klantId = bestellingView.voerKlantIdIn();
        
        System.out.println("De bestellingen van Klant ID: " + klantId + " zijn:");
        Klant klant = (Klant) session.get(Klant.class, klantId);
        Set <Bestelling> bestellingen = klant.getBestellingen();
        for (Bestelling best: bestellingen){
            System.out.println("Bestelling ID: " + best.getId());
        }
        
        
       
        }
        else{
        
            System.out.println("U gaat terug naar BestellingMenu");    
            
        }
 
    }
    
    public void wijzigBestelling() {
        
        Bestelling bestelling;
        boolean checker = false;
        Long bestellingId;
       
        // Bestelling ophalen
        
        bestellingId = bestellingView.voerBestellingIdIn();
        
        bestelling = (Bestelling)session.get(Bestelling.class, bestellingId);
        bestellingView.printBestellingInfo(bestelling);
        
        // Artikellen in bestelling tonen
        Set <BestellingArtikel> artikelLijst = bestelling.getBestellingArtikellen();
            System.out.println("Artikellen in Bestelling " + bestellingId + ":");
        for (BestellingArtikel BS: artikelLijst){
            System.out.println(BS.getArtikel().getId() + " - " + BS.getArtikel().getArtikelNaam() + ": " + BS.getArtikelAantal() + " keer");
        }
        
       
        // keuze geven welk artikel aangepast moeten worden
        
        do {
        
        int artikelKeus;
        int verwijderWijzig;
            
        artikelKeus = bestellingView.voerArtikelId();
        verwijderWijzig = bestellingView.wijzigBestellingKeuze();
        
        if (verwijderWijzig == 1){
            // artikel uit bestelling verwijderen
            
           
            Bestelling wijzigBestelling;
            wijzigBestelling = (Bestelling) session.get(Bestelling.class, bestellingId);
            Set<BestellingArtikel>bestellingArtikellen;
            Set<BestellingArtikel>bestellingArtikellenClone = new HashSet<>();
            bestellingArtikellen = (Set<BestellingArtikel>) wijzigBestelling.getBestellingArtikellen();
            
            // 2 verschillende sets, 1 (bestellingArtikellen) om de te verwijderen bestellingartikellen 
            // toe te voegen aan de ander (bestellingArtikellenClone) en vervolgens ook uit de orignele set zelf
            for (BestellingArtikel BS: bestellingArtikellen){
                if(BS.getArtikel().getId() == artikelKeus){
                    bestellingArtikellenClone.add(BS);
                }
            }
            
            for (BestellingArtikel BSC: bestellingArtikellenClone){
                session.delete(BSC);
                bestellingArtikellen.remove(BSC);
            }

           
            
            // Extra check om te kijken of de bestelling leeg is, zoja word ie verwijderd
            if (bestellingArtikellen.isEmpty()){
                
                System.out.println("Bestelleng ID: " + bestellingId + " heeft geen artikellen meer, bestelling word verwijderd");
                session.delete(bestelling);
                
            }
                   
        }
        
        else if (verwijderWijzig == 2){
            // artikel aantal wijzigen
            
            int nieuwAantal;
          
            
            Bestelling wijzigBestelling;
            wijzigBestelling = (Bestelling) session.get(Bestelling.class, bestellingId);
            Set<BestellingArtikel>bestellingArtikellen;
            bestellingArtikellen = (Set<BestellingArtikel>) wijzigBestelling.getBestellingArtikellen();
            
            
            nieuwAantal = bestellingView.wijzigAantal();
            
            for (BestellingArtikel BS: bestellingArtikellen){
                if (BS.getArtikel().getId() == artikelKeus){
                    
                    BS.setArtikelAantal(nieuwAantal);
                    session.update(BS);
                    
                    // nog een sout met nieuwe artikel info
                }
            }
            
        }
        
        checker = bestellingView.nogEenArtikelWijzigen();
        
        }while(checker);
        
        
        // Aantal wijzigen of verwijderen?
        
        // Optioneel: nog een artikel
        
        // Done
        
        
        
        
        
        
        
    }
    
    public void verwijderBestelling() {
        
       
        boolean deleted = false;
        Long bestellingId;
        
        ArrayList<Bestelling> bestellingLijst = (ArrayList<Bestelling>) bestellingDao.readAll(Bestelling.class);
        bestellingView.printBestellingLijst(bestellingLijst);
        System.out.println("Welke bestelling wilt u verwijderen?");
        bestellingId = bestellingView.voerBestellingIdIn();
        deleted = bestellingDao.deleteById(bestellingId);
        System.out.println("Verwijderprocess succes:" + deleted);
        
        System.out.println(bestellingId + " is verwijderd."); 
        
        bestellingMenu();        
    }
    

    public void toonAlleBestellingen() {
        ArrayList<Bestelling> bestellingLijst = (ArrayList<Bestelling>) bestellingDao.readAll(Bestelling.class);
        bestellingView.printBestellingLijst(bestellingLijst);
        
    }
    
    public void verwijderAlleBestellingen() {
        
      
        int verwijderConfirmatie = bestellingView.verwijderConfirmatie();
        
        if (verwijderConfirmatie == 1){
            
            bestellingDao.deleteAll(Bestelling.class);
            
            System.out.println("Alle bestellingen zijn verwijderd.\n");  
        }
        
        else {
            System.out.println("Verwijderen afgebroken.\n");
        }
        
    }
    
    // Optionele methoden
    
    public BestellingArtikel createBestellingArtikel(){
        
        long artikelId = bestellingView.voerArtikelIdIn();
        int artikelAantal = bestellingView.voerAantalIn();
                
        BestellingArtikel BS = new BestellingArtikel();
        // BS.setArtikelId(artikelId);
        BS.setArtikelAantal(artikelAantal);        
        
        return BS;
        
    }
  
}

