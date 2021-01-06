/* 
 * File:   metronome_tempo_fsm.h
 * Author: leibe
 *
 * Created on November 9, 2020, 12:27 PM
 */

#ifndef METRONOME_TEMPO_FSM_H
#define	METRONOME_TEMPO_FSM_H

#include <inttypes.h>

extern void init_metronome_fsm();
extern uint8_t tick_metronome_fsm(uint8_t button_press); //making this move will be in the main

#endif	/* METRONOME_TEMPO_FSM_H */

