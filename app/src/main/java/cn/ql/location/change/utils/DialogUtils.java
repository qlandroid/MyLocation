package cn.ql.location.change.utils;

import android.content.Context;

import cn.ql.location.change.widget.dialog.OperateDialog;

/**
 * Created by Administrator on 2017-4-14.
 */
public class DialogUtils {
    public static OperateDialog createOperateDialog(Context context, OperateDialog.OnOperateClickListener listener){
        OperateDialog dialog = new OperateDialog(context,listener);
        return dialog;
    }
}
