/* 
 * File:   debounce_on_off.h
 * Author: leibe
 *
 * Created on November 25, 2020, 1:11 PM
 */

#ifndef DEBOUNCE_ON_OFF_H
#define	DEBOUNCE_ON_OFF_H

  #include <inttypes.h>
    
    extern void init_debounce_on_off_FSM();
	extern uint8_t tickFct_debounce_on_off(uint16_t btn);

#endif	/* DEBOUNCE_ON_OFF_H */

