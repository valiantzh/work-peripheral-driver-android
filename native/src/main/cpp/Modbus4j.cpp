//
// Created by tony.zhongli on 2018/2/12.
//


#include "Modbus4j.h"
#include "SerialPortBase.h"

Modbus4j::Modbus4j()
{
    mod_context = NULL;
}

Modbus4j::~Modbus4j()
{
    if(mod_context != NULL){
        modbus_close(mod_context);
        modbus_free(mod_context);
    }
}

Modbus4j* Modbus4j::createInstances()
{
    Modbus4j* modbus4J = new Modbus4j();
    if (modbus4J == NULL)
        return NULL;

    return modbus4J;
}

void Modbus4j::Release()
{
    delete this;
}

bool Modbus4j::CreateMasterRTU(const char *device,
                            int baud, char parity, int data_bit,
                            int stop_bit)
{
    mod_context = modbus_new_rtu(device, baud, parity, data_bit, stop_bit);

    if (mod_context == NULL){
        LOGD("Unable to create the libmodbus context [%d]%s", errno, modbus_strerror(errno));
        return false;
    }

    modbus_set_response_timeout(mod_context, 5, 100000);
    modbus_set_error_recovery(mod_context,
                              modbus_error_recovery_mode(MODBUS_ERROR_RECOVERY_LINK | MODBUS_ERROR_RECOVERY_PROTOCOL));

    if (modbus_connect(mod_context) == -1) {
        LOGD("Failed to open serial port: %s [%d]%s", device, errno, modbus_strerror(errno));

        modbus_free(mod_context);
        return false;
    }

    LOGD("Opened Modbus port %s at baudrate %d", device, baud);

    return true;
}

void Modbus4j::FreeMasterRTU(void){
    if(mod_context != NULL)
        modbus_free(mod_context);
}

//关闭串口
void Modbus4j::Close(void)
{
    modbus_close(mod_context);
}

bool Modbus4j::SetSlave(int slave_id){
    int rc = modbus_set_slave(mod_context, slave_id);
    if(rc == -1){
        LOGD("Invalid slave id %d", slave_id);

        return false;
    }

    return true;
}

int Modbus4j::Read_bits(int addr, int nb, uint8_t *dest){
    int rc = modbus_read_bits(mod_context,addr,nb,dest);
    if(rc == -1){
        LOGD("Modbus IO failed [%d]%s with Read_bits: ", errno, modbus_strerror(errno));

        return -1;
    }

    return 0;
}

int Modbus4j::Read_input_bits(int addr, int nb, uint8_t *dest){
    int rc = modbus_read_input_bits(mod_context,addr,nb,dest);
    if(rc == -1){
        LOGD("Modbus IO failed [%d]%s with Read_input_bits: ", errno, modbus_strerror(errno));

        return -1;
    }

    return 0;
}

int Modbus4j::Read_registers(int addr, int nb, uint16_t *dest){
    int rc = modbus_read_registers(mod_context,addr,nb,dest);
    if(rc == -1){
        LOGD("Modbus IO failed [%d]%s with Read_registers: ", errno, modbus_strerror(errno));

        return -1;
    }

    return 0;
}

int Modbus4j::Read_input_registers(int addr, int nb, uint16_t *dest){
    int rc = modbus_read_input_registers(mod_context,addr,nb,dest);
    if(rc == -1){
        LOGD("Modbus IO failed [%d]%s with Read_input_registers: ", errno, modbus_strerror(errno));

        return -1;
    }

    return 0;
}
int Modbus4j::Write_bit(int coil_addr, int status){
    int rc = modbus_write_bit(mod_context,coil_addr,status);

    if(rc == -1){
        LOGD("Modbus IO failed [%d]%s with Write_bit: ", errno, modbus_strerror(errno));

        return -1;
    }

    return 0;
}

int Modbus4j::Write_register(int reg_addr, int value){
    int rc = modbus_write_register(mod_context,reg_addr,value);

    if(rc == -1){
        LOGD("Modbus IO failed [%d]%s with Write_register: ", errno, modbus_strerror(errno));

        return -1;
    }

    return 0;
}

int Modbus4j::Write_bits(int addr, int nb, const uint8_t *data){
    int rc = modbus_write_bits(mod_context,addr,nb,data);

    if(rc == -1){
        LOGD("Modbus IO failed [%d]%s with Write_bits: ", errno, modbus_strerror(errno));

        return -1;
    }

    return 0;
}

int Modbus4j::Write_registers(int addr, int nb, const uint16_t *data){
    int rc = modbus_write_registers(mod_context,addr,nb,data);

    if(rc == -1){
        LOGD("Modbus IO failed [%d]%s with Write_registers: ", errno, modbus_strerror(errno));

        return -1;
    }

    return 0;
}

int Modbus4j::Secure_write_registers(int addr, int nb, const uint16_t *data){

    // read out after write, and compare the result to ensure write correct
    int rc = modbus_write_registers(mod_context,addr,nb,data);

    if(rc == -1){
        LOGD("Modbus IO failed [%d]%s with Secure_write_registers0: ", errno, modbus_strerror(errno));

        return -1;
    }else{
        usleep(200000);//us-微秒  200000us=200ms  sleep(5); 5秒
        uint16_t readed_buffer[MAX_FRAME_LENGTH];

        rc = modbus_read_registers(mod_context, addr, min(nb, MAX_FRAME_LENGTH), readed_buffer);

        if (rc == -1) {
            LOGD("Modbus IO failed [%d]%s with Secure_write_registers1: ", errno, modbus_strerror(errno));
            return -1;
        } else if (memcmp(data, readed_buffer, 2*nb) != 0) {
            LOGD("compare the result !=0 ");

            return -1;
        }
    }

    return 0;
}