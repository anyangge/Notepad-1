package xiaoguang.notepad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xiaoguang.tools.BitmapTools;
import xiaoguang.tools.Constants;
import xiaoguang.tools.ToastUtils;

/**
 * Created by xiaoguang on 2015/8/25.
 */
public class MyListAdapter extends BaseAdapter{
    private List<ItemBean> mList;
    private ViewHolder holder;
    private LayoutInflater mInflater;
    public boolean[] isSelected;
    public boolean showCheckbox = false;
    public MyListAdapter(Context context,List<ItemBean> list){
        mList = list;
        mInflater = LayoutInflater.from(context);
        isSelected = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++){
            isSelected[i] = false;
        }
        showCheckbox = false;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_layout,null);
            holder.imageIco = (ImageView)convertView.findViewById(R.id.id_lv_img);
            holder.content = (TextView) convertView.findViewById(R.id.id_lv_content);
            holder.time = (TextView)convertView.findViewById(R.id.id_lv_time);
            holder.noteId = (TextView)convertView.findViewById(R.id.id_lv_id);
            holder.cb = (CheckBox)convertView.findViewById(R.id.id_lv_cb);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ItemBean itemBean = mList.get(position);
        holder.noteId.setText(itemBean.id + "");
        holder.imageIco.setImageBitmap(itemBean.image);
        holder.content.setText(delPathForContent(itemBean.content));
        holder.time.setText(itemBean.time);
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSelected[position] = isChecked;
            }
        });
        holder.cb.setChecked(isSelected[position]);

        // 设置checkbox显示与否
        if(showCheckbox){
            holder.cb.setVisibility(View.VISIBLE);
        }else{
            holder.cb.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView imageIco;
        TextView noteId;
        TextView content;
        TextView time;
        CheckBox cb;
    }

    /**
     * 去除文本里的路径和<pic_id>
     * @param content
     * @return 无图片标记的文本String
     */
    private String delPathForContent(String content){
        String patternStr = Environment.getExternalStorageDirectory()
                + "/" + Constants.IMG_DIR + "/.+?\\.\\w{3}";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher m = pattern.matcher(content);
        content = m.replaceAll("");

        String patternPicStr = "<pic_" + "\\d*"+">";
        Pattern patternPic = Pattern.compile(patternPicStr);
        Matcher mPic = patternPic.matcher(content);
        content = mPic.replaceAll("");

        patternStr = "\\n";
        pattern = Pattern.compile(patternStr);
        m = pattern.matcher(content);
        content = m.replaceAll("");
        return content;
    }
}