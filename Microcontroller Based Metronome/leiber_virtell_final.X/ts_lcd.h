/* 
 * File:   ts_lcd.h
 * Author: joshu
 *
 * Created on October 2, 2020, 10:09 AM
 */

#ifndef TS_LCD_H
#define	TS_LCD_H
#include <inttypes.h>

extern uint8_t ts_lcd_get_ts(uint16_t *x, uint16_t *y);

extern void cursor_position(uint16_t x, uint16_t y, uint16_t z);

extern void ts_lcd_init();

#endif	/* TS_LCD_H */

