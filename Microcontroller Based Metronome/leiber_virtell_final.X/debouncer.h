/* 
 * File:   debouncer.h
 * Author: joshu
 *
 * Created on October 11, 2020, 1:42 PM
 */

#ifndef DEBOUNCER_H
#define	DEBOUNCER_H
#include <inttypes.h>

extern void init_debounce_FSM();

extern uint8_t tickFct_debounce(uint8_t z);

#endif	/* DEBOUNCER_H */

