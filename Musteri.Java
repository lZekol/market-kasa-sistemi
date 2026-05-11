import java.util.ArrayList;
import java.util.List;

/**
 * Müşteri bilgileri ve sadakat puan sistemi.
 */
public class Musteri {
    private static int sayac = 100;

    private final int id;
    private String ad;
    private String soyad;
    private String telefon;
    private String email;
    private int sadakatPuani;
    private double toplamHarcama;
    private List<String> alisverisGecmisi;
    private String uyelikSeviyesi; // Bronze, Silver, Gold, Platinum

    public Musteri(String ad, String soyad, String telefon) {
        this.id = ++sayac;
        this.ad = ad;
        this.soyad = soyad;
        this.telefon = telefon;
        this.email = "";
        this.sadakatPuani = 0;
        this.toplamHarcama = 0;
        this.alisverisGecmisi = new ArrayList<>();
        this.uyelikSeviyesi = "Bronze";
    }

    /**
     * Alışveriş sonrası puan ve harcama güncelle.
     * Her 10₺ = 1 puan
     */
    public void alisverisYap(double tutar, String fis) {
        toplamHarcama += tutar;
        int kazanilanPuan = (int)(tutar / 10);
        sadakatPuani += kazanilanPuan;
        alisverisGecmisi.add(fis);
        uyelikGuncelle();
    }

    /**
     * Puan kullan (100 puan = 10₺ indirim)
     * @return kullanılan puanın TL karşılığı
     */
    public double puanKullan(int puan) {
        if (puan > sadakatPuani) {
            System.out.println("  ❌ Yetersiz puan! Mevcut: " + sadakatPuani);
            return 0;
        }
        sadakatPuani -= puan;
        return puan * 0.10; // 1 puan = 0.10₺
    }

    private void uyelikGuncelle() {
        if      (toplamHarcama >= 5000) uyelikSeviyesi = "Platinum";
        else if (toplamHarcama >= 2000) uyelikSeviyesi = "Gold";
        else if (toplamHarcama >= 500)  uyelikSeviyesi = "Silver";
        else                             uyelikSeviyesi = "Bronze";
    }

    public String tamAd()       { return ad + " " + soyad; }
    public int getId()          { return id; }
    public String getAd()       { return ad; }
    public void setAd(String a) { this.ad = a; }
    public String getSoyad()    { return soyad; }
    public String getTelefon()  { return telefon; }
    public void setTelefon(String t) { this.telefon = t; }
    public String getEmail()    { return email; }
    public void setEmail(String e) { this.email = e; }
    public int getSadakatPuani(){ return sadakatPuani; }
    public double getToplamHarcama() { return toplamHarcama; }
    public String getUyelikSeviyesi(){ return uyelikSeviyesi; }
    public List<String> getAlisverisGecmisi() { return alisverisGecmisi; }

    @Override
    public String toString() {
        return String.format("[%d] %-20s | Tel: %-14s | Puan: %4d | %s | Toplam: %.2f₺",
                id, tamAd(), telefon, sadakatPuani, uyelikSeviyesi, toplamHarcama);
    }

