package com.courseproject.mycontractitegration.showContractList

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.courseproject.mycontractitegration.R
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import cn.bmob.newim.BmobIM
import cn.bmob.newim.core.ConnectionStatus
import cn.bmob.newim.listener.ConnectListener
import cn.bmob.newim.listener.ConnectStatusChangeListener
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import com.courseproject.mycontractitegration.orderManagement.OrderListActivity
import com.courseproject.mycontractitegration.addeditcontract.AddEditContractActivity
import com.courseproject.mycontractitegration.showTemplateList.TemplateListActivity
import com.courseproject.mycontractitegration.data.Contract
import com.courseproject.mycontractitegration.data.source.repository.ContractLocalDataSource
import com.courseproject.mycontractitegration.friendManage.FriendListActivity
import com.courseproject.mycontractitegration.showSignatureList.SignatureListActivity
import kotlinx.android.synthetic.main.contract_list_act.*

class ContractListActivity : AppCompatActivity(),ContractListVP.View, NavigationView.OnNavigationItemSelectedListener {
    lateinit var mPresenter:ContractListVP.Presenter
    lateinit var cur_user:BmobUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contract_list_act)
        setSupportActionBar(contract_list_toolbar)
        /*
        获取当前用户并改动navigation drawer中的用户信息
         */
        cur_user = BmobUser.getCurrentUser()
        val nav_header:View = LayoutInflater.from(this@ContractListActivity).inflate(R.layout.nav_header_entrance,null)
        var nav_username = nav_header.findViewById<TextView>(R.id.user_name)
        nav_username.setText(cur_user.username)
        val nav_phonenumber = nav_header.findViewById<TextView>(R.id.phone_number)
        nav_phonenumber.setText(cur_user.mobilePhoneNumber)
        contract_list_nav_view.addHeaderView(nav_header)

        mPresenter = ContractListPresenter(ContractLocalDataSource().getInstance(),this)
        mPresenter.loadContractList()
/*
进入contractlist首页后，开启bmobIm 连接，随时等待接收消息与合同
 */
        val user = BmobUser.getCurrentUser()
        BmobIM.connect(user.mobilePhoneNumber, object : ConnectListener() {
            override fun done(s: String, e: BmobException?) {
                if (e == null) {
                    Log.i("TAG", "服务器连接成功")
                } else {
                    Log.i("TAG", "服务器连接失败")
                    Toast.makeText(this@ContractListActivity, "无网络 将无法发送消息", Toast.LENGTH_SHORT).show()
                }
            }
        })

        //监听连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(object : ConnectStatusChangeListener() {
            override fun onChange(connectionStatus: ConnectionStatus) {
                Toast.makeText(this@ContractListActivity, connectionStatus.msg, Toast.LENGTH_SHORT).show()
                Log.i("连接状态：", BmobIM.getInstance().currentStatus.msg)
            }
        })

        contract_list.setOnItemClickListener(object: AdapterView.OnItemClickListener
        {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(id.toInt() == -1) {
                    Log.d("id-positionError", "ClickOnHeader")
                }
                val realPosition:Int = id.toInt()
                val item : Contract? = parent?.getItemAtPosition(realPosition) as? Contract
                val contractList2addEdit:Intent = Intent(this@ContractListActivity, AddEditContractActivity::class.java)
                contractList2addEdit.putExtra("ContractToEdit",item)
                startActivity(contractList2addEdit)
            }
        })
        add.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                val contractList2TemplateSelect: Intent = Intent(this@ContractListActivity, TemplateListActivity::class.java)
                startActivity(contractList2TemplateSelect)
            }
        })
        val toggle = ActionBarDrawerToggle(
                this,  contract_list_drawer_layout, contract_list_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        contract_list_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        contract_list_nav_view.setNavigationItemSelectedListener(this)
    }


    override fun showContractList(tempList: List<Contract>) {
        val mContractAdapter = ContractAdapter(this,R.layout.contract_item,tempList)
        contract_list.adapter = mContractAdapter;

    }

    override fun showEmptyListWarning() {}
    override fun setPresenter(presenter: ContractListVP.Presenter) {
        mPresenter = presenter
    }

/*
重写返回键功能
 */
    override fun onBackPressed() {
        if ( contract_list_drawer_layout.isDrawerOpen(GravityCompat.START)) {
             contract_list_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.entrance, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }
/*
重载drawer菜单项目监听
 */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
                R.id.nav_signature -> {
                   val nav2SignatureList:Intent = Intent(this@ContractListActivity,SignatureListActivity::class.java)
                    startActivity(nav2SignatureList)
                }
                R.id.nav_templates -> {
                    val nav2TemplateList:Intent = Intent(this@ContractListActivity,TemplateListActivity::class.java)
                    startActivity(nav2TemplateList)

                }
                R.id.nav_contacts -> {
                    val nav2FriendList:Intent = Intent(this@ContractListActivity,FriendListActivity::class.java)
                    startActivity(nav2FriendList)
                }
                R.id.nav_order -> {
                    val nav2SignatureList:Intent = Intent(this@ContractListActivity,OrderListActivity::class.java)
                    startActivity(nav2SignatureList)
                }
            else->{}

            }
        contract_list_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
