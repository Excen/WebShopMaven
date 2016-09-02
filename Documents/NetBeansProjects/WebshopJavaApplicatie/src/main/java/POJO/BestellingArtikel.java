/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POJO;

import java.io.Serializable;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Excen
 */

@Entity
@Table (name ="BESTELLINGARTIKEL")
@AssociationOverrides({
		@AssociationOverride(name = "pk.bestelling",
			joinColumns = @JoinColumn(name = "BESTELLING_ID")),
		@AssociationOverride(name = "pk.artikel",
			joinColumns = @JoinColumn(name = "ARTIKEL_ID")) })



public class BestellingArtikel implements Serializable {

    @EmbeddedId
    private BestellingArtikelId pk = new BestellingArtikelId();
    
    @Column (nullable = false) 
    private int artikelAantal;
    
    @ManyToOne
    @JoinColumn (name = "ARTIKEL_ID", insertable = false, updatable = false)
    private Artikel artikel;
    
    @ManyToOne
    protected Bestelling bestelling;
    
   
    
    
    // Constructor
    public BestellingArtikel(Artikel artikel, Bestelling bestelling, int artikelAantal){        
        this.artikel = artikel;
        this.bestelling = bestelling;
        this.artikelAantal = artikelAantal;
    }
    
    
    
    public BestellingArtikel(){
        
    }    
  

    /**
     * @return the artikelAantal
     */
    public int getArtikelAantal() {
        return artikelAantal;
    }

    /**
     * @param artikelAantal the artikelAantal to set
     */
    public void setArtikelAantal(int artikelAantal) {
        this.artikelAantal = artikelAantal;
    }
    
    
    
    
    
    
}
