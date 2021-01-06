#include <xc.h>
#include "button_control.h"
#include <plib.h>


void init_buttons() {
    //Set ports as digital in
    PORTSetPinsDigitalIn(IOPORT_B, BIT_7 | BIT_8);
    
    //Button pullups
    CNPUB = 0x0180;
}

uint16_t read_on_button() {
    return PORTReadBits(IOPORT_B, BIT_7);
}

uint16_t read_tempo_button() {
    uint16_t result = PORTReadBits(IOPORT_B, BIT_8);
    return result;
}