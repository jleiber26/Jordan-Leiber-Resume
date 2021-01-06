
#include "Adafruit_2_4_LCD_Serial_Library/tft_master.h"
#include "Adafruit_2_4_LCD_Serial_Library/tft_gfx.h"
#include <inttypes.h>

uint8_t beats_in_measure; //In future you can change this depending on user desired thing

//Enumerated states

static enum metronome_states {
    INIT, OFF, HIGH_CLICK, LOW_CLICK
} metronome_state;

void init_metronome_fsm() {
    //Just init the thing
    metronome_state = INIT;
}

uint8_t tick_metronome_fsm(uint8_t button_press) { //Take in which beat you're on to determine what tick to play

    tft_fillCircle(15, 10, 3, ILI9341_RED);

    beats_in_measure = 4;

    //Return 0 for off 
    // 1 for high 
    //2 for low
    static uint8_t beat = 0; //Update on each time you call the thingy

    //This needs to talk to the thing that handles the clicking sound
    //so...when the next beat of the metronome comes up...call this tick function
    //When you call this function...tell the clicky thing what to do 
    //So...if you're off here...then the clicky thing wont do anything
    //Blah blah do the things 

    switch (metronome_state) {

        case INIT:
            //Go right to off
            metronome_state = OFF;
            break;

        case OFF:
            //If you hit the stupid start button...then start
            //If you dont...then...dont
            if (button_press) {
                metronome_state = HIGH_CLICK;
            } else {
                metronome_state = OFF;
            }

            break;

        case HIGH_CLICK:

            if (!button_press) {
                metronome_state = OFF;
            } else {

                //FOr right now we only want to play this as the first beat of each measure...later can put desired
                //emphasized notes into an array...and if the upcoming beat is in that array...then horray...come back here

                //ONLY FOR RIGHT NOW GO RIGHT TO LOW CLICK
                metronome_state = LOW_CLICK;
            }
            break;

        case LOW_CLICK:

            if (!button_press) {
                metronome_state = OFF;
            } else {
                //If youve hit the end of the measure...then go back to the high click
                if (beat < beats_in_measure) {
                    metronome_state = LOW_CLICK;
                } else {
                    beat = 0;
                    metronome_state = HIGH_CLICK;
                }
            }
            break;

    }

    switch (metronome_state) {

        case INIT:
            //Go right to off
            tft_fillCircle(15, 10, 3, ILI9341_PURPLE);
            break;

        case OFF:
            tft_fillCircle(15, 10, 3, ILI9341_ORANGE);

            return 0;
            break;

        case HIGH_CLICK:
            tft_fillCircle(15, 10, 3, ILI9341_CYAN);

            //Increase beat #
            beat++;

            return 1;
            break;

        case LOW_CLICK:
            tft_fillCircle(15, 10, 3, ILI9341_BLACK);


            beat++;

            return 2;

            break;

    }

} 