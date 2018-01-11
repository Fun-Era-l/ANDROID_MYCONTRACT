package com.courseproject.mycontractitegration.sendContract;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**
 * Created by 27827 on 2018/1/10.
 */

public class ContractMessage extends BmobIMExtraMessage {
    @Override
    public String getMsgType() {
        return "contract";
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    public ContractMessage(){}
}
