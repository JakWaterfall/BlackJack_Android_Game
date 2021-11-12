package com.main.blackjack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    ArrayList<Card> deck = new ArrayList<Card>();
    String[] suit = {"Hearts", "Spades", "Clubs", "Diamonds"};
    String[] value = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "10", "10", "10"};
    int[] image;

    int k = 0;
    int cWidth = 240;
    int cHeight = 290;

    Random rand = new Random();
    Context context;

    // Construct deck
    public Deck(Context context) {
        this.context = context;
        image = new int[]{R.drawable.card_hearts_a, R.drawable.card_hearts2, R.drawable.card_hearts3, R.drawable.card_hearts4, R.drawable.card_hearts5, R.drawable.card_hearts6,
                R.drawable.card_hearts7, R.drawable.card_hearts8, R.drawable.card_hearts9, R.drawable.card_hearts10, R.drawable.card_hearts_j, R.drawable.card_hearts_q, R.drawable.card_hearts_k,
                R.drawable.card_spades_a, R.drawable.card_spades2, R.drawable.card_spades3, R.drawable.card_spades4, R.drawable.card_spades5, R.drawable.card_spades6,
                R.drawable.card_spades7, R.drawable.card_spades8, R.drawable.card_spades9, R.drawable.card_spades10, R.drawable.card_spades_j, R.drawable.card_spades_q, R.drawable.card_spades_k,
                R.drawable.card_clubs_a, R.drawable.card_clubs2, R.drawable.card_clubs3, R.drawable.card_clubs4, R.drawable.card_clubs5, R.drawable.card_clubs6,
                R.drawable.card_clubs7, R.drawable.card_clubs8, R.drawable.card_clubs9, R.drawable.card_clubs10, R.drawable.card_clubs_j, R.drawable.card_clubs_q, R.drawable.card_clubs_k,
                R.drawable.card_diamonds_a, R.drawable.card_diamonds2, R.drawable.card_diamonds3, R.drawable.card_diamonds4, R.drawable.card_diamonds5, R.drawable.card_diamonds6,
                R.drawable.card_diamonds7, R.drawable.card_diamonds8, R.drawable.card_diamonds9, R.drawable.card_diamonds10, R.drawable.card_diamonds_j, R.drawable.card_diamonds_q, R.drawable.card_diamonds_k};
        reshuffleDeck();
    }
    public Card drawCard() {
        int r = rand.nextInt(deck.size());
        Card randCard = deck.get(r);
        deck.remove(r);
        return randCard;
    }
    public void reshuffleDeck() {
        k = 0;
        deck.clear();
        for(int i = 0; i < suit.length; i++) {
            for(int j = 0; j < value.length; j++) {
                deck.add(new Card(suit[i], value[j], Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), image[k]), cWidth, cHeight, true)));
                k++;
            }
        }
    }

}
