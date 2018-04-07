package com.gigabyte.tugasakhir.vertx.model;

import io.vertx.core.shareddata.Shareable;

import java.util.ArrayList;
import java.util.List;

public class QnA implements Shareable {
    private List<List<String>> questionSet;
    private List<String> qna;

    public List<List<String>> getQuestionSet() {
        setQuestion();
        return questionSet;
    }

    private void setQuestion() {
        questionSet = new ArrayList<>();

        qna = new ArrayList<>();
        qna.add(0, "Siapakah Presiden kedua Negara Indonesia?");
        qna.add(1, "Soeharto");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Siapakah manusia pertama yang menyentuh bulan?");
        qna.add(1, "Neil Armstrong");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Tuliskan nama ilmiah dari air putih!");
        qna.add(1, "H2O");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Siapakah Presiden Amerika Serikat Ke-16?");
        qna.add(1, "Abraham Lincoln");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Apakah nama dari gunung tertinggi?");
        qna.add(1, "Everest");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Negara mana yang memiliki populasi tertinggi?");
        qna.add(1, "Cina");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Nama planet ke-7 pada tata surya?");
        qna.add(1, "Uranus");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Apakah nama dari satelit buatan manusia pertama yang diluncurkan ke luar angkasa?");
        qna.add(1, "Sputnik");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Nama benua terbesar di dunia?");
        qna.add(1, "Asia");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Apakah nama ilmiah dari belerang?");
        qna.add(1, "Sulfur");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Apakah nama dari samudera terluas di dunia?");
        qna.add(1, "Pasifik");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Siapakah penemu dari gaya gravitasi?");
        qna.add(1, "Isaac Newton");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Siapakah penemu dari Teori Relativitas?");
        qna.add(1, "Albert Einstein");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Ibukota dari negara Italia?");
        qna.add(1, "Roma");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Ibukota dari negara Jepang?");
        qna.add(1, "Tokyo");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Agama dengan penganut terbanyak di dunia?");
        qna.add(1, "Kristen");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Dalam mitologi Aztec, apakah nama dari dewa matahari?");
        qna.add(1, "Tonatiuh");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Dalam mitologi Yunani, siapakah istri dari dewa Zeus?");
        qna.add(1, "Hera");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Apakah nama dari makam raja-raja Mesir kuno?");
        qna.add(1, "Piramida");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Ada berapakah satelit alami yang diketahui mengorbit planet Jupiter?");
        qna.add(1, "67");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Siapa penemu dari termometer?");
        qna.add(1, "Galileo Galilei");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Berapakah kecepatan cahaya dalam m/s? (Tuliskan hanya dengan angka)");
        qna.add(1, "299792458");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Pada tahun berapakah Barack Obama lahir?");
        qna.add(1, "1961");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Siapakah pemeran pertama Peter Parker dalam film Spider-Man?");
        qna.add(1, "Tobey Maguire");
        questionSet.add(qna);

        qna = new ArrayList<>();
        qna.add(0, "Apakah produk yang diambil oleh manusia dari serangga lebah?");
        qna.add(1, "Madu");
        questionSet.add(qna);
    }
}
