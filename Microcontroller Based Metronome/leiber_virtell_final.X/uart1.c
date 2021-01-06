/* 
 * File:   uart1.c
 * Author: joshu
 *
 * Created on September 20, 2020, 2:48 PM
 */

#include <plib.h>
#include <inttypes.h>
#include "uart1.h"

void uart1_init(uint32_t baudrate) {
    ANSELA = 0; //Set A to be digital

    //Port A is a digital in for RX
   // PPSInput(3, U1RX, RPA4);

    //Port A is a digital out for TX
    PPSOutput(1, RPB4, U1TX);

    //Configure UART1 to receive and transmit with given baudrate
    OpenUART1(UART_EN, UART_RX_ENABLE | UART_TX_ENABLE, baudrate);
}

uint8_t uart1_txrdy() {
    //If BusyUART1 returns a non-zero value, then UART1 is not ready
    if (BusyUART1()) {
        return 0x00;
    } else { //Otherwise, it is ready
        return 0x01;
    }
}

void uart1_txwrite(uint8_t c) {
    if (uart1_txrdy()) WriteUART1(c);
}

void uart1_txwrite_str(char *c) {
    char tc;
    while (*c != '\0') {
        tc = *c;
        if (uart1_txrdy()) {
            uart1_txwrite(tc);
            c++;
        }
    }
}

uint8_t uart1_rxrdy() {
    return DataRdyUART1();
}

uint8_t uart1_rxread() {
    uint8_t result = 0x00;
    if (uart1_rxrdy()) {
        result = ReadUART1();
    }
    return result;
}

////Unit tests for uart transmission
//void main() {
//    uart1_init(22726);
//
//    char greeting [] = "Hello World! This is Josh and Jordan coming at you from the live world of UART\n\n\r";
//    char input;
//    
//    //Print a greeting
//    uart1_txwrite_str(greeting);
//    
////    //Print the opposite case of what is typed into the terminal
////    while (1) {
////        if (uart1_rxrdy()) {
////            input = uart1_rxread();
////
////            if (input > 96) { //The value is lower case
////                uart1_txwrite(input - 32);  //Lower case is 32 larger than upper
////            } else {
////                uart1_txwrite(input + 32);
////            }
////        }
////    }
//
//    //Perform test from input strings from a single char to 100 chars
//    char one_character []= "z\n\n\r";
//    char twenty_six_chars [] = "why don't we do words-josh\n\n\r";
//    char fifty_chars [] = "the quick brown fox jumped over the lazy river ye\n\n\r";
//    char hundred_chars [] = "lets write a poem about professor nestor. professor nestor professor nestor, I sure hope we're on campus next semester\n\n\r";
//    
//    uart1_txwrite_str(one_character);
//    uart1_txwrite_str(twenty_six_chars);
//    uart1_txwrite_str(fifty_chars);
//    uart1_txwrite_str(hundred_chars);
//}