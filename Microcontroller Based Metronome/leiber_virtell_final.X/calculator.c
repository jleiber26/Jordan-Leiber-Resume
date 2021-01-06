#include "calculator.h"
#include "Adafruit_2_4_LCD_Serial_Library/tft_master.h"
#include "Adafruit_2_4_LCD_Serial_Library/tft_gfx.h"
#include "ts_lcd.h"
#include "draw_main_screen.h"
#include <inttypes.h>
#include <math.h>





static char buffer[30];


//Enumerated states

static enum Calculator_States {
    INIT, CLEAR_SCREEN, VAL1_PRESS, OP_PRESS, VAL2_PRESS, ERROR_CHECK, DISPLAY, WAIT
}
calculator_state, prev_state;

void init_calculator_FSM() {
    calculator_state = INIT;
}

void tickFct_calculator(uint8_t press, char button) {

    static uint8_t previous_press;

    static int64_t val1, val2, result; //Val 1 should store the first number entered into the calculator, 2 the second, and result the result of mathy things
    static int32_t resMax = 2147483647;
    static int32_t resMin = -2147483646;
    static char operation;
    static uint8_t error; // 0 - no error, 1 - underflow, 2 - overflow, 3 - "div0"


    //Switch statement 1 for state transition
    switch (calculator_state) {
        case INIT:
            calculator_state = CLEAR_SCREEN; //On initialization go directly to the no push state
            break;

        case CLEAR_SCREEN:
            //Stay in clear if nothing happens...otherwise...a value has been pressed
            if (press && (button >= 48 && button <= 57)) { //If a button gets pressed, and that button is a number
                calculator_state = VAL1_PRESS; //If you hit a number...well do something about it
            } else {
                calculator_state = CLEAR_SCREEN; //If you're clearing the screen and doing nothing...stay clearing
            }

            break;

        case VAL1_PRESS:
            //Yay you hit a button
            //Record that button, then wait.
            prev_state = VAL1_PRESS;
            calculator_state = WAIT;

            break;

        case OP_PRESS:
            prev_state = OP_PRESS;
            calculator_state = WAIT;
            break;

        case VAL2_PRESS:
            prev_state = VAL2_PRESS;
            calculator_state = WAIT;
            break;

        case ERROR_CHECK:

            calculator_state = DISPLAY;

            break;

        case DISPLAY:
            prev_state = DISPLAY;
            calculator_state = WAIT;
            break;

        case WAIT:
            //If you hit a button that is a number...and you were just in val1...go back there
            //If you hit a butotn that is anumber...and you were in val2...go back there
            //If you hit an operator...go to op_press
            
            if (press && (button == 'C')) { //If at any point we press clear
                calculator_state = CLEAR;
            } else if(error != 0xFF) {
                calculator_state = DISPLAY;
            } else if (press == 1 && press != previous_press && (button >= 48 && button <= 57) && prev_state == VAL1_PRESS) { //If you are waiting, and hit another number, go back
                calculator_state = VAL1_PRESS;

            } else if (press == 1 && press != previous_press && (button >= 48 && button <= 57) && prev_state == VAL2_PRESS) { //If you are waiting, and hit another number, go back
                calculator_state = VAL2_PRESS;

            }//If at any point you are waiting, and you hit an operator...and you aren't in val1...go to op press
                //also...if you hit an operator and were already in operator...just go back and change the operator you're looking for
            else if ((button == 42 || button == 43 || button == 45 || button == 47) && (prev_state == VAL1_PRESS || prev_state == OP_PRESS)) {
                calculator_state = OP_PRESS;

            } else if(press == 1 && press != previous_press && (button == 42 || button == 43 || button == 45 || button == 47) && (prev_state == DISPLAY)) {
                val1 = result;
                val2 = result = 0;
                calculator_state = OP_PRESS;
                
            } else if (press == 1 && press != previous_press && (button == 61) && (prev_state == VAL2_PRESS)) { //We are done entering the second value...and want to get a result
                calculator_state = ERROR_CHECK;

            } else if (press == 1 && press != previous_press && (button >= 48 && button <= 57) && prev_state == DISPLAY) { //We hit a number after we were just displaying the previous result...go to val2 enter
                calculator_state = VAL2_PRESS;

            } else if (press == 1 && press != previous_press && (button >= 48 && button <= 57) && prev_state == OP_PRESS) { //We hit a number after we were just displaying the previous result...go to val2 enter
                calculator_state = VAL2_PRESS;

            } else {
                calculator_state = WAIT;
            }

            break;

    }

    //Switch statement 2 for output (here the output is a global var)
    switch (calculator_state) {
        case INIT:
            //Do Nothing
            break;

        case CLEAR_SCREEN:
            val1 = val2 = result = 0; //Reset all values
            operation = 0;
            error = 0xFF;
            
            tft_setCursor(20, 20);
            tft_setTextColor(ILI9341_WHITE);
            tft_setTextSize(2);

            //erase old text
            tft_setTextColor(ILI9341_BLACK);
            tft_writeString(buffer);
            tft_setCursor(20, 20);

            tft_setTextColor(ILI9341_WHITE);
            sprintf(buffer, "C: %d ", result);
            tft_writeString(buffer);
            break;

        case VAL1_PRESS:
            
            val1 = val1*10 + ((int32_t)button-48); //Each time we enter a new number, slide the old one left, add the new
            

            tft_setCursor(20, 20);
            tft_setTextColor(ILI9341_WHITE);
            tft_setTextSize(2);

            //erase old text
            tft_setTextColor(ILI9341_BLACK);
            tft_writeString(buffer);
            tft_setCursor(20, 20);

            tft_setTextColor(ILI9341_WHITE);
            sprintf(buffer, "%d ", val1);
            tft_writeString(buffer);
            break;

        case OP_PRESS:
            operation = button;

            tft_setCursor(20, 20);
            tft_setTextColor(ILI9341_WHITE);
            tft_setTextSize(2);

            //erase old text
            tft_setTextColor(ILI9341_BLACK);
            tft_writeString(buffer);
            tft_setCursor(20, 20);

            tft_setTextColor(ILI9341_WHITE);
            sprintf(buffer, "%c ", operation);
            tft_writeString(buffer);
            break;

        case VAL2_PRESS:
            
             val2 = val2*10 + ((int32_t)button-48); //Each time we enter a new number, slide the old one left, add the new
            
            

            tft_setCursor(20, 20);
            tft_setTextColor(ILI9341_WHITE);
            tft_setTextSize(2);

            //erase old text
            tft_setTextColor(ILI9341_BLACK);
            tft_writeString(buffer);
            tft_setCursor(20, 20);

            tft_setTextColor(ILI9341_WHITE);
            sprintf(buffer, "%d ", val2);
            tft_writeString(buffer);
            break;

        case ERROR_CHECK:
            if (operation == '+') {
                if (val1 + val2 > resMax) {
                    error = 2;
                } else {
                    result = val1 + val2;
                }
            } else if (operation == '-') {
                if (val1 - val2 < resMin) {
                    error = 1;
                } else {
                    result = val1 - val2;
                }
            } else if (operation == '*') {
                if (val1 * val2 > resMax) {
                    error = 2;
                } else if (val1 * val2 < resMin) {
                    error = 1;
                } else {
                    result = val1 * val2;
                }
            } else if (operation == '/') {
                if (val2 == 0) {
                    error = 3;
                } else if (val1 / val2 > resMax) {
                    error = 2;
                } else {
                    result = val1 / val2;
                }
            }
            break;

        case DISPLAY:

            //Display Result
            tft_setCursor(20, 20);
            tft_setTextColor(ILI9341_WHITE);
            tft_setTextSize(2);

            //erase old text
            tft_setTextColor(ILI9341_BLACK);
            tft_writeString(buffer);
            tft_setCursor(20, 20);

            tft_setTextColor(ILI9341_WHITE);

            if (error == 0) {
                sprintf(buffer, "%d", result);
            } else if (error == 1) {
                sprintf(buffer, "UNDERFLOW");
            } else if (error == 2) {
                sprintf(buffer, "OVERFLOW");
            } else if (error == 3) {
                sprintf(buffer, "DIV0");
            } else {
                sprintf(buffer, "%d ", result);
            }

            tft_writeString(buffer);


            break;

        case WAIT:
            if(val1 >= resMax || val2 >= resMax) {
                error = 2;
            }
            break;
    }
    previous_press = press;

}







