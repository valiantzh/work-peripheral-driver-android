package com.dcdz.drivers.demo.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.dcdz.drivers.BR;

/**
 * Created by LJW on 2018/3/9.
 */

public class CardReadModel extends BaseObservable {

    private String cardNo;//卡号
    private String names;//姓名
    private String address;//地址
    private String national;//民族
    private String gender;//性别
    private String dateOfBirth;//出生日期
    private String sendLicenseAgencies;//发证机关
    private String dateOfRegistration;//办证日期
    private String dateOfEnd;//截止日期

    @Bindable
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
        notifyPropertyChanged(BR.cardNo);
    }

    @Bindable
    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
        notifyPropertyChanged(BR.names);
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
        notifyPropertyChanged(BR.national);
    }

    @Bindable
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    @Bindable
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        notifyPropertyChanged(BR.dateOfBirth);
    }

    @Bindable
    public String getSendLicenseAgencies() {
        return sendLicenseAgencies;
    }

    public void setSendLicenseAgencies(String sendLicenseAgencies) {
        this.sendLicenseAgencies = sendLicenseAgencies;
        notifyPropertyChanged(BR.sendLicenseAgencies);
    }

    @Bindable
    public String getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(String dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
        notifyPropertyChanged(BR.dateOfRegistration);
    }

    @Bindable
    public String getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(String dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
        notifyPropertyChanged(BR.dateOfEnd);
    }
}
