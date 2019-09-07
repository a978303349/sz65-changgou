package com.changgou.Service;

public interface UserInfoService {

    /**
     * 账户金额递减
     * @param username
     * @param money
     */
    void decrMoney(String username,int money);
}
