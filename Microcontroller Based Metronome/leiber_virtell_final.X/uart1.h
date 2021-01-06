/* 
 * File:   uart1.h
 * Author: joshu
 *
 * Created on September 20, 2020, 2:48 PM
 */


#ifndef UART1_H
#define	UART1_H
#include <inttypes.h>

extern void uart1_init(uint32_t baudrate);

extern uint8_t uart1_txrdy();

extern void uart1_txwrite(uint8_t c);

extern void uart1_txwrite_str(char *c);

extern uint8_t uart1_rxrdy();

extern uint8_t uart1_rxread();

#endif	/* UART1_H */

