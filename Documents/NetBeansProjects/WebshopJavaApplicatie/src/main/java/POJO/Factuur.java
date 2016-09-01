/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package POJO;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import static javax.persistence.GenerationType.AUTO;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Excen
 */
@Entity
@Table (name = "FACTUUR")
public class Factuur implements Serializable {   
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "FACTUUR_ID")
    private long Id;
    
    private String factuurnummer;
    
    @Temporal(TemporalType.DATE)
    private java.util.Date factuurdatum;
    
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "KLANT_ID")
    private Klant klant; 
    
    
    @OneToMany(mappedBy = "factuur")
    private Set<Betaling> betalingset;
    
    @OneToOne (fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    protected Bestelling bestelling;
    
    
    
    public long getId() {
        return this.Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public Klant getKlant() {
        return this.klant;
    }

    public void setKlant(Klant klant) {
        this.klant = klant;
    }
    
    public String getFactuurnummer() {
        return this.factuurnummer;
    }

    public void setFactuurnummer(String factuurnummer) {
        this.factuurnummer = factuurnummer;
    }

    public Date getFactuurdatum() {
        return this.factuurdatum;
    }

    public void setFactuurdatum(Date factuurdatum) {
        this.factuurdatum = factuurdatum;
    }

    public Set<Betaling> getBetalingset() {
        return this.betalingset;
    }

    public void setBetalingset(Set<Betaling> betalingset) {
        this.betalingset = betalingset;
    }

    public Bestelling getBestelling() {
        return this.bestelling;
    }

    public void setBestelling(Bestelling bestelling) {
        this.bestelling = bestelling;
    }
    
    
    
    
    
}
