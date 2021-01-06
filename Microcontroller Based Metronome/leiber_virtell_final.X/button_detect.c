
#include <inttypes.h>

#include "calculator.h"
#include "draw_main_screen.h"


uint8_t screen_pressed(uint8_t pressed, uint16_t x, uint16_t y) {
    
    //If you pressed the TAP button
    if(pressed && x > 10 && x < 10 + boxw && y > 220 - (boxh) && y < 220) {
        return 1;
    }else if(pressed && x > 320 - boxw - 10 && x < 310 && y > 220 - (boxh) && y < 220){  //You pressed the SLIDE Button
        return 2;
    }else{ //If you're not touching the screen...dont return anything
        return 0;
    }
}
 