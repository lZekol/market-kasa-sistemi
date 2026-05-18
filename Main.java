import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Market Kasa Sistemi — Ana Konsol Menüsü
 *
 * OOP Kavramları Kullanılan:
 *  ✅ Soyut Sınıf    → Urun (abstract)
 *  ✅ Kalıtım         → Gida, Elektronik, Temizlik extends Urun
 *  ✅ Arayüz          → Odeme (interface)
 *  ✅ Polimorfizm     → Nakit & Kart aynı odemeYap() metoduyla
 *  ✅ Kapsülleme      → private alanlar, getter/setter
 *  ✅ Kompozisyon     → Kasa içinde Sepet, Odeme, Musteri
 */
public class Main {

    static Scanner sc = new Scanner(System.in);
    static UrunYonetimi urunYonetimi       = new UrunYonetimi();
    static MusteriYonetimi musteriYonetimi = new MusteriYonetimi();
    static KampanyaYonetimi kampanyaYon    = new KampanyaYonetimi();
    static Kasa kasa                       = new Kasa(1);
    static Sepet sepet                     = new Sepet();
    static Musteri aktifMusteri            = null;

    public static void main(String[] args) {
        orneKVeriYukle();
        System.out.println("\n  ╔══════════════════════════════════════╗");
        System.out.println("  ║    MARKET KASA SİSTEMİ'NE HOŞGELDİN  ║");
        System.out.println("  ╚══════════════════════════════════════╝");

        boolean devam = true;
        while (devam) {
            anaMenu();
            int secim = intOku("  Seçiminiz: ");
            switch (secim) {
                case 1  -> urunMenusu();
                case 2  -> sepetMenusu();
                case 3  -> musteriMenusu();
                case 4  -> kampanyaMenusu();
                case 5  -> kasaMenusu();
                case 6  -> { kasa.gunlukRaporYazdir(); }
                case 0  -> { System.out.println("\n  Güle güle! 👋"); devam = false; }
                default -> System.out.println("  ❌ Geçersiz seçim!");
            }
        }
        sc.close();
    }

    // ─── MENÜLER ────────────────────────────────────────────────────────────

    static void anaMenu() {
        System.out.println("\n  ════════════════════════════════════");
        System.out.printf("  Müşteri: %s%n",
                aktifMusteri != null ? aktifMusteri.tamAd() + " (" + aktifMusteri.getSadakatPuani() + " puan)" : "Misafir");
        System.out.printf("  Sepet: %d kalem | %.2f₺%n",
                sepet.getUrunler().size(), sepet.genelToplam());
        System.out.println("  ════════════════════════════════════");
        System.out.println("  1. Ürün İşlemleri");
        System.out.println("  2. Sepet İşlemleri");
        System.out.println("  3. Müşteri İşlemleri");
        System.out.println("  4. Kampanyalar");
        System.out.println("  5. Ödeme Al (Kasa)");
        System.out.println("  6. Günlük Rapor");
        System.out.println("  0. Çıkış");
        System.out.println("  ────────────────────────────────────");
    }

    static void urunMenusu() {
        System.out.println("\n  ── ÜRÜN İŞLEMLERİ ──");
        System.out.println("  1. Tüm ürünleri listele");
        System.out.println("  2. Ürün ara");
        System.out.println("  3. Kategoriye göre listele");
        System.out.println("  4. Yeni ürün ekle");
        System.out.println("  5. Ürün güncelle (fiyat/stok)");
        System.out.println("  6. Ürün sil");
        System.out.println("  7. Kritik stok uyarısı");
        System.out.println("  8. Ürüne indirim uygula");
        System.out.println("  0. Geri");
        int s = intOku("  Seçim: ");
        switch (s) {
            case 1 -> urunYonetimi.hepsiniListele();
            case 2 -> {
                String anahtar = strOku("  Aranacak kelime: ");
                List<Urun> sonuclar = urunYonetimi.ara(anahtar);
                if (sonuclar.isEmpty()) System.out.println("  Sonuç yok.");
                else sonuclar.forEach(u -> System.out.println("  " + u));
            }
            case 3 -> {
                System.out.println("  Kategoriler: Gıda / Elektronik / Temizlik");
                String kat = strOku("  Kategori: ");
                urunYonetimi.kategoriFiltre(kat).forEach(u -> System.out.println("  " + u));
            }
            case 4 -> yeniUrunEkle();
            case 5 -> urunGuncelle();
            case 6 -> {
                int id = intOku("  Silinecek ürün ID: ");
                urunYonetimi.sil(id);
            }
            case 7 -> {
                int esik = intOku("  Stok eşiği (örn: 5): ");
                List<Urun> kritik = urunYonetimi.kritikStok(esik);
                if (kritik.isEmpty()) System.out.println("  Kritik stoklu ürün yok.");
                else { System.out.println("  ⚠️  KRİTİK STOK UYARISI:"); kritik.forEach(u -> System.out.println("  " + u)); }
            }
            case 8 -> {
                int id = intOku("  Ürün ID: ");
                Urun u = urunYonetimi.bul(id);
                if (u == null) { System.out.println("  ❌ Ürün bulunamadı."); return; }
                double yuzde = doubleOku("  İndirim yüzdesi (0-100): ");
                u.setIndirimYuzdesi(yuzde);
                System.out.printf("  ✅ %s için %%%.0f indirim uygulandı.%n", u.getAd(), yuzde);
            }
            case 0 -> {}
            default -> System.out.println("  ❌ Geçersiz seçim.");
        }
    }

    static void yeniUrunEkle() {
        System.out.println("  Tür: 1-Gıda  2-Elektronik  3-Temizlik");
        int tur = intOku("  Tür seçin: ");
        String ad    = strOku("  Ürün adı: ");
        double fiyat = doubleOku("  Fiyat (₺): ");
        int stok     = intOku("  Stok adedi: ");

        switch (tur) {
            case 1 -> {
                String skt = strOku("  Son kullanma tarihi (GG.AA.YYYY): ");
                urunYonetimi.ekle(new Gida(ad, fiyat, stok, skt));
            }
            case 2 -> {
                String marka = strOku("  Marka: ");
                int garanti  = intOku("  Garanti (ay): ");
                urunYonetimi.ekle(new Elektronik(ad, fiyat, stok, marka, garanti));
            }
            case 3 -> {
                double hacim = doubleOku("  Hacim (ml): ");
                urunYonetimi.ekle(new Temizlik(ad, fiyat, stok, hacim));
            }
            default -> System.out.println("  ❌ Geçersiz tür.");
        }
    }

    static void urunGuncelle() {
        int id = intOku("  Güncellenecek ürün ID: ");
        Urun u = urunYonetimi.bul(id);
        if (u == null) { System.out.println("  ❌ Ürün bulunamadı."); return; }
        System.out.println("  Mevcut: " + u);
        System.out.println("  1. Fiyat güncelle");
        System.out.println("  2. Stok güncelle");
        System.out.println("  3. Ad güncelle");
        int s = intOku("  Seçim: ");
        switch (s) {
            case 1 -> { u.setFiyat(doubleOku("  Yeni fiyat: ")); System.out.println("  ✅ Fiyat güncellendi."); }
            case 2 -> { u.stokEkle(intOku("  Eklenecek stok: ")); System.out.println("  ✅ Stok güncellendi."); }
            case 3 -> { u.setAd(strOku("  Yeni ad: ")); System.out.println("  ✅ Ad güncellendi."); }
            default -> System.out.println("  ❌ Geçersiz seçim.");
        }
    }

    static void sepetMenusu() {
        System.out.println("\n  ── SEPET İŞLEMLERİ ──");
        sepet.yazdir();
        System.out.println("  1. Ürün ekle");
        System.out.println("  2. Ürün çıkar");
        System.out.println("  3. Kampanya kodu gir");
        System.out.println("  4. Kampanyayı kaldır");
        System.out.println("  0. Geri");
        int s = intOku("  Seçim: ");
        switch (s) {
            case 1 -> {
                urunYonetimi.hepsiniListele();
                int id   = intOku("  Ürün ID: ");
                Urun u   = urunYonetimi.bul(id);
                if (u == null) { System.out.println("  ❌ Ürün bulunamadı."); return; }
                int adet = intOku("  Adet: ");
                if (sepet.ekle(u, adet)) System.out.println("  ✅ Sepete eklendi.");
            }
            case 2 -> {
                int id = intOku("  Çıkarılacak ürün ID: ");
                if (sepet.cikar(id)) System.out.println("  ✅ Sepetten çıkarıldı.");
                else System.out.println("  ❌ Ürün sepette değil.");
            }
            case 3 -> {
                String kod = strOku("  Kampanya kodu: ");
                Kampanya k = kampanyaYon.bul(kod);
                if (k == null) System.out.println("  ❌ Kampanya bulunamadı.");
                else sepet.kampanyaUygula(k);
            }
            case 4 -> { sepet.kampanyaKaldir(); System.out.println("  Kampanya kaldırıldı."); }
            case 0 -> {}
        }
    }

    static void musteriMenusu() {
        System.out.println("\n  ── MÜŞTERİ İŞLEMLERİ ──");
        System.out.println("  1. Tüm müşterileri listele");
        System.out.println("  2. Müşteri bul (telefon)");
        System.out.println("  3. Yeni müşteri kaydet");
        System.out.println("  4. Aktif müşteri seç / değiştir");
        System.out.println("  5. Aktif müşteriyi kaldır (misafir)");
        System.out.println("  0. Geri");
        int s = intOku("  Seçim: ");
        switch (s) {
            case 1 -> musteriYonetimi.hepsiniListele();
            case 2 -> {
                String tel = strOku("  Telefon (05XXXXXXXXX): ");
                Optional<Musteri> m = musteriYonetimi.telefonIleBul(tel);
                m.ifPresentOrElse(
                        mu -> System.out.println("  " + mu),
                        ()  -> System.out.println("  ❌ Müşteri bulunamadı.")
                );
            }
            case 3 -> {
                String ad    = strOku("  Ad: ");
                String soyad = strOku("  Soyad: ");
                String tel   = strOku("  Telefon: ");
                Musteri yeni = new Musteri(ad, soyad, tel);
                musteriYonetimi.ekle(yeni);
            }
            case 4 -> {
                int id = intOku("  Müşteri ID: ");
                Musteri m = musteriYonetimi.bul(id);
                if (m == null) System.out.println("  ❌ Müşteri bulunamadı.");
                else { aktifMusteri = m; System.out.println("  ✅ Aktif müşteri: " + m.tamAd()); }
            }
            case 5 -> { aktifMusteri = null; System.out.println("  Misafir moduna geçildi."); }
            case 0 -> {}
        }
    }

    static void kampanyaMenusu() {
        System.out.println("\n  ── KAMPANYALAR ──");
        kampanyaYon.hepsiniListele();
        System.out.println("  1. Yeni kampanya ekle");
        System.out.println("  0. Geri");
        int s = intOku("  Seçim: ");
        if (s == 1) {
            String kod  = strOku("  Kod (örn: INDIRIM10): ");
            String acik = strOku("  Açıklama: ");
            System.out.println("  Tip: 1-Yüzde  2-Sabit TL  3-Min Tutar");
            int tip     = intOku("  Tip: ");
            double deger = doubleOku("  Değer (%  veya ₺): ");
            double min   = doubleOku("  Min sepet tutarı (0 = yok): ");
            int max      = intOku("  Max kullanım (0 = sınırsız): ");
            Kampanya.KampanyaTipi kt = switch (tip) {
                case 1  -> Kampanya.KampanyaTipi.YUZDE_INDIRIM;
                case 2  -> Kampanya.KampanyaTipi.SABIT_INDIRIM;
                default -> Kampanya.KampanyaTipi.MIN_TUTAR_INDIRIM;
            };
            kampanyaYon.ekle(new Kampanya(kod, acik, kt, deger, min, max));
            System.out.println("  ✅ Kampanya eklendi.");
        }
    }

    static void kasaMenusu() {
        if (sepet.bosmu()) {
            System.out.println("  ❌ Sepet boş! Önce ürün ekleyin.");
            return;
        }
        System.out.println("\n  ── ÖDEME AL ──");
        sepet.yazdir();

        // Puan kullanımı
        double puanIndirimi = 0;
        if (aktifMusteri != null && aktifMusteri.getSadakatPuani() > 0) {
            System.out.printf("  %s — %d sadakat puanınız var (her 100 puan = 10₺).%n",
                    aktifMusteri.tamAd(), aktifMusteri.getSadakatPuani());
            int kullan = intOku("  Kaç puan kullanmak istersiniz? (0 = hiç): ");
            if (kullan > 0) {
                puanIndirimi = aktifMusteri.puanKullan(kullan);
                System.out.printf("  ✅ %.2f₺ puan indirimi uygulandı.%n", puanIndirimi);
            }
        }

        double odenecek = sepet.genelToplam() - puanIndirimi;
        System.out.printf("  Ödenecek tutar: %.2f₺%n", odenecek);
        System.out.println("  Ödeme yöntemi: 1-Nakit  2-Kredi Kartı  3-Banka Kartı");
        int yt = intOku("  Seçim: ");

        Odeme odeme = null;
        switch (yt) {
            case 1 -> {
                double verilen = doubleOku("  Verilen para (₺): ");
                odeme = new Nakit(verilen);
            }
            case 2, 3 -> {
                String no     = strOku("  Kart No (16 hane): ");
                String sahip  = strOku("  Kart Sahibi: ");
                double limit  = doubleOku("  Kart Limiti (₺): ");
                try {
                    odeme = new Kart(no, sahip, limit, yt == 2);
                } catch (IllegalArgumentException e) {
                    System.out.println("  ❌ " + e.getMessage());
                    return;
                }
            }
            default -> { System.out.println("  ❌ Geçersiz seçim."); return; }
        }

        kasa.odemeAl(sepet, odeme, aktifMusteri);
    }

    // ─── ÖRNEK VERİ ─────────────────────────────────────────────────────────

    static void orneKVeriYukle() {
        // Ürünler
        urunYonetimi.ekle(new Gida("Sütaş Süt 1L",         18.50, 50, "01.06.2026"));
        urunYonetimi.ekle(new Gida("Ülker Çikolata",        12.90, 100,"31.12.2026", false, false));
        urunYonetimi.ekle(new Gida("Evyap Sabun",            8.75, 200,"31.12.2028"));
        urunYonetimi.ekle(new Elektronik("Samsung A15",   6499.00, 10, "Samsung", 24, "SM-A155F"));
        urunYonetimi.ekle(new Elektronik("TWS Kulaklık",   299.00, 30, "Xiaomi",  12));
        urunYonetimi.ekle(new Temizlik("Domestos 750ml",    32.90, 80, 750));
        urunYonetimi.ekle(new Temizlik("Persil 3kg",        89.00, 40, 3000, "Standart", true));

        // İndirim örneği
        Urun samsung = urunYonetimi.ara("Samsung").get(0);
        samsung.setIndirimYuzdesi(10);

        // Müşteriler
        musteriYonetimi.ekle(new Musteri("Zeki Enis ", "Öztürk", "05373956947"));
        musteriYonetimi.ekle(new Musteri("Fatma", "Kaya",  "05359876543"));
        musteriYonetimi.ekle(new Musteri("Ataberk", "Ergin",  "05347566543"));

        System.out.println("  ✅ Örnek veriler yüklendi.\n");
    }

    // ─── YARDIMCI METOTLAR ───────────────────────────────────────────────────

    static int intOku(String mesaj) {

        System.out.print(mesaj);
        while (!sc.hasNextInt()) { sc.next(); System.out.print(mesaj); }
        int v = sc.nextInt(); sc.nextLine();
        return v;
    }

    static double doubleOku(String mesaj) {
        System.out.print(mesaj);
        while (!sc.hasNextDouble()) { sc.next(); System.out.print(mesaj); }
        double v = sc.nextDouble(); sc.nextLine();
        return v;
    }

    static String strOku(String mesaj) {
        System.out.print(mesaj);
        return sc.nextLine().trim();
    }
}

