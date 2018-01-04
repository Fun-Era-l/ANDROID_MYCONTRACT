package com.courseproject.mycontractitegration.showSignatureList

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.courseproject.mycontractitegration.R
import com.courseproject.mycontractitegration.data.Signature


class SignatureAdapter(context: Context, itemResourceId: Int, objects:List<Signature>):
            ArrayAdapter<Signature>(context,itemResourceId,objects){
        var resourceId: Int = itemResourceId;

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var mSignture: Signature = getItem(position)
            var mView :View = LayoutInflater.from(context).inflate(resourceId,parent,false)
            var signatureImage:ImageView = mView.findViewById(R.id.sign_item_image)
            var signatureName: TextView =mView.findViewById(R.id.sign_item_name)
            val sig_bitmap:Bitmap? = mSignture.getSignatureBitmap()
            signatureImage.setImageBitmap(sig_bitmap)
            signatureName.setText(mSignture.signature_name)
            return mView;
        }
}
