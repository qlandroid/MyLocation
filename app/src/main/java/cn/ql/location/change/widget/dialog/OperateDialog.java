package cn.ql.location.change.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import cn.ql.location.change.R;

/**
 * Created by Administrator on 2017-4-14.
 */
public class OperateDialog {

    private AlertDialog mDialog;
    private OnOperateClickListener mListener;
    public OperateDialog(Context context,OnOperateClickListener l){
        this.mListener = l;
        init(context);

    }
    private void init(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog = li.inflate(R.layout.dialog_operate,null,false);
        builder.setView(dialog).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mDialog.cancel();

            }
        }).setNegativeButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.clickOperate();
                mDialog.cancel();
            }
        }).setTitle("设置权限");


        mDialog = builder.create();
    }

    public void show(){
        mDialog.show();
    }
    public void cancel(){
        mDialog.cancel();
    }

    public interface OnOperateClickListener{
        void clickOperate();
    }
}
