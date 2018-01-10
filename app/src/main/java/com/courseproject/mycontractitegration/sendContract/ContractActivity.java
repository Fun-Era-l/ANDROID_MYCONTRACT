package com.courseproject.mycontractitegration.sendContract;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.courseproject.mycontractitegration.R;
import com.courseproject.mycontractitegration.data.Contract;
import com.courseproject.mycontractitegration.data.source.local.ContractLocalDataSource;
import com.courseproject.mycontractitegration.showContractList.ContractAdapter;
import com.courseproject.mycontractitegration.showContractList.ContractListPresenter;
import com.courseproject.mycontractitegration.showContractList.ContractListVP;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;

public class ContractActivity extends AppCompatActivity implements ContractListVP.View{

    ContractListVP.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_list);
        //ContractListVP.Presenter mPresenter=new ContractListPresenter(ContractLocalDataSource().getInstance(),this);
        mPresenter=new ContractListPresenter(new ContractLocalDataSource().getInstance(),this);
        mPresenter.loadContractList();
        ListView contractListView=findViewById(R.id.lv_contract);
        Intent intent=getIntent();
        //MBmobIMConversation mBmobIMConversation=(MBmobIMConversation)intent.getSerializableExtra("conversation");
        //System.out.println(mBmobIMConversation.getConversationId().toString());
        //BmobIMConversation mBmobIMConversation=(BmobIMConversation)intent.getSerializableExtra("conversation");
        //System.out.println(mBmobIMConversation.getConversationId().toString()+"kkk");
        contractListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
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
