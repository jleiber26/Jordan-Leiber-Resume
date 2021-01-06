/* 
 * File:   slider_handle.h
 * Author: leibe
 *
 * Created on November 8, 2020, 6:21 PM
 */

#ifndef SLIDER_HANDLE_H
#define	SLIDER_HANDLE_H

#include <inttypes.h>

extern uint16_t bpm;

extern uint8_t box_startx, boxy;
extern uint16_t box_h, box_w;

extern void draw_slider_box();
extern void display_bpm(uint16_t xloc);
extern void move_slider(uint8_t pressed, uint16_t xloc);
extern uint16_t get_slider_bpm();


#endif	/* SLIDER_HANDLE_H */

