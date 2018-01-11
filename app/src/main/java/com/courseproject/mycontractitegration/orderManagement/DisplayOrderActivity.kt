package com.courseproject.mycontractitegration.orderManagement

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.CustomOrder
import com.courseproject.mycontractitegration.data.OrderBmob
import com.courseproject.mycontractitegration.issueHandler.IssueActivity
import kotlinx.android.synthetic.main.order_display_activity.*
import org.litepal.crud.DataSupport

class DisplayOrderActivity : AppCompatActivity() {
    private val MY_URL = "http://128.199.183.116"
    val CHARGE_URL = MY_URL
    val LIVEMODE = false
    private val CHANNEL_ALIPAY = "alipay"

    var mCurrentOrder: CustomOrder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_display_activity)
        setSupportActionBar(order_display_toolbar)
        mCurrentOrder = intent.getSerializableExtra("SelectedItem") as CustomOrder?
        if(mCurrentOrder != null) {
            order_display_name.setText(mCurrentOrder?.order_name)
            order_display_content.setText(mCurrentOrder?.order_detail)
            when(mCurrentOrder?.status) {
                0->order_display_status.setText("编辑中")
                1->{
                    order_display_status.setText("等待律师")
                }
                2->{
                    order_display_status.setText("律师代写中")
                    order_display_lawyer.setText(mCurrentOrder?.lawyer_name)
                }
                3->{
                    order_display_status.setText("已完成")
                    order_display_lawyer.setText(mCurrentOrder?.lawyer_name)
                }
                else->
                {

                    Log.d("ORDER STATUS ERROR","STATUS: ${mCurrentOrder?.status}")
                }
            }

        }
        order_display_toolbar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener {
/*
注释部分代码 - 提交订单后调用 ping ++ 集成支付功能api

 */
            override fun onMenuItemClick(item:MenuItem):Boolean {
                when (item.getItemId()) {
                    R.id.submit_order-> {
                        /*
                        用支付金额生成支付信息，调用paymenttast异步执行支付任务
                         */
                        /*
                        val amountText:String = "1000"
                        val replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).currency.getSymbol(Locale.CHINA))
                        val cleanString = amountText.replace(replaceable.toRegex(), "")
                        val amount = Integer.valueOf(BigDecimal(cleanString).toString())!!
                        val mPaymentTask = Paymenttask()
                        mPaymentTask.execute(PaymentRequest(CHANNEL_ALIPAY, amount,false))
                        */

                        val order = CustomOrder(order_display_name.text.toString(),order_display_content.text.toString())
                        val orderBmob = OrderBmob(order)
                        /*
                        订单状态为已提交，未处理
                         */
                        orderBmob.status = 1
                        orderBmob.save(object :SaveListener<String>(){
                            override fun done(p0: String?, p1: BmobException?) {
                                order.bmob_id = p0!!
                                order.save()
                                Toast.makeText(this@DisplayOrderActivity, "成功提交订单，请等待系统分配律师", Toast.LENGTH_SHORT).show();
                                startActivity(Intent(this@DisplayOrderActivity,OrderListActivity::class.java))
                            }
                        })
                    }
                   R.id.edit_order -> {
                       val order = CustomOrder(order_display_name.text.toString(),order_display_content.text.toString())
                       startActivity(Intent(this@DisplayOrderActivity,AddEditOrderActivity::class.java).putExtra("OrderToEdit",order))

                   }
                    R.id.delete_order->{
                        when(mCurrentOrder?.status){
                            0->{
                                DataSupport.delete(CustomOrder::class.java,mCurrentOrder?.id!!)
                                startActivity(Intent(this@DisplayOrderActivity,OrderListActivity::class.java))
                                Toast.makeText(this@DisplayOrderActivity,"成功删除订单",Toast.LENGTH_SHORT).show()
                            }
                            1->{
                                DataSupport.delete(CustomOrder::class.java,mCurrentOrder?.id!!)
                                val curOrder_Bmob =  OrderBmob()
                                curOrder_Bmob.objectId = mCurrentOrder?.bmob_id
                                curOrder_Bmob.delete(object :UpdateListener(){
                                    override fun done(p0: BmobException?) {
                                        if(p0 == null)
                                        {
                                            Toast.makeText(this@DisplayOrderActivity,"成功删除订单",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                })
                                startActivity(Intent(this@DisplayOrderActivity,OrderListActivity::class.java))
                            }
                            //取消正在执行的订单
                            2,3->{
                                Toast.makeText(this@DisplayOrderActivity,"无法直接取消该订单，自动跳转至订单申诉界面",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@DisplayOrderActivity,IssueActivity::class.java).putExtra("OrderToIssue",mCurrentOrder))
                            }
                        }
                    }
                    else-> {
                    }
                }
                return true;
            }
        });
    }
     override fun onCreateOptionsMenu(menu: Menu):Boolean {
       getMenuInflater().inflate(R.menu.menu_display_order, menu);
       return true;
    }
    /*
    /*
        Paymenttask 内部类, 访问服务器并调用ping++ 的activity
    */
    inner class Paymenttask:AsyncTask<PaymentRequest,Void,String>() {
        override fun doInBackground(vararg p0: PaymentRequest?): String? {
            val paymentRequest = p0[0]
            var data: String? = null
            try {
                val `object` = JSONObject()
                `object`.put("channel", paymentRequest?.channel)
                `object`.put("amount", paymentRequest?.amount)
                `object`.put("livemode", paymentRequest?.livemode)
                val json = `object`.toString()
                /*向 Ping++ Server SDK： 128.199.183.116 请求数据*/
                data = postJson(CHARGE_URL, json)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return data
        }

        override fun onPostExecute(data: String?) {
            if (null == data) {
                showMsg("请求出错", "请检查URL", "URL无法获取charge")
                return
            }
            Log.d("charge", data)
            /*
            调用Ping++支付功能，支付成功后回调onActivityResult()函数
            */
            //除QQ钱包外，其他渠道调起支付方式：
            //参数一：Activity  当前调起支付的Activity
            //参数二：data  获取到的charge或order的JSON字符串
            Pingpp.createPayment(this@DisplayOrderActivity, data)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data.extras!!.getString("pay_result")
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                val errorMsg = data.extras!!.getString("error_msg") // 错误信息
                val extraMsg = data.extras!!.getString("extra_msg") // 错误信息
                showMsg(result, errorMsg, extraMsg)
            }
        }
    }
/*
    显示支付结果和错误信息
*/
    fun showMsg(title: String?, msg1: String?, msg2: String?) {
        var str = title
        if (null != msg1 && msg1.length != 0) {
            str += "\n" + msg1
        }
        if (null != msg2 && msg2.length != 0) {
            str += "\n" + msg2
        }
        val builder = AlertDialog.Builder(this@DisplayOrderActivity)
        builder.setMessage(str)
        builder.setTitle("提示")
        builder.setPositiveButton("OK", null)
        builder.create().show()
    }

    /**
     * 访问服务器获取charge
     * @param urlStr charge_url
     * @param json 获取charge的传参
     * @return charge
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun postJson(urlStr: String, json: String): String? {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 8000
        conn.readTimeout = 8000
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true
        conn.doInput = true
        conn.outputStream.write(json.toByteArray())

        if (conn.responseCode == 200) {
            val reader = BufferedReader(InputStreamReader(conn.inputStream, "utf-8"))
            val response = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                response.append(line)
                line = reader.readLine()
            }
            return response.toString()
        }
        return null
    }
    */

}
