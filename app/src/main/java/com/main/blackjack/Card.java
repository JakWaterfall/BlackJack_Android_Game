package com.main.blackjack;

import android.graphics.Bitmap;

public class Card {
    String suit;
    String value;
    Bitmap image;

    public Card(String suit, String value, Bitmap image) {
        this.suit = suit;
        this.value = value;
        this.image = image;
    }
}
