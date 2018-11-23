package com.hzdongcheng.drivers.base.modbus.helper;


/**
 * 命令字对象
 * Created by Administrator on 2018/4/17.
 */

public class CmdChar {
    private int startAddr;//起始地址
    public final static int MAX_CMD_LENGTH = 40;//40个字
    private char[] data;
    public CmdChar(int startAddr, int length){
        if(length<1){
            length =1;
        }else if(length > MAX_CMD_LENGTH){
            length = MAX_CMD_LENGTH;
        }
        data = new char[length];
        this.startAddr = startAddr;
    }

    public CmdChar(CmdChar cmdChar){
        this.startAddr = cmdChar.getStartAddr();
        data = new char[cmdChar.getLength()];
        for(int i=0; i<data.length; i++){
            data[i] = cmdChar.getCmdChar(i+1);
        }
    }
    public String getCmdCharString(){
        StringBuffer strBuff = new StringBuffer();
        for(int value: data){
            strBuff.append(String.format("%x",value)).append(" ");
        }
        return strBuff.toString();
    }
    public int getStartAddr() {
        return startAddr;
    }

    private char _getCmdChar(int hight, int low){
        int value = ((hight<<8)&0xff00);
        value += (low&0xff);
        return (char)value;
    }
    private byte _getHightByte(char value){
        return (byte)((value>>8)&0xff);
    }
    private byte _getLowByte(char value){
        return (byte)value;
    }

    /**
     * 取指定命令字
     * @param iChar 1开始
     * @return
     */
    public char getCmdChar(int iChar){
        if(iChar>0 && iChar <=data.length){
            return data[iChar-1];
        }else{
            return 0;
        }
    }

    /**
     * 取指定命令字
     * @param iChar 1开始
     * @return
     */
    public char getCharValue(int iChar){
        return getCmdChar(iChar);
    }

    /**
     * 取指定命令字的指定位
     * @param iChar 1开始
     * @param iBit  0开始
     * @return
     */
    public byte getBitValue(int iChar, int iBit){
        char value = getCmdChar(iChar);
        if(iBit>=0 && iBit< 16){
            return (byte)((value>>iBit)&0x1);
        }else{
            return 0;
        }
    }

    /**
     * 取指定命令字 的Short值
     * @param iChar
     * @return
     */
    public short getShortValue(int iChar){
        char value = getCmdChar(iChar);
        return (short)(value&0xffff);
    }
    /**
     * 取指定命令字 的Int值
     * @param iChar
     * @return
     */
    public int getIntValue(int iChar){
        char low = getCmdChar(iChar);
        char high = getCmdChar(iChar+1);
        int value = (low&0xffff);
        value += (high<<16)&0xffff0000;
        return  value;
    }

    /**
     * 修改指定命令字
     * @param iChar  1开始
     * @param value
     */
    public void modCmdChar(int iChar, char value){
        if(iChar>0 && iChar <=data.length){
            data[iChar-1] = value;
        }
    }

    /**
     * 修改指定命令字 - 指定Bit置1
     * @param iChar  1开始
     * @param iBit   0开始
     */
    public void modCmdCharBit(int iChar, int iBit){
        if(iBit>=0 && iBit< 16){
            char cChar =getCmdChar(iChar);
            cChar |= (1<<iBit);
            modCmdChar(iChar, cChar);
        }
    }
    /**
     * 修改指定命令字 - 指定Bit置1
     * @param iChar  1开始
     * @param iBit   0开始
     * @param  value 0 or 1
     */
    public void modCmdCharBit(int iChar, int iBit, int value){
        if(iBit>=0 && iBit< 16){
            char cChar =getCmdChar(iChar);
            if(value == 0){
                cChar &= ~(1<<iBit);
            }else{
                cChar |= (1<<iBit);
            }
            modCmdChar(iChar, cChar);
        }
    }

    /**
     * 修改指定命令字
     * @param iChar  1开始
     * @param hight
     * @param low
     */
    public void modCmdChar(int iChar, int hight, int low){
        modCmdChar(iChar, _getCmdChar(hight, low));
    }

    public char[] getCmdChars(){
        return data;
    }
    public int getLength(){
        return data.length;
    }

}
