// ICardSenderController.aidl
package com.hzdongcheng.drivers.peripheral.cardsender;
import com.hzdongcheng.drivers.bean.Result;

// Declare any non-default types here with import statements

interface ICardSenderController {
    //复位发卡器
    Result resetCardSender();
    //查询发卡机状态
    Result queryStatus();
    //移动卡片到IC读卡位置
    Result moveCard2ReadPosition();
    //移动卡片到RF读卡位置
    Result moveRFCard2ReadPosition();
    //移动卡到出卡口
    Result moveCard2Front();
    //回收卡
    Result recycleCard();
}
