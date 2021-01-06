/* 
 * File:   touchscreen_main.c
 * Author: joshu
 *
 * Created on October 2, 2020, 10:13 AM
 */
#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include "config.h"

#include "TouchScreen.h"
#include "Adafruit_2_4_LCD_Serial_Library/tft_master.h"
#include "Adafruit_2_4_LCD_Serial_Library/tft_gfx.h"
#include "ts_lcd.h"
#include "adc_intf.h"
#include "debouncer.h"
#include "button_detect.h"
#include "timer1.h"
#include "ztimer.h"

#include "slider_handle.h"
#include "draw_main_screen.h"
#include "button_tempo_controller.h"

#include "metronome_tempo_fsm.h"
#include "click_control.h"

#include "uart1.h"

#include "dds_fsm.h"

//Includes for dds
#include <xc.h>
#include <plib.h>

#include "button_control.h"
#include "debouncer_tempo.h"
#include "debounce_on_off.h"


#define XM AN0
#define YP AN1

#define SYSCLK	40000000UL
//#define PBCLK	40000000UL

#define DEBOUNCE_TIMER 50

/*
 * 
 */
int main(int argc, char** argv) {
    uint16_t x, y;
    uint8_t z, screen_touched;
    uint16_t ta1, ta2, tb1, tb2, tc1, tc2;

    uint8_t tick_type;

    uint8_t tempo_press, on_off_press, prev_press;

    //Is the metronome on or off
    uint8_t on_off;

    //Var for what mode of tempo entry we should be in
    //Also one that will let these be toggles...stay in one mode until you're done with it
    uint8_t tempo_mode, mode_select, previous_mode;

    uint8_t box_not_drawn, cover_text_not_done;

    uint16_t metronome_period = 500;
    uint16_t metronome_period_temp;
    uint16_t graphics_period = 150;
    uint16_t button_tempo_period = 75;
    uint16_t debounce_tempo_elapsedTime = 50;
    uint16_t click_period = 50;
    uint16_t tone_period = 3;
    uint16_t z_period = 5;

    uint16_t metronome_elapsed = metronome_period;
    uint16_t click_elapsed = click_period;
    uint16_t graphics_elapsed = graphics_period;
    uint16_t button_tempo_elapsed = button_tempo_period;

    char buffer[30];

    configureADC();

    SYSTEMConfigPerformance(SYSCLK);
    CM1CON = 0;
    CM2CON = 0;
    ANSELA = 0;
    ANSELB = 0; // why disable comparators? not sure

    mPORTASetPinsDigitalOut(BIT_0); // this was in Tahmid's example; not sure why

    PORTSetPinsDigitalOut(IOPORT_B, BIT_10);

    //    dds_init();
    INTEnableSystemMultiVectoredInt();

    //dds_on(440);
    ts_lcd_init();

    tft_fillScreen(ILI9341_BLACK);
    drawMainScreen();

    timer1_init();
    init_debounce_FSM();
    init_debounce_tempo_FSM();
    init_debounce_on_off_FSM();

    draw_slider_box();

    init_metronome_fsm();

    init_buttons();
    init_button_fsm();

    init_dds_fsm();

    uart1_init(259);

    zTimerSet(z_period);
    zTimerOn();

    //Init time
    ta1 = ta2 = timer1_read();
    tb1 = tb2 = timer1_read();
    tc1 = tc2 = timer1_read();

    //uart1_txwrite_str("TEST");

    uint16_t count;

    while (1) {
        z = ts_lcd_get_ts(&x, &y);

        //Control the graphics for the slider and the tap mode
        //If you are in the tap mode, cover up the text that was in the slider mode only once

        //In the slider mode, cover up what was there from the tap mode, and also handle moving the slider around (doesn't need a state machine)
        if (tempo_mode == 1) { //In tap mode
            if (cover_text_not_done) { //Cover up the old text
                tft_fillRect(200, 90, 100, 50, ILI9341_BLACK);
                clear_slider_box();
                cover_text_not_done = 0;
            }
            box_not_drawn = 1;

        } else if (tempo_mode == 2) { //In slide mode so handle the entirety of the moving the slider...
            cover_text_not_done = 1;

            if (box_not_drawn) {
                draw_slider_box();
                tft_fillRect(200, 90, 100, 50, ILI9341_BLACK);

                box_not_drawn = 0;
            }
            //Slide Handle
            move_slider(screen_touched, x);

            if ((get_slider_bpm() > 0) && (get_slider_bpm() != (60000 / metronome_period))) {
                //tft_fillRect(100, 50, 100, 100, ILI9341_PURPLE);
                metronome_period = 60000 / get_slider_bpm();
                metronome_elapsed = metronome_period;
            }
        }


        //DEBOUNCER TIMER INTERVAL
        //Use this for all debouncers except tempo button
        ta2 = timer1_read();
        if (timer1_ms_elapsed(ta1, ta2) > DEBOUNCE_TIMER) {
            screen_touched = tickFct_debounce(z); //Touchscreen debounce
            on_off_press = tickFct_debounce_on_off(read_on_button()); //On off button debounce

            if (on_off_press == 0 && prev_press == 1) {
                on_off = !on_off;
            }

            //On off indicator
            if (on_off) {
                tft_fillCircle(160, 200, 3, ILI9341_GREEN); //Green if on, red if off
            } else {
                tft_fillCircle(160, 200, 3, ILI9341_RED);

            }
            //Grab what is being pressed
            //1 if TAP
            //2 if SLIDE
            //0 if neither or not touching the screen
            if (screen_pressed(screen_touched, x, y) == 1) {
                tempo_mode = 1;
            } else if (screen_pressed(screen_touched, x, y) == 2) {
                tempo_mode = 2;
            } else {
                tempo_mode = tempo_mode;
            }

            ta1 = ta2;
        }//Debounce timer

        tc2 = timer1_read();
        if (timer1_ms_elapsed(tc1, tc2) > 25) {
            tempo_press = tickFct_debounce_tempo(read_tempo_button());

            tc1 = tc2;

        }//Tempo press debounce

        //Run the tap mode state machine
        if (button_tempo_elapsed > button_tempo_period) {
            //Only tick if you are in the proper mode
            if (tempo_mode == 1) {
                metronome_period_temp = tickFct_button(!tempo_press, zTimerReadms());
                //Avoid div0 and being out of the requirement bounds
                if (metronome_period_temp > 125 && metronome_period_temp < 1500) {
                    metronome_period = metronome_period_temp;
                    metronome_elapsed = metronome_period;
                }
            }
            button_tempo_elapsed = 0;
        }//Tap state tick

        //Metronome FSM (When should I click)
        //Let the main control the on off of hte metronome so we dont mess with the tempos
        if (metronome_elapsed > metronome_period) {
            if (on_off) { //Turn on 
                tick_type = tick_metronome_fsm(1);
            } else { //Turn off
                tick_type = tick_metronome_fsm(0);
            }
            metronome_elapsed = 0;
        }//metronome tick

        //CLICK TIMER INTERVAL
        tb2 = timer1_read();
        if (timer1_ms_elapsed(tb1, tb2) > tone_period) {
            if (tick_type) { //If the metronome says its time to tick, run the dds table once, then stop
                tickFct_dds(1); //Run the table
                tick_type = 0; //Stop after 3 ms
            } else {
                tickFct_dds(0); //If we dont want to tick...then dont
                tick_type = 0;
            }
            tb1 = tb2;
        }//Click tick

        while (!zTimerReadFlag()) {
        } // Wait for timer period

        metronome_elapsed += z_period;
        button_tempo_elapsed += z_period;

        prev_press = on_off_press;

    }//while(1)

}

