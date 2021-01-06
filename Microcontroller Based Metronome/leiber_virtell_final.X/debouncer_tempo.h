/*
 * The purpose of this module is to establish all functions and variables used by the right debouncer
 */

/* 
 * File:   debouncer_right.h
 * Author: leibe
 *
 * Created on September 8, 2020, 4:09 PM
 */

#ifndef DEBOUNCER_TEMPO_H
#define	DEBOUNCER_TEMPO_H

    #include <inttypes.h>
    
    extern void init_debounce_tempo_FSM();
	extern uint8_t tickFct_debounce_tempo(uint16_t btn);

#endif





