//
// Created by tony.zhongli on 2018/2/12.
//

#ifndef ALXDRIVERS_MODBUS4J_H
#define ALXDRIVERS_MODBUS4J_H

#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <list>
#include <string.h>
#include <errno.h>

#include "modbus.h"

#define MAX_FRAME_LENGTH 128

class Modbus4j {

protected:
    Modbus4j();
    virtual ~Modbus4j();

public:
    static Modbus4j* createInstances();

public:
    void Release();

public:
    bool CreateMasterRTU(const char *device,
                                  int baud, char parity, int data_bit,
                                  int stop_bit);

    void FreeMasterRTU(void);
    void Close(void);

    bool SetSlave(int slave_id);

    int Read_bits(int addr, int nb, uint8_t *dest);
    int Read_input_bits(int addr, int nb, uint8_t *dest);
    int Read_registers(int addr, int nb, uint16_t *dest);
    int Read_input_registers(int addr, int nb, uint16_t *dest);
    int Write_bit(int coil_addr, int status);
    int Write_register(int reg_addr, int value);
    int Write_bits(int addr, int nb, const uint8_t *data);
    int Write_registers(int addr, int nb, const uint16_t *data);

    int Secure_write_registers(int addr, int nb, const uint16_t *data);

private:
    modbus_t* mod_context;
};


#endif //ALXDRIVERS_MODBUS4J_H
