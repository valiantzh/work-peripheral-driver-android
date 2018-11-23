package com.hzdongcheng.drivers.base.modbus.exception;

import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ModbusException extends DcdzSystemException {
    public ModbusException(int errorCode) {
        super(errorCode);
    }

    public ModbusException(int errorCode, String message) {
        super(errorCode, message);
    }

    public ModbusException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    @Override
    public int getErrorCode() {
        return super.getErrorCode();
    }

    @Override
    public String getErrorTitle() {
        return super.getErrorTitle();
    }
}
