#include <math.h>
#include <stdint.h>

#include <plib.h>

#include <inttypes.h>

#include "dac4822.h"
#include "dds_fsm.h"

#define TABLE_SIZE 64

#define SYSCLK	40000000UL
#define PBCLK	40000000UL

int16_t sineTable[TABLE_SIZE];
int16_t triangleTable[TABLE_SIZE];

uint8_t counter;

uint8_t i;

static enum dds_States {
    INIT, UPDATE, OFF
} dds_state;

void init_dds_fsm() {
    dds_state = INIT;
}

void generateTables(void) {
    uint8_t i;
    for (i = 0; i < TABLE_SIZE; i++) {
        sineTable[i] = (short) (2047.0 * sin(6.2832 * ((float) i) / (float) TABLE_SIZE));
        sineTable[i] = sineTable[i] + 2047;

        if (i < TABLE_SIZE / 2) {
            triangleTable[i] = (i * (4096 / (TABLE_SIZE / 2))); // increasing part
        } else {
            triangleTable[i] = triangleTable[(TABLE_SIZE / 2 - 1)-(i - TABLE_SIZE / 2)]; // mirror to get decreasing part
        }
    }
}

void tickFct_dds(uint8_t button) {

    //Switch statement 1 for state transition
    switch (dds_state) {
        case INIT:
            init_dac4822();
            generateTables();

            dds_state = UPDATE;
            break;

        case UPDATE:
            if (button) {
                dds_state = OFF;
            } else {
                dds_state = UPDATE;
            }
            break;

        case OFF:
            if (button) {
                dds_state = UPDATE;
            } else {
                dds_state = OFF;
            }
            break;
    }

    //Switch statement 2 for output (here the output is the address of the buffer)
    switch (dds_state) {
        case INIT:
            break;

        case UPDATE:
            
            for (i = 0; i < TABLE_SIZE; i++) {
                write_dac4822(0, sineTable[counter]);
            }

//            counter++;
//            if (counter >= TABLE_SIZE) {
//                counter = 0;
//            }
            mPORTBToggleBits(BIT_5);


            break;

        case OFF:
            write_dac4822(0, 0);
            break;
    }

}

