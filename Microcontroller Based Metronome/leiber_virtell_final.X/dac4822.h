/* 
 * File:   dac4822.h
 * Author: nestorj ((Derived from example code by Syed Tahmid Mahbub)
 * See: http://tahmidmc.blogspot.com/2014/10/pic32-spi-using-mcp4822-12-bit-serial.html
 * Created on October 27, 2020, 2:31 PM
 */

#ifndef DAC4822_H
#define	DAC4822_H

#include <xc.h>
#include <plib.h>
#include <stdint.h>

// Set up the SPI interface and initialize the DAC
void init_dac4822(void);

// Write a data value to specified channel (0=A, 1=B))
void write_dac4822(uint16_t channel, uint16_t data);

#endif	/* DAC4822_H */

