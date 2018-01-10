package com.courseproject.mycontractitegration.sendContract;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.courseproject.mycontractitegration.R;
import com.courseproject.mycontractitegration.data.Contract;
import com.courseproject.mycontractitegration.data.Msg;
import com.courseproject.mycontractitegration.data.source.local.ContractLocalDataSource;
import com.courseproject.mycontractitegration.friendManage.FriendModel;
import com.courseproject.mycontractitegration.friendManage.HomepageActivity;
import com.courseproject.mycontractitegration.showContractList.ContractAdapter;
import com.courseproject.mycontractitegration.showContractList.ContractListActivity;
import com.courseproject.mycontractitegration.showContractList.ContractListPresenter;
import com.courseproject.mycontractitegration.showContractList.ContractListVP;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ContractActivity extends AppCompatActivity implements ContractListVP.View{

    ContractListVP.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Contract contract=new Contract();
        contract.setTitle("ttttest");
        contract.setContent("fhuerewgafdsfasdgfgdfgdsfas");
        contract.save();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_list);
        mPresenter=new ContractListPresenter(new ContractLocalDataSource().getInstance(),this);
        mPresenter.loadContractList();
        ListView contractListView=findViewById(R.id.lv_contract);
        //创建会话
        Intent intent=getIntent();
        String name=intent.getStringExtra("fName");
        final String phone= FriendModel.getInstance().nameToPhone(name);
        contractListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取contract
                int realPosition=(int) id;
                final Contract contract=(Contract) parent.getItemAtPosition(realPosition);


                BmobQuery<BmobUser> query=new BmobQuery<BmobUser>();
                query.addWhereEqualTo("mobilePhoneNumber",phone);
                query.findObjects(new FindListener<BmobUser>() {
                    @Override
                    public void done(List<BmobUser> list, BmobException e) {
                        if (e==null){
                            if (list.size()>0){
                                final BmobIMUserInfo info = new BmobIMUserInfo();
                                info.setName(list.get(0).getUsername());
                                info.setUserId(list.get(0).getMobilePhoneNumber());
                                BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                                    @Override
                                    public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                                        if (e==null){
                                            BmobIMConversation messageManager=BmobIMConversation.obtain(BmobIMClient.getInstance(),bmobIMConversation);
                                            ContractMessage msgContract=new ContractMessage();
                                            //设置内容
                                            msgContract.setContent(contract.getTitle().toString()+"+"+contract.getContent().toString());
                                            messageManager.sendMessage(msgContract, new MessageSendListener() {
                                                @Override
                                                public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                                                    if (e==null){
                                                        Toast.makeText(ContractActivity.this,"已发送",Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(ContractActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(ContractActivity.this,"手机号为"+phone+"的用户不存在",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

            }
        });
    }

    @Override
    public void showContractList(@NotNull List<Contract> tempList) {
        ContractAdapter mContractAdapter=new ContractAdapter(this,R.layout.contract_item,tempList);
        ListView mContractlistview=findViewById(R.id.lv_contract);
        mContractlistview.setAdapter(mContractAdapter);
    }

    @Override
    public void setPresenter(ContractListVP.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void showEmptyListWarning() {

    }
}
