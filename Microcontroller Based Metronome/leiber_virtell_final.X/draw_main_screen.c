
#include "Adafruit_2_4_LCD_Serial_Library/tft_master.h"
#include "Adafruit_2_4_LCD_Serial_Library/tft_gfx.h"
#include "ts_lcd.h"
#include <inttypes.h>

uint16_t boxw, boxh;

char button_chars[20];

static char buffer[30];

void drawMainScreen() {


    boxw = 320 / 5;
    boxh = 240 / 6;

    //Draw Tap button
    tft_fillRect(10, 220 - (boxh), boxw, boxh, ILI9341_RED);
    //Text
    tft_setCursor(10 + (boxw / 4), 220 - (boxh) + (boxh / 2) - (boxh / 8));
    tft_setTextColor(ILI9341_WHITE);
    tft_setTextSize(2);

    sprintf(buffer, "TAP");
    tft_writeString(buffer);

    //Draw Slider button
    tft_fillRect(320 - boxw - 10, 220 - (boxh), boxw, boxh, ILI9341_BLUE);
    //Text
    tft_setCursor((320 - boxw - 10) + (boxw / 14), 220 - (boxh) + (boxh / 2) - (boxh / 8));
    tft_setTextColor(ILI9341_CYAN);
    tft_setTextSize(2);

    sprintf(buffer, "SLIDE");
    tft_writeString(buffer);

    //ON/OFF indicator
    tft_setCursor(140, 185);
    tft_setTextColor(ILI9341_WHITE);
    tft_setTextSize(1);

    sprintf(buffer, "On | Off");
    tft_writeString(buffer);


    //Draw BPM:
    tft_setCursor(60, 95);
    tft_setTextColor(ILI9341_WHITE);
    tft_setTextSize(5);

    sprintf(buffer, "BPM: ");
    tft_writeString(buffer);

    //Draw Title
    tft_setCursor(80, 10);
    tft_setTextColor(ILI9341_YELLOW);
    tft_setTextSize(3);

    sprintf(buffer, "\"Doc\"tor Time");
    tft_writeString(buffer);

}
