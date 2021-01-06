/* 
 * File:   button_tempo_controller.h
 * Author: leibe
 *
 * Created on November 23, 2020, 12:28 PM
 */

#ifndef BUTTON_TEMPO_CONTROLLER_H
#define	BUTTON_TEMPO_CONTROLLER_H

extern void init_button_fsm();
extern uint32_t tickFct_button(uint8_t pressed, uint16_t curr_time);

#endif	/* BUTTON_TEMPO_CONTROLLER_H */

