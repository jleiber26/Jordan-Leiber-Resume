#include <stdio.h>
#include "Adafruit_2_4_LCD_Serial_Library\tft_gfx.h"
#include "Adafruit_2_4_LCD_Serial_Library\tft_master.h"
#include "button_tempo_controller.h"



//This module is gonna contain something that will draw the message
//To explain how to enter a tempo with tapping

//That drawing dealy will be called within a state machine...so...tick fxn

//What's that machine gonna do? 
//INIT, IDLE, DISPLAY, PRESSED, RELEASED, DONE

//IDLE...youre still in the slider mode of BPM entry
//DISPLAY...change the screen to display the instructions rather than the slider thing
//PRESSED...keep displaying the message...but if the button is pressed grab the time it was pressed
//HELD...while the button is being held in, we dont want to keep grabbing data points...so stay here until
//we're ready for another press

//Ok so how are we going to get the average time between taps
//Obvious choice would be to just collect the time of each press...but I'm fairly sure well get major roll over issues
//So lets actively take an average
//Press once...theres a time in ms..
//Press twice...2 -1 is the time in ms b/w press 1 and 2...theres a bpm there
//Store 2...grab 3 on the press...theres a second interval
//Do again and again until 5 presses...now you have 4 intervals stored
//Average those...return 

uint8_t buffer[30];
uint32_t avg; 
uint32_t output_period = 500;

void display_button_bpm() {
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
    sprintf(buffer, "%d", 60000/output_period);
    tft_writeString(buffer);
}

static enum Button_States {
    INIT, IDLE, DISPLAY, PRESSED, HELD
}
button_state;

void init_button_fsm() {
    button_state = INIT;
}

uint32_t tickFct_button(uint8_t pressed, uint16_t curr_time) {
    static uint8_t prev_press = 0;
    static uint32_t sum, time1, time2, interval, startTime;
    static uint8_t count = 0;

    //Switch statement 1 for state transition
    switch (button_state) {
        case INIT:
            button_state = IDLE;
            break;

        case IDLE:
            button_state = DISPLAY;
            break;

        case DISPLAY:
            if (pressed) {
                button_state = PRESSED;
            } else {
                button_state = DISPLAY;
            }

            break;

        case PRESSED:
            if (prev_press && pressed) {
                button_state = HELD;
            } else {
                button_state = DISPLAY;
            }
            break;

        case HELD:
            if (pressed == 0) {
                button_state = DISPLAY;
            }
            break;
    }

    //Switch statement 2 for output (here the output is the address of the buffer)
    switch (button_state) {
        case INIT:
            //Do nothing
            break;

        case IDLE:
            //Do nothing
            //tft_fillCircle(190, 160, 8, ILI9341_PURPLE);
            break;

        case DISPLAY:
            if (count == 0) {
                //tft_fillScreen(ILI9341_BLACK);
                tft_setCursor(50, 150);
                tft_setTextColor(ILI9341_WHITE);
                tft_setTextSize(1);

                //erase old text
                tft_setTextColor(ILI9341_BLACK);
                tft_writeString(buffer);
                tft_setCursor(50, 150);

                tft_setTextColor(ILI9341_WHITE);
                tft_writeString("Tap the button at the desired tempo");
            }
            
            display_button_bpm();
            //tft_fillCircle(190, 160, 8, ILI9341_YELLOW);
            break;

        case PRESSED:
            if (count == 0) {
                avg = 0;
                sum = 0;
                
                time1 = curr_time;
                
                
            }
            else {
                time2 = curr_time;

                if (time2 >= time1) {
                    interval = time2 - time1;
                } else if (time1 >= time2) {
                    interval  = time1 - time2;
                }
                
                sum += interval;
                time1 = time2;
            }
            count++;
            if(count == 4) {
                count = 0;
                avg = sum/3;
                output_period = avg;
                return output_period;
            }
            //tft_fillCircle(190, 160, 8, ILI9341_OLIVE);
            break;

        case HELD:
            //Do nothing
            break;
    }
}

