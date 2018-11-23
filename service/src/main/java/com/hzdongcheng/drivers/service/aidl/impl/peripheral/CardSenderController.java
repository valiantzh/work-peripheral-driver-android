package com.hzdongcheng.drivers.service.aidl.impl.peripheral;

import android.os.RemoteException;

import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.peripheral.cardsender.ICardSenderController;
import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.cardsender.ICardSender;
import com.hzdongcheng.drivers.peripheral.cardsender.model.CardSenderStatus;

/**
 * Created by Administrator on 2018/3/11.
 */

public class CardSenderController extends ICardSenderController.Stub{
    private final Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());
    private ICardSender cardSender;
    public CardSenderController(ICardSender cardSender){
        this.cardSender = cardSender;
    }

    @Override
    public Result resetCardSender() throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][CardSenderCtrl]==>resetCardSender");
        try{
            cardSender.resetCardSender();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][CardSenderCtrl] ==>resetCardSender -->Success");
        } else {
            log4jUtils.error("[Service][CardSenderCtrl] ==>resetCardSender -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][CardSenderCtrl] ==>resetCardSender耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result queryStatus() throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][CardSenderCtrl]==>queryStatus");
        try {
            CardSenderStatus cardSenderStatus = cardSender.queryStatus();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(cardSenderStatus));
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][CardSenderCtrl] ==>queryStatus -->Success");
        } else {
            log4jUtils.error("[Service][CardSenderCtrl] ==>queryStatus -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][CardSenderCtrl] ==>queryStatus耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result moveCard2ReadPosition() throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][CardSenderCtrl]==>moveCard2ReadPosition");
        try{
            cardSender.moveCard2ReadPosition(false);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][CardSenderCtrl] ==>moveCard2ReadPosition -->Success");
        } else {
            log4jUtils.error("[Service][CardSenderCtrl] ==>moveCard2ReadPosition -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][CardSenderCtrl] ==>moveCard2ReadPosition耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result moveRFCard2ReadPosition() throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][CardSenderCtrl]==>moveRFCard2ReadPosition");
        try{
            cardSender.moveCard2ReadPosition(true);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][CardSenderCtrl] ==>moveRFCard2ReadPosition -->Success");
        } else {
            log4jUtils.error("[Service][CardSenderCtrl] ==>moveRFCard2ReadPosition -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][CardSenderCtrl] ==>moveRFCard2ReadPosition耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result moveCard2Front() throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][CardSenderCtrl]==>moveCard2Front");
        try{
            cardSender.moveCard2Front();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][CardSenderCtrl] ==>moveCard2Front -->Success");
        } else {
            log4jUtils.error("[Service][CardSenderCtrl] ==>moveCard2Front -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][CardSenderCtrl] ==>moveCard2Front耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result recycleCard() throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][CardSenderCtrl]==>recycleCard");
        try{
            cardSender.recycleCard();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][CardSenderCtrl] ==>recycleCard -->Success");
        } else {
            log4jUtils.error("[Service][CardSenderCtrl] ==>recycleCard -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][CardSenderCtrl] ==>recycleCard耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }
}
