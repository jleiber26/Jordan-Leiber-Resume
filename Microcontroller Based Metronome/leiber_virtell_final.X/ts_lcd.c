/* 
 * File:   ts_lcd.c
 * Author: joshu
 *
 * Created on October 2, 2020, 10:09 AM
 */

#include <inttypes.h>
#include "TouchScreen.h"
#include "Adafruit_2_4_LCD_Serial_Library/tft_master.h"
#include "Adafruit_2_4_LCD_Serial_Library/tft_gfx.h"



uint16_t interpolatex(uint16_t in) {
    uint32_t xmin = 160; //Josh 147; Jordan 160;
    uint32_t xmax = 899; //Josh 856 Jordan 899
    uint32_t sub = in - xmin;
    uint32_t mult = sub*240;
    uint32_t out = mult/(xmax-xmin);
    return out;
}

uint16_t interpolatey(uint16_t in) {
    uint16_t ymin = 140;//140; //Josh 366 Jordan 140
    uint16_t ymax = 963;//963; //Josh 955 Jordan 963
    return(((in-ymin)*(320 - 0))/(ymax-ymin))+0;
}

uint8_t ts_lcd_get_ts(uint16_t *x, uint16_t *y) {
    
    static struct TSPoint p;
    p.x = 0;
    p.y = 0;
    p.z = 0;
    getPoint(&p);
    
    if (p.z >= 60) {
        //tft_drawRect(160, 120,100,100, ILI9341_RED);
        *y = interpolatex(p.x);
        *x = interpolatey(p.y);
        
        return p.z;
    }
    else {
        *x = 0;
        *y = 0;
        return 0;
    }
    
    
}

void cursor_position(uint16_t x, uint16_t y, uint16_t z) {
    char buffer[30];
    tft_setCursor(20, 20);
    tft_setTextColor(ILI9341_WHITE);
    tft_setTextSize(2);

    //erase old text
    tft_setTextColor(ILI9341_BLACK);
    tft_writeString(buffer);
    tft_setCursor(20, 20);
    
    tft_setTextColor(ILI9341_WHITE);
    sprintf(buffer, "x: %d, y: %d, z: %d", x, y, z);
    tft_writeString(buffer);
    
    tft_drawLine(x-5,y,x+5,y, ILI9341_GREEN);
    tft_drawLine(x,y-5,x,y+5, ILI9341_GREEN);
}

void ts_lcd_init() {
    //initialize screen
    tft_init_hw();
    tft_begin();
    tft_setRotation(3); 
    tft_fillScreen(ILI9341_BLACK);  
}