package OVChipkaart;

import Reiziger.Reiziger;
import Product.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OVChipkaart {
    private int kaartnummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;
    private List<Product> producten;

    public OVChipkaart(int kaartnummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.kaartnummer = kaartnummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
        this.producten = new ArrayList<>();
    }

    public int getKaartnummer() {
        return kaartnummer;
    }

    public void setKaartnummer(int kaartnummer) {
        this.kaartnummer = kaartnummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public String toString() {
        return "OVChipkaart {kaartnummer=" + kaartnummer + ", geldigTot=" + geldigTot + ", klasse=" + klasse + ", saldo=" + saldo + ", reiziger=" + reiziger.getId() + "}";
    }
}

