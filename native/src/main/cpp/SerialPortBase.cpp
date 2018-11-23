/*
 * CSerialPort.cpp
 *
 *  Created on: 2018/01/31
 */
#include "SerialPortBase.h"

CSerialPort::CSerialPort()
{
    m_hSerialPort = -1;
    m_tid = 0;
    m_bExit = false;
    m_bPause = false;
    m_uRef = 1;
    m_sErrorDesc  = "Unknown error.";
}

CSerialPort::~CSerialPort()
{

}

//添加引用计数
unsigned int CSerialPort::AddRef(void)
{
    return 1;
}

//减少引用计数
unsigned int CSerialPort::Release(void)
{
    delete this;
    return 0;
}

//打开串口
bool CSerialPort::Open(const char* szName)
{
    //非阻塞模式打开串口
    m_hSerialPort = open(szName, O_RDWR | O_NOCTTY | O_NDELAY);
    //阻塞模式打开串口
    //m_hSerialPort = open(szName, O_RDWR | O_NOCTTY);
    if (m_hSerialPort == -1)
    {
        GetLastErrorDesc();
        return false;
    }

    //设置为阻塞模式
    if ( fcntl(m_hSerialPort, F_SETFL, 0) < 0 )
    {
        close(m_hSerialPort);
        m_hSerialPort = -1;
        GetLastErrorDesc();
        return false;
    }

    //android下要有相应的读写权限
    int iRet = flock(m_hSerialPort, LOCK_EX | LOCK_NB );
    if (iRet != 0)
    {
        close(m_hSerialPort);
        m_hSerialPort = -1;

        GetLastErrorDesc();
        return false;
    }

    return true;
}

//检查串口是否已经打开
bool CSerialPort::IsOpen(void)
{
    m_sErrorDesc = "No error";

    bool bOK = m_hSerialPort == -1 ? false : true;
    if (!bOK)
        m_sErrorDesc = "The serial port did not open";

    return bOK;
}

//关闭串口
void CSerialPort::Close(void)
{
    m_sErrorDesc = "No error";

    if (IsOpen()) {
        CloseEventMode();

        //等待线程结束
        if(m_tid > 0){
					m_bExit = true;
					pthread_join(m_tid, NULL);
					FD_CLR(m_hSerialPort, &rfds);
					m_pEvent = NULL;
					m_tid = 0;
        }

        flock(m_hSerialPort, LOCK_UN);

        close(m_hSerialPort);
        m_hSerialPort = -1;
    }
}

//初始化串口
bool CSerialPort::Initialize(unsigned int dwBaudRate, unsigned char uPaity,
                                  unsigned char uStopBits, unsigned char uDataBytes)
{
    if (!IsOpen())
        return false;

    struct termios options;
    //Get the current options for the port...
    int iRet = tcgetattr(m_hSerialPort, &options);
    if (iRet == -1)
        return false;

    //Set the baud rates
    unsigned int speed_arr[] = {B115200, B57600, B38400, B19200, B9600,
    B4800, B2400, B1200, B600, B300, B110};

    unsigned int name_arr[]  = {115200, 57600, 38400, 19200, 9600,
    4800, 2400, 1200, 600, 300, 110};

    unsigned int count = sizeof(speed_arr) / sizeof(unsigned int);
    unsigned int index;
    for (index = 0; index < count; index++)
    {
        if (name_arr[index] == dwBaudRate)
            break;
    }

    if (index == count)
        return false;

    cfsetispeed(&options, speed_arr[index]);
    cfsetospeed(&options, speed_arr[index]);


    options.c_cflag &= ~CSIZE; /* Mask the character size bits */
    switch (uDataBytes)
    {
    case DCDZ_DATABITS7:
        options.c_cflag |= CS7;
        break;
    case DCDZ_DATABITS8:
        options.c_cflag |= CS8;
        break;
    default:
        break;
    }

    switch (uPaity)
    {
    case DCDZ_NOPARITY:
        options.c_cflag &= ~PARENB;    /* Clear parity enable */
        options.c_iflag &= ~INPCK;     /* Enable parity checking */
        break;
    case DCDZ_ODDPARITY:
        options.c_cflag |= (PARODD | PARENB);
        options.c_iflag |= INPCK;             /* Disnable parity checking */
        break;
    case DCDZ_EVENPARITY:
        options.c_cflag |= PARENB;     /* Enable parity */
        options.c_cflag &= ~PARODD;
        options.c_iflag |= INPCK;      /* Disnable parity checking */
        break;
    default:
        break;
    }

    switch (uStopBits)
    {
    case DCDZ_ONESTOPBIT:
        options.c_cflag &= ~CSTOPB;
        break;
    case DCDZ_TWOSTOPBITS:
        options.c_cflag |= CSTOPB;
       break;
    default:
        break;
    }

    //Clear read and write queue
    Clear();

    //Enable the receiver and set local mode...
    options.c_cflag |= (CLOCAL | CREAD);

    //有时候，在用write发送数据时没有键入回车，信息就发送不出去，
    //这主要是因为我们在输入输出时是按照规范模式接收到回车或换行才发送，
    //而更多情况下我们是不必键入回车或换行的。此时应转换到行方式输入，不经处理直接发送，设置如下：
    options.c_lflag  &= ~(ICANON | ECHO | ECHOE | ISIG);  /*Input*/
    options.c_oflag  &= ~OPOST;   /*Output*/

    //还存在这样的情况：发送字符0X0d的时候，往往接收端得到的字符是0X0a，
    //原因是因为在串口设置中c_iflag和c_oflag中存在从NL-CR和CR-NL的映射，
    //即串口能把回车和换行当成同一个字符，可以进行如下设置屏蔽之：
    options.c_iflag &= ~ (INLCR | ICRNL | IGNCR);
    options.c_oflag &= ~(ONLCR | OCRNL);

    //c_cc数组的VSTART和VSTOP元素被设定成DC1和DC3，代表ASCII标准的XON和XOFF字符，
    //如果在传输这两个字符的时候就传不过去，需要把软件流控制屏蔽，即：
    options.c_iflag &= ~(IXON | IXOFF | IXANY);

    //Set the new options for the port...
    iRet = tcsetattr(m_hSerialPort, TCSANOW, &options);
    return iRet == 0 ? true : false;
}

//设置读写缓冲区大小
bool CSerialPort::SetInOutQueue(unsigned int dwInQueue, unsigned int dwOutQueue)
{
    return true;
}

//设置读写超时时间
bool CSerialPort::SetReadWriteTimeouts(unsigned int dwReadTotalTimeoutConstant,
                                            unsigned int dwWriteTotalTimeoutConstant)
{
    if (!IsOpen())
        return false;

    struct termios options;
    //Get the current options for the port...
    int iRet = tcgetattr(m_hSerialPort, &options);
    if (iRet == -1)
        return false;

    options.c_cc[VTIME] = dwReadTotalTimeoutConstant; /* read timeout seconds*/
    options.c_cc[VMIN]  = 0; /* Update the options and do it NOW */

    iRet = tcsetattr(m_hSerialPort, TCSANOW, &options);

    return iRet == 0 ? true : false;
}

bool CSerialPort::Clear(void)
{
    if (!IsOpen())
        return false;

    int iRet = tcflush(m_hSerialPort, TCIOFLUSH);
    return iRet == 0 ? true : false;
}

//清除串口错误
bool CSerialPort::ClearError(void)
{
    return false;
}

//返回串口当前错误
const char* CSerialPort::ErrorString(void)
{
    return m_sErrorDesc.c_str();
}

//查看缓冲区有多少字节数据可以读
unsigned int CSerialPort::BytesInQueue(void)
{
    int bytes = 0;

    if (!IsOpen())
        return 0;

    if ( -1 == ioctl(m_hSerialPort, FIONREAD, &bytes))
    	bytes = 0;

    return bytes;
}

//打开事件监听模式
bool CSerialPort::OpenEventMode(ISerialPortEvent* pEvent)
{
    if (!IsOpen())
        return false;

    m_bPause = false;

    if (m_tid != 0)
        return true;

    m_bExit = false;

    int iRet = pthread_create(&m_tid, NULL, SerialPortCommThread, (void *)this);
    if(iRet == 0)
    {
        m_pEvent = pEvent;
        return true;
    }

    return false;
}

//关闭事件监听模式
void CSerialPort::CloseEventMode(void)
{
    if (m_tid != 0)
    {
        m_bPause = true;

        //m_bExit = true;
        //pthread_join(m_tid, NULL);
        //FD_CLR(m_hSerialPort, &rfds);
        //m_pEvent = NULL;
        //m_tid = 0;
    }
}

//
bool CSerialPort::Send(const char* szData, unsigned int uNumberOfBytesToSend, unsigned int* lpNumberOfBytesSend)
{
    if (!IsOpen())
        return false;

    int iRet = write(m_hSerialPort, szData, uNumberOfBytesToSend);
    if (iRet == -1)
         return false;

    *lpNumberOfBytesSend = iRet;
    return true;
}

//
bool CSerialPort::Recv(char* szData, unsigned int uNumberOfBytesToRecv, unsigned int* lpNumberOfBytesRecv)
{
    if (!IsOpen())
        return false;

    int iRet = read(m_hSerialPort, szData, uNumberOfBytesToRecv);
    if (iRet == -1)
        return false;

    *lpNumberOfBytesRecv = iRet;
    return true;
}

int CSerialPort::RecvFixLength(char* szData, unsigned int uNumberOfBytesToRecv)
{
    if (!IsOpen())
        return -4;

    int iRet = 0;

    unsigned int totalLen = uNumberOfBytesToRecv;
    unsigned int recvLen  = 0;

    do
    {
    	ssize_t bytes = read(m_hSerialPort, &szData[recvLen], totalLen - recvLen);

        if (bytes == -1)
        {
            iRet = -4;
            break;
        }

        if (bytes == 0)
        {
            iRet = -2;
            break;
        }

        recvLen += bytes;
    }while(totalLen != recvLen);

    return iRet;
}

unsigned int CSerialPort::GetLastErrorDesc()
{
    m_sErrorDesc = (const char*)strerror(errno);
    return errno;
}

void* CSerialPort::SerialPortCommThread(void* params)
{
    CSerialPort* lpThis = static_cast<CSerialPort*>(params);

    LOGD("handle begin.");

    lpThis->Handle();

    LOGD("handle end.");

    return NULL;
}

void CSerialPort::Handle()
{
    timeval timeouts={1,0};

    do
    {
        memset (&rfds, 0, (unsigned long)sizeof (rfds));
        FD_SET(m_hSerialPort, &rfds);
        timeouts.tv_sec = 1;
        timeouts.tv_usec = 0;

        int iRet = select(1 + m_hSerialPort, &rfds, NULL, NULL, &timeouts);

        if (iRet < 0 || m_bExit){
        	LOGD("Serial port error. m_bExit over");
            break;
        }

        if (iRet == 0 || m_bPause)
            continue;

        //有数据可以读
        if (FD_ISSET(m_hSerialPort, &rfds))
        {
            unsigned int bytes = BytesInQueue();

            char* szData = new char[bytes + 1];
            bzero(szData, bytes + 1);

            bool bOK = Recv(szData, bytes, &bytes);
            if (bOK && m_pEvent != NULL)
                m_pEvent->fire_HandleData(szData, bytes);
            else
                Clear();

            string code = "";
                for (unsigned int i = 0; i < bytes; i++){
                    char tmp[3] = {0};
                    sprintf(tmp, "%02x", szData[i]);
                    code += tmp;
                }
                LOGD("bytes = %d, %s", bytes, code.c_str());

            delete [] szData;
        }

    }while(true);

    m_tid = 0;

    //异常退出
    if (m_pEvent != NULL) {
    	if (!m_bExit){
    		m_pEvent->fire_HandleError("Serial port error. event thread exit is non-normal.");
    	}
    }

    LOGD("thread exit.");
    pthread_exit((void *)0);
}