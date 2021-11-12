package com.main.blackjack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class Player {

    ArrayList<Card> hand = new ArrayList<Card>();
    ArrayList<Card> handSplit = new ArrayList<Card>();

    int chips = 1000;
    int bet = 0;
    int betSplit = 0;

    int cWidth;
    int cHeight;

    public static final int cardSpaceing = 60;

    Bitmap downArrow;

    Deck deck;
    GameView2 GV;

    //PHYSICS
    int arrowY = 1050;
    int arrowVel = 1;

    int cardX;
    int cardXX = cardSpaceing;
    int cardXXX = cardSpaceing;
    int cardXXXX = cardSpaceing;
    int cardXXXXX = cardSpaceing;

    public Player(Deck deck, GameView2 GV) {
        this.deck = deck;
        this.GV = GV;
        this.cWidth = deck.cWidth;
        this.cHeight = deck.cHeight;
        downArrow = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(deck.context.getResources(), R.drawable.arrow_down), cWidth, cHeight, true);

    }

    public void update(){

        if(cardX < cardSpaceing){
            cardX += 2;
        }
        if(cardXX < cardSpaceing) {
            cardXX += 2;
        }
        if(cardXXX < cardSpaceing) {
            cardXXX += 2;
        }
        if(cardXXXX > cardSpaceing) {
            cardXXXX -= 8;
        }
        if(cardXXXXX < cardSpaceing) {
            cardXXXXX += 1;
        }


            // ARROW PHYSICS
        if(GV.leftHand || GV.rightHand){
            arrowY += arrowVel;
            if (arrowY <= 1000){
                arrowVel = 1;
            }
            if(arrowY >= 1050){
                arrowVel = -1;
            }
        }
    }

    public void render(Canvas canvas, Paint paint){
        paint.setTextSize(40);
        paint.setColor(Color.WHITE);
        // RENDER SINGLE HAND
        if (handSplit.size() < 1) {

            for (int i = 0; i < hand.size(); i++) {
                if(cardXX >= cardSpaceing) {
                    // IF NO NEW CARDS ARE DRAWN RENDER THIS (CARDXX IS SET TO 0 ON CARD DRAW THATS NOT FIRST ROUND).
                    canvas.drawBitmap(hand.get(i).image, (i + 8) * cardX, 1050, paint);
                }else{
                    for (int j = 0; j < hand.size() - 1; j++) {
                        // IF A CARD IS DRAWN THEN RENDER THAT ARRAY WITH ONE LESS CARD TO GIVE APPEARANCE THAT ITS BEING DRAWN FROM THE DECK INSTEAD OF JUST POPPING IN.
                        canvas.drawBitmap(hand.get(j).image, (j + 8) * cardX, 1050, paint);
                        // RENDER THAT LAST CARD IN THE ARRAY SEPARATELY MOVING IT ACROSS THE SCREEN TO GIVE THE APPEARANCE THAT IT IS BEING DRAWN FROM THE DECK.
                        canvas.drawBitmap(hand.get(hand.size() - 1).image, (hand.size() + 7) * cardXX, 1050, paint);
                    }
                }
            }
                if(cardXX >= cardSpaceing) {
                    // RENDER IF NO CARD IS BEING DRAWN
                    canvas.drawText("Score: " + GV.handValue(hand), 8 * cardSpaceing, 1100 + cHeight, paint);
                } else {
                    // RENDER THE SCORE MINUS THE LAST LASTS VALUE WHILE ANIMATION OF CARD DRAW IS BEING PLAYED
                    canvas.drawText("Score: " + (GV.handValue(hand) - lastCardValue(hand.get(hand.size()-1))), 8 * cardSpaceing, 1100 + deck.cHeight, paint);
                }
            canvas.drawText("Bet: " + bet, 8 * cardSpaceing, 1150 + cHeight, paint);

        } else {

            // RENDER TWO HANDs
            if(cardXXXX > cardSpaceing){
                // RENDERS THE SPLITTING OF THE CARDS BEFORE RENDERING THE OTHER TO CARDS
                splitPhysics(canvas, paint);

            }else {

                // norm hand
                for (int i = 0; i < hand.size(); i++) {
                    if (cardXX >= cardSpaceing) {
                        // IF NO NEW CARDS ARE DRAWN RENDER THIS (CARDXX IS SET TO 0 ON CARD DRAW THATS NOT FIRST ROUND).
                        canvas.drawBitmap(hand.get(i).image, (i + 1) * cardSpaceing, 1050, paint);
                    } else {
                        for (int j = 0; j < hand.size() - 1; j++) {
                            // IF A CARD IS DRAWN THEN RENDER THAT ARRAY WITH ONE LESS CARD TO GIVE APPEARANCE THAT ITS BEING DRAWN FROM THE DECK INSTEAD OF JUST POPPING IN.
                            canvas.drawBitmap(hand.get(j).image, (j + 1) * cardX, 1050, paint);
                        }
                    }
                    if (cardXX < cardSpaceing) {
                        // RENDER THAT LAST CARD IN THE ARRAY SEPARATELY MOVING IT ACROSS THE SCREEN TO GIVE THE APPERANCE THAT IT IS BEING DRAWN FROM THE DECK.
                        canvas.drawBitmap(hand.get(hand.size() - 1).image, (hand.size() + 0) * cardXX, 1050, paint);
                    }
                }

                if(cardXX >= cardSpaceing) {
                    // RENDER IF NO CARD IS BEING DRAWN
                    canvas.drawText("Score: " + GV.handValue(hand), 1 * cardSpaceing, 1100 + cHeight, paint);
                } else {
                    // RENDER THE SCORE MINUS THE LAST LASTS VALUE WHILE ANIMATION OF CARD DRAW IS BEING PLAYED
                    canvas.drawText("Score: " + (GV.handValue(hand) - lastCardValue(hand.get(hand.size()-1))), 1 * cardSpaceing, 1100 + deck.cHeight, paint);
                }
                canvas.drawText("Bet: " + bet, 1 * cardSpaceing, 1150 + cHeight, paint);



                // split hand
                for (int i = 0; i < handSplit.size(); i++) {
                    if (cardXXX >= cardSpaceing) {
                        // IF NO NEW CARDS ARE DRAWN RENDER THIS (CARDXX IS SET TO 0 ON CARD DRAW THATS NOT FIRST ROUND).
                        canvas.drawBitmap(handSplit.get(i).image, (i + 10) * cardSpaceing, 1050, paint);
                    } else {
                        for (int j = 0; j < handSplit.size() - 1; j++) {
                            // IF A CARD IS DRAWN THEN RENDER THAT ARRAY WITH ONE LESS CARD TO GIVE APPERANCE THAT ITS BEING DRAWN FROM THE DECK INSTEAD OF JUST POPPING IN.
                            canvas.drawBitmap(handSplit.get(j).image, (j + 10) * cardX, 1050, paint);
                        }
                    }
                    if (cardXXX < cardSpaceing) {
                        // RENDER THAT LAST CARD IN THE ARRAY SEPARATELY MOVING IT ACROSS THE SCREEN TO GIVE THE APPERANCE THAT IT IS BEING DRAWN FROM THE DECK.
                        canvas.drawBitmap(handSplit.get(handSplit.size() - 1).image, (handSplit.size() + 9) * cardXXX, 1050, paint);
                    }
                }

                if(cardXXX >= cardSpaceing) {
                    // RENDER IF NO CARD IS BEING DRAWN
                    canvas.drawText("Score: " + GV.handValue(handSplit), 10 * cardSpaceing, 1100 + cHeight, paint);
                } else {
                    // RENDER THE SCORE MINUS THE LAST LASTS VALUE WHILE ANIMATION OF CARD DRAW IS BEING PLAYED
                    canvas.drawText("Score: " + (GV.handValue(handSplit) - lastCardValue(handSplit.get(handSplit.size()-1))), 10 * cardSpaceing, 1100 + deck.cHeight, paint);
                }
                canvas.drawText("Bet: " + betSplit, 10 * cardSpaceing, 1150 + cHeight, paint);
            }



            // arrow pointer
            if (GV.leftHand) {
                canvas.drawBitmap(downArrow, 2 * cardSpaceing,arrowY - cHeight, paint );
            } else if (GV.rightHand) {
                canvas.drawBitmap(downArrow, 11 * cardSpaceing, arrowY - cHeight, paint);
            }
        }
    }
    public void drawCard_Hand(){
        hand.add(deck.drawCard());
        if(GV.standBtn.getText().toString().equalsIgnoreCase("Stand")) {
                cardXX = -50;
        }
    }
    public void drawCard_Split(){
        handSplit.add(deck.drawCard());
            cardXXX = -50;
    }

    public void splitPhysics(Canvas canvas, Paint paint){
        canvas.drawBitmap(hand.get(0).image, (1) * cardXXXX, 1050, paint);
        canvas.drawBitmap(handSplit.get(0).image, (10) * cardXXXXX, 1050, paint);


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
