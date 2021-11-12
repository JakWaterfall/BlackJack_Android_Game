package com.main.blackjack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;

import static com.main.blackjack.Player.cardSpaceing;


public class GameView2 extends SurfaceView implements SurfaceHolder.Callback {

    // VARIABLES
    boolean stand = false;
    boolean leftHand = false;
    boolean rightHand = false;
    int startingBet;

    // RESULT TEXT
    String resultText = "";
    String resultTextSplit = "";
    int Red;
    int Green;
    int Blue;

    // OBJECTS AND CLASSES
    MainThread thread;
    Deck deck;
    Player player;
    Dealer dealer;
    Paint paint;

    // CONTROLS
    Button standBtn;
    Button hitBtn;
    Button splitBtn;
    Button doubleBtn;
    SeekBar betBar;

    // IMAGES
    Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.felt);

    public GameView2(Context context, Button stand, Button hit, Button split, Button doubleDown, SeekBar betBar) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        // CONTROLS
        this.standBtn = stand;
        this.hitBtn = hit;
        this.splitBtn = split;
        this.doubleBtn = doubleDown;
        this.betBar = betBar;
        // OBJECTS
        paint  = new Paint();
        deck   = new Deck(getContext());
        player = new Player(deck, this);
        dealer = new Dealer(deck, this);

    }

    public GameView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void update(){
        controls();
        betPhase();
        player.update();
        dealer.update();
        Red += 4;
        Green += 5;
        Blue += 6;
        if (Red >= 255) Red = 10;
        if (Green >= 245) Green = 10;
        if (Blue >= 235) Blue = 10;

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            // BACKGROUND
            canvas.drawBitmap(bg, 0, 0, null);

            // DEALER
            dealer.render(canvas, paint);

            // PLAYER
            player.render(canvas, paint);

            // RESULT TEXT
            if (player.cardXX >= cardSpaceing) {
                // ^^^^ ABOVE LINE IS SO RESULT TEXT DOSENT SHOW TILL DRAWING ANIMATION IS DONE
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.rgb(Red, Green, Blue));
                if (resultText.length() > 14)
                    paint.setTextSize(50);
                else
                    paint.setTextSize(100);
                if (player.handSplit.size() < 1) {
                    canvas.drawText(resultText, canvas.getWidth() / 2, canvas.getHeight() / 3, paint);
                } else {
                    if (resultText.length() > 14)
                        paint.setTextSize(35);
                    else
                        paint.setTextSize(50);
                    canvas.drawText(resultText, canvas.getWidth() / 4, canvas.getHeight() / 2, paint);
                    if (resultTextSplit.length() > 14)
                        paint.setTextSize(35);
                    else
                        paint.setTextSize(50);
                    canvas.drawText(resultTextSplit, canvas.getWidth() * 0.75f, canvas.getHeight() / 2, paint);
                }

            }

            // STARTING BET AND CHIPS
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(40);
            canvas.drawText("Chips: " + player.chips + "  Bet: "+ startingBet, 60, 1220 + player.cHeight, paint);
        }
    }

    public void betPhase() {
        if (standBtn.getText().toString().equalsIgnoreCase("Bet!")) {
            leftHand = false;
            rightHand = false;
            doubleBtn.setEnabled(false);
            hitBtn.setEnabled(false);
            splitBtn.setEnabled(false);
            betBar.setEnabled(true);
        }
    }

    public void startingRound(){
        // Starting Draw
        player.cardX = -50;
        dealer.cardX = -50;
        dealer.drawCard();
        dealer.drawCard();
        player.drawCard_Hand();
        player.drawCard_Hand();
        // SPLIT TEST
        if (player.hand.get(0).value == player.hand.get(1).value && player.chips > player.bet) {
            splitBtn.setEnabled(true);
        }
    }

    public void stand(ArrayList<Card> hand){
        stand = true;

        if (handValue(hand) > 21) {
            if (hand == player.hand) {
                resultText += "BUST";
            } else resultTextSplit += "BUST";

            if (!leftHand) {
                standBtn.setText(R.string.bet);
                return;
            }
        }
        dealer.dealerAction();

        if (handValue(dealer.hand) > 21) {
            if (hand == player.hand) {
                resultText += "DEALER BUST";
            } else resultTextSplit += "DEALER BUST";
            // payout
            if (handValue(player.hand) <= 21) player.chips += player.bet * 2;
            if (handValue(player.handSplit) <= 21) player.chips += player.betSplit * 2;
            standBtn.setText(R.string.bet);
            return;
        }

        if (handValue(hand) > handValue(dealer.hand)) {
            if (hand == player.hand) {
                resultText += "PLAYER WINS";
            } else resultTextSplit += "PLAYER WINS";
            // payout
            if (hand == player.hand) player.chips += player.bet * 2;
            if (hand == player.handSplit) player.chips += player.betSplit * 2;
        } else if (handValue(hand) < handValue(dealer.hand)) {
            if (hand == player.hand) {
                resultText += "DEALER WINS";
            } else resultTextSplit += "DEALER WINS";
        } else {
            if (hand.size() < dealer.hand.size()) {
                if (hand == player.hand) {
                    resultText += "PLAYER WINS WITH LESS CARDS";
                } else resultTextSplit += "PLAYER WINS WITH LESS CARDS";
                // payout
                if (hand == player.hand) player.chips += player.bet * 2;
                if (hand == player.handSplit) player.chips += player.betSplit * 2;
            } else if (hand.size() > dealer.hand.size()) {
                if (hand == player.hand) {
                    resultText += "DEALER WINS WITH LESS CARDS";
                } else resultTextSplit += "DEALER WINS WITH LESS CARDS";
            } else {
                if (hand == player.hand) {
                    resultText += "DRAW";
                } else resultTextSplit += "DRAW";
                // payout
                if (hand == player.hand) player.chips += player.bet;
                if (hand == player.handSplit) player.chips += player.betSplit;
            }

        }
        standBtn.setText(R.string.bet);
    }

    public void controls(){
        // STAND / BET
        standBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doubleDownCheck();
                if(standBtn.getText().toString().contains("Stand")){
                    // CHANGE HANDS
                    if(leftHand){
                        leftHand = false;
                        rightHand = true;
                        doubleDownCheck();
                    } else if(rightHand){
                        stand(player.hand);
                        stand(player.handSplit);
                    } else {
                        // IF ONE HAND
                        stand(player.hand);
                    }
                } else {
                    // IF BETING PHASE
                    standBtn.setText(R.string.standBtn);
                    stand = false;
                    resultText = "";
                    resultTextSplit = "";
                    betBar.setEnabled(false);
                    hitBtn.setEnabled(true);
                    deck.reshuffleDeck();
                    dealer.hand.clear();
                    player.hand.clear();
                    player.handSplit.clear();

                    player.bet = 0;
                    player.betSplit = 0;
                    player.chips -= startingBet;
                    player.bet += startingBet;

                    startingRound();
                    doubleDownCheck();
                }
            }
        });

        // HIT
        hitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                splitBtn.setEnabled(false);

                if (!rightHand) {
                    player.drawCard_Hand();
                    if (handValue(player.hand) > 21 && !leftHand) {
                        stand(player.hand);
                    } else if (handValue(player.hand) > 21 && leftHand) {
                        leftHand = false;
                        rightHand = true;
                    }
                } else if (rightHand) {
                    player.drawCard_Split();
                    if (handValue(player.handSplit) > 21) {
                        stand(player.hand);
                        stand(player.handSplit);
                    }
                }
                doubleDownCheck();
            }
        });
        // SPLIT
        splitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                splitBtn.setEnabled(false);
                leftHand = true;

                player.betSplit += player.bet;
                player.chips -= player.bet;

                player.handSplit.add(player.hand.get(1));
                player.hand.remove(1);
                player.hand.add(deck.drawCard());
                player.handSplit.add(deck.drawCard());

                player.cardXXXX = 8 * cardSpaceing;
                player.cardXXXXX =  54;

                doubleDownCheck();
            }
        });
        // DOUBLE DOWN
        doubleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                splitBtn.setEnabled(false);

                if (!rightHand) {
                    player.drawCard_Hand();

                    player.chips -= player.bet;
                    player.bet += player.bet;

                    if (leftHand) {
                        // SWAP HANDS
                        leftHand = false;
                        rightHand = true;
                    } else {
                        stand(player.hand);
                    }
                } else if (rightHand) {
                    player.drawCard_Split();

                    player.chips -= player.betSplit;
                    player.betSplit += player.betSplit;
                    stand(player.hand);
                    stand(player.handSplit);
                }
                doubleDownCheck();
            }
        });
        // BET BAR
        betBar.setMax(player.chips);
        betBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                startingBet = betBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void doubleDownCheck() {
        if (player.hand.size() > 2 && !rightHand || player.chips < player.bet && !rightHand) {
            doubleBtn.setEnabled(false);
        } else if (player.handSplit.size() > 2 || player.chips < player.betSplit) {
            doubleBtn.setEnabled(false);
        } else {
            doubleBtn.setEnabled(true);
        }
    }

    public int handValue(ArrayList<Card> hand) {
        @SuppressWarnings("unchecked")
        ArrayList<Card> cloneHand = (ArrayList<Card>) hand.clone();
        int s = cloneHand.size();
        // moves aces to the back
        for (int i = 0; i < s; i++) {
            if (cloneHand.get(i).value == "Ace") {
                cloneHand.add(cloneHand.get(i));
                cloneHand.remove(i);
            }
        }
        // works out value
        int handValue = 0;
        for (int i = 0; i < cloneHand.size(); i++) {
            if (cloneHand.get(i).value == "Ace" && handValue < 11) {
                handValue += 11;
            } else if (cloneHand.get(i).value == "Ace" && handValue >= 11) {
                handValue += 1;
            } else {
                handValue += Integer.parseInt(cloneHand.get(i).value);
            }
        }

        // DOUBLE CHECKS ACES
        if (handValue > 21) {
            handValue = 0;
            for (int i = 0; i < cloneHand.size(); i++) {
                if (cloneHand.get(i).value == "Ace") {
                    handValue += 1;
                } else {
                    handValue += Integer.parseInt(cloneHand.get(i).value);
                }
            }
        }
        return handValue;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            } catch (Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }
}
