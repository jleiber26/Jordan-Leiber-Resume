
#include "Adafruit_2_4_LCD_Serial_Library/tft_master.h"
#include "Adafruit_2_4_LCD_Serial_Library/tft_gfx.h"
#include <inttypes.h>

uint16_t bpm;

uint8_t box_startx, boxy;
uint16_t box_h, box_w;

uint8_t slider_w = 10;
uint16_t slider_x, slider_y;

uint16_t prev_x;

uint16_t max_bpm = 400;
uint16_t min_bpm = 20;

static char buffer[30];

void draw_slider_box() {

    box_startx = 40;
    boxy = 150;
    box_w = 320 - 80;
    box_h = 10;

    tft_fillRect(box_startx, boxy, box_w, box_h, ILI9341_WHITE);
}

void clear_slider_box() {
    box_startx = 40;
    boxy = 150;
    box_w = 320 - 80;
    box_h = 10;

    tft_fillRect(box_startx, boxy, box_w, box_h, ILI9341_BLACK); //Clear out the box
}

uint16_t map_bpm(uint16_t xloc) {
    //We want to stupid map the x location to a bpm from 20-400
    //The new x' is bpm 
    //The current x is xloc 

    //These gotta be float 
    float temp1 = xloc - box_startx;
    float temp2 = box_startx + box_w - slider_w - box_startx;

    bpm = (temp1 / temp2) * (max_bpm - min_bpm) + min_bpm;
    return bpm;
}
//Function to get the bpm and put it on the screen

void display_bpm() {
    //We need to map the current x position of the stupid slider to the bpm

    //Draw BPM to screen
    tft_setCursor(200, 95);
    //Delete old text
    tft_setTextSize(5);
    tft_setTextColor(ILI9341_BLACK);
    tft_writeString(buffer);

    //New Text
    tft_setCursor(200, 95);
    tft_setTextColor(ILI9341_WHITE);
    sprintf(buffer, "%d", map_bpm(prev_x));
    tft_writeString(buffer);
}

void move_slider(uint8_t pressed, uint16_t xloc) { //xloc is the position of the touch

    //Check init case for prev_x...dont let it not be on the thing
    //Lets just init it to the min bpm
    if (prev_x < box_startx) {
        prev_x = box_startx;
    }

    //If you're pressing the stupid screen, then you're going to have to update the slider 
    //AND you're within the bounds of the box
    if (pressed && xloc > box_startx && xloc < box_startx + box_w - slider_w) {
        if (xloc != prev_x) { //If youve movedf

            //Put up the new bpm
            display_bpm(xloc);

            //put a white box in the old spot, and a purple one where you are now
            tft_fillRect(prev_x, boxy, slider_w, box_h, ILI9341_WHITE);

            tft_fillRect(xloc, boxy, slider_w, box_h, ILI9341_PURPLE);

            prev_x = xloc; //Do all of that stuff then store where you are as the previous loc
        }



    } else if (!pressed) {
        //If you're not pressing the stupid thing, put a box in the previous location
        tft_fillRect(prev_x, boxy, slider_w, box_h, ILI9341_PURPLE);

    }

}

uint16_t get_slider_bpm() {
    return map_bpm(prev_x);
}


