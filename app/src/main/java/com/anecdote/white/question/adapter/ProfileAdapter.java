package com.anecdote.white.question.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.anecdote.white.question.R;
import com.anecdote.white.question.bean.EventProfile;
import com.anecdote.white.question.bean.Profile;
import com.anecdote.white.question.bean.ProfileRule;
import com.anecdote.white.question.bean.ShowType;
import com.anecdote.white.question.viewholder.AbsProfileViewHolder;
import com.anecdote.white.question.viewholder.CheckBoxViewHolder;
import com.anecdote.white.question.viewholder.DateViewHolder;
import com.anecdote.white.question.viewholder.EnumViewHolder;
import com.anecdote.white.question.viewholder.MultiBoxViewHolder;
import com.anecdote.white.question.viewholder.NumViewHolder;
import com.anecdote.white.question.viewholder.NumberPickerViewHolder;
import com.anecdote.white.question.viewholder.TextViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ProfileAdapter extends RecyclerView.Adapter<AbsProfileViewHolder> {

    private List<Profile> list = new ArrayList<>();
    private Context context;
    private List<Profile> originList;

    public ProfileAdapter(Context context) {
        this.context = context;
        EventBus.getDefault().register(this);
    }


    public void onEventMainThread(EventProfile eventProfile) {
        fillShow();
        notifyDataSetChanged();
    }

    public void setContent(List<Profile> list) {
        this.originList = list;
        filterIllegal();
        fillShow();
        notifyDataSetChanged();
    }

    private void filterIllegal() {
        if (originList == null)
            return;
        Iterator<Profile> iterator = originList.listIterator();
        while (iterator.hasNext()) {
            Profile profile = iterator.next();
            if (profile == null)
                iterator.remove();
        }
    }

    private void fillShow() {
        if (originList == null)
            return;
        list.clear();
        for (int i = 0; i < originList.size(); i++) {
            if (originList.get(i).isShown(listener))
                list.add(originList.get(i));
        }
    }

    private ProfileRule.ObtainProfileListener listener = new ProfileRule.ObtainProfileListener() {
        @Override
        public Profile obtain(String profileKey) {
            if (originList == null) {
                return null;
            }
            for (int i = 0; i < originList.size(); i++) {
                if (originList.get(i) != null && originList.get(i).getProfileKey().equals(profileKey))
                    return originList.get(i);
            }
            return null;
        }
    };


    @Override
    public AbsProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbsProfileViewHolder holder;
        switch (viewType) {
            case ShowType.TYPE_MULTI_BOX:
                holder = new MultiBoxViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_profile_check_box, null));
                break;
            case ShowType.TYPE_ENUM:
                holder = new EnumViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_profile_check_box, null));
                break;
            case ShowType.TYPE_DATE:
                holder = new DateViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_profile_date, null));
                break;
            case ShowType.TYPE_NUM:
                holder = new NumViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_profile_num, null));
                break;
            case ShowType.TYPE_NUMBER_PICKER:
                holder = new NumberPickerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_profile_numpicker, null));
                break;
            case ShowType.TYPE_CHECKBOX:
                holder = new CheckBoxViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_profile_check_box, null));
                break;
            case ShowType.TYPE_TEXT:
                holder = new TextViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_profile_text, null));
                break;
            default:
                holder = new TextViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_profile_text, null));
        }
        holder.setObtainProfileListener(listener);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        return holder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(AbsProfileViewHolder holder, int position) {
        if (holder != null) {
            holder.fillView(list.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list != null) {
            if (list.get(position) != null)
                return list.get(position).getShowType();
        }
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void unit() {
        EventBus.getDefault().unregister(this);
    }

}