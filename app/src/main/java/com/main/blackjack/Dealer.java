package com.main.blackjack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import static com.main.blackjack.Player.cardSpaceing;

public class Dealer {

    ArrayList<Card> hand = new ArrayList<Card>();

    Deck deck;
    GameView2 GV;

    Bitmap cardBack;

    // DEALER PHYSICS
    int cardX;
    int cardXX;

    public Dealer(Deck deck, GameView2 GV){
        this.deck = deck;
        this.GV = GV;
        cardBack = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(deck.context.getResources(), R.drawable.cardback), deck.cWidth, deck.cHeight, true);
    }

    public void update(){
        if (cardX < cardSpaceing){
            cardX += 2;
        }
        if (cardXX < cardSpaceing){
            cardXX += 2;
        }
    }

    public void render(Canvas canvas, Paint paint){
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);

        // DRAW HAND
        for (int i = 0; i < hand.size(); i++) {
            if(cardXX >= cardSpaceing) {
                // IF NO NEW CARDS ARE DRAWN RENDER THIS (CARDXX IS SET TO 0 ON CARD DRAW THATS NOT FIRST ROUND).
                canvas.drawBitmap(hand.get(i).image, (i + 8) * cardX, 50, paint);
            } else {
                for (int j = 0; j < hand.size() - 1; j++) {
                    // IF A CARD IS DRAWN THEN RENDER THAT ARRAY WITH ONE LESS CARD TO GIVE APPEARANCE THAT ITS BEING DRAWN FROM THE DECK INSTEAD OF JUST POPPING IN.
                    canvas.drawBitmap(hand.get(j).image, (j + 8) * cardX, 50, paint);
                    canvas.drawBitmap(hand.get(hand.size() - 1).image, (hand.size() + 7) * cardXX, 50, paint);
                }
            }
        }

        //  DRAW CARD BACK TO COVER HIDDEN CARD
        if (!GV.stand && GV.standBtn.getText().toString().equalsIgnoreCase("Stand") && GV.player.hand.size() > 1) {
            canvas.drawBitmap(cardBack, 9 * cardX, 50, paint);
        }

        // DRAW SCORE
        if(hand.size() > 0 && !GV.stand) {
            canvas.drawText("Score: " + hand.get(0).value, 8 * cardSpaceing, 100 + deck.cHeight, paint);
        } else {
            if(cardXX >= cardSpaceing) {
                // RENDER IF NO CARD IS BEING DRAWN
                canvas.drawText("Score: " + GV.handValue(hand), 8 * cardSpaceing, 100 + deck.cHeight, paint);
            } else {
                // RENDER THE SCORE MINUS THE LAST LASTS VALUE WHILE ANIMATION OF CARD DRAW IS BEING PLAYED
                canvas.drawText("Score: " + (GV.handValue(hand) - lastCardValue(hand.get(hand.size()-1))), 8 * cardSpaceing, 100 + deck.cHeight, paint);
            }
        }
    }

    public void drawCard(){
        hand.add(deck.drawCard());
        if(GV.standBtn.getText().toString().equalsIgnoreCase("Stand")) {
            cardXX = -50;
        }
    }

    public void dealerAction() {
        //  TOO CAUSE THE DEALER TO DRAW EACH CARD
        while (true){
            if (cardXX >= cardSpaceing){
                if (GV.handValue(hand) <= 16) {
                    drawCard();
                    dealerAction();
                } else {
                    break;
                }
            }
        }
    }

    public int lastCardValue(Card card){
        int value = 0;
            if (card.value == "Ace" && GV.handValue(hand) < 11) {
                value += 11;
            } else if (card.value == "Ace" && GV.handValue(hand) >= 11) {
                value += 1;
            } else {
                value += Integer.parseInt(card.value);
            }
        return value;
    }
}
