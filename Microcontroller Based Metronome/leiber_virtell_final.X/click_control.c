
#include "Adafruit_2_4_LCD_Serial_Library/tft_master.h"
#include "Adafruit_2_4_LCD_Serial_Library/tft_gfx.h"
#include "click_control.h"
#include <inttypes.h>

static uint8_t circle_x = 320 / 3;
static uint8_t circle_y = 15;
static uint8_t circle_r = 5;

uint8_t beats_in_measure = 4; //In future you can change this depending on user desired thing

void play_high_click() {
    tft_fillCircle(circle_x, circle_y, circle_r, ILI9341_BLUE);
}

void play_low_click() {
    tft_fillCircle(circle_x, circle_y, circle_r, ILI9341_ORANGE);

}

void stop_click() {
    tft_fillCircle(circle_x, circle_y, circle_r, ILI9341_BLACK);
}



////Enumerated states
//
//static enum click_states {
//    INIT, OFF, HIGH_CLICK, LOW_CLICK
//}   click_state;
//
//void init_click_fsm(){
//	//Just init the thing
//    click_state = INIT;
//}
//
//void tick_click_fsm(uint8_t sound_type){  //Take in which beat you're on to determine what tick to play
//
//	//This needs to talk to the thing that handles the clicking sound
//	//so...when the next beat of the metronome comes up...call this tick function
//	//When you call this function...tell the clicky thing what to do 
//	//So...if you're off here...then the clicky thing wont do anything
//	//Blah blah do the things 
//
//	switch(click_state){
//
//		case INIT:
//			//Go right to off
//            click_state = OFF; 
//		break;
//
//		case OFF:
//			//If you hit the stupid start button...then start
//			//If you dont...then...dont
//			if(sound_type == 1){
//				click_state	= HIGH_CLICK;
//			}else if(sound_type == 2){
//				click_state	= LOW_CLICK;
//			}else{
//				click_state = OFF;
//			}
//
//		break;
//
//		case HIGH_CLICK: 
//			//Go right back to being off after you go clicky click
//			click_state = OFF;
//		break;
//
//		case LOW_CLICK:
//
//			//Go right back to being off after you go clicky click
//			click_state = OFF;
//
//		break;
//	}
//
//	switch(click_state){
//
//		case INIT:
//			//Go right to off
//            tft_fillCircle(circle_x, circle_y, circle_r, ILI9341_BLUE);  
//
//		break;
//
//		case OFF:
//			//Draw a black circle where we want the circle to show up
//            tft_fillCircle(circle_x, circle_y, circle_r, ILI9341_BLACK);  
//		break;
//
//		case HIGH_CLICK: 
//			//Increase beat #
//			tft_fillCircle(circle_x, circle_y, circle_r, ILI9341_GREEN); 
//
//		break;
//
//		case LOW_CLICK:
//
//			tft_fillCircle(circle_x, circle_y, circle_r, ILI9341_BLUE); 
//
//
//		break;
//	}
//
//} 