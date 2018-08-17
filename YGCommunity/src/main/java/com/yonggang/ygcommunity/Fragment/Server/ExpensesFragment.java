package com.yonggang.ygcommunity.Fragment.Server;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yonggang.ygcommunity.Activity.Server.AddAccountActivity;
import com.yonggang.ygcommunity.Activity.Server.EditExpActivity;
import com.yonggang.ygcommunity.Activity.Server.FeeListActivity;
import com.yonggang.ygcommunity.Entry.Expense;
import com.yonggang.ygcommunity.Entry.Home;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liyangyang on 2017/6/27.
 */

public class ExpensesFragment extends Fragment {
    @BindView(R.id.text_address)
    TextView textAddress;
    @BindView(R.id.list_expense)
    ListView listExpense;
    @BindView(R.id.txt_edit)
    TextView txtEdit;

    private Home home;
    private int index;

    public void setArg(Home home, int index) {
        this.home = home;
        this.index = index;
    }

    private ExpensesOnclickListener listener;

    private ExpenseAdapter adapter;

    private List<Expense> list_expense;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_expenses, container, false);
        ButterKnife.bind(this, view);
        textAddress.setText(home.getAddress());
        account_tab(home.getId());
        return view;
    }

    /**
     * 获取家庭的缴费信息
     *
     * @param id
     */
    private void account_tab(String id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<List<Expense>>() {
            @Override
            public void onNext(final List<Expense> data) {
                Log.i("account_tab", data.toString());
                list_expense = data;
                adapter = new ExpenseAdapter();
                listExpense.setAdapter(adapter);  //设置适配器
                if (listener == null) {
                    listener = new ExpensesOnclickListener();
                }
                listExpense.setOnItemClickListener(listener);
                txtEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (adapter.is_edit) {
                            txtEdit.setText("编辑");
                            listExpense.setOnItemClickListener(listener);
                        } else {
                            txtEdit.setText("取消编辑");
                            listExpense.setOnItemClickListener(null);
                        }
                        adapter.is_edit = !adapter.is_edit;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        HttpUtil.getInstance().account_tab(new ProgressSubscriber<List<Expense>>(onNextListener, getActivity()), id);
    }

    @OnClick(R.id.layout_address)
    public void onViewClicked() { // 编辑家庭界面
        Intent intent = new Intent(getActivity(), EditExpActivity.class);
        intent.putExtra("id", home.getId());
        intent.putExtra("home", home);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 缴费信息适配器
     */
    class ExpenseAdapter extends BaseAdapter {

        private boolean is_edit; // 是否处于编辑状态

        @Override
        public int getCount() {
            return list_expense.size();
        }

        @Override
        public Object getItem(int position) {
            return list_expense.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_expense, null);
                holder.img_logo = (ImageView) convertView.findViewById(R.id.img_logo);
                holder.txt_type = (TextView) convertView.findViewById(R.id.txt_type);
                holder.txt_num = (TextView) convertView.findViewById(R.id.txt_num);
                holder.txt_add = (TextView) convertView.findViewById(R.id.txt_add);
                holder.txt_delete = (TextView) convertView.findViewById(R.id.txt_delete);
                holder.layout_normal = (LinearLayout) convertView.findViewById(R.id.layout_normal);
                holder.layout_edit = (LinearLayout) convertView.findViewById(R.id.layout_edit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_type.setText(list_expense.get(position).getType_name());
            switch (list_expense.get(position).getType()) {
                case 0:
                    holder.img_logo.setImageResource(R.mipmap.server_water);
                    break;
                case 1:
                    holder.img_logo.setImageResource(R.mipmap.server_electric);
                    break;
            }
            holder.txt_num.setText(list_expense.get(position).getSurface());
            if ("".equals(list_expense.get(position).getSurface())) {
                holder.txt_num.setVisibility(View.GONE);
                holder.txt_add.setVisibility(View.VISIBLE);
                if (is_edit) {
                    holder.layout_normal.setVisibility(View.GONE);
                    holder.layout_edit.setVisibility(View.VISIBLE);
                } else {
                    holder.layout_normal.setVisibility(View.VISIBLE);
                    holder.layout_edit.setVisibility(View.GONE);
                }
            } else {
                holder.txt_num.setVisibility(View.VISIBLE);
                holder.txt_add.setVisibility(View.GONE);
                if (is_edit) {
                    holder.layout_normal.setVisibility(View.GONE);
                    holder.layout_edit.setVisibility(View.VISIBLE);
                } else {
                    holder.layout_normal.setVisibility(View.VISIBLE);
                    holder.layout_edit.setVisibility(View.GONE);
                }
            }
            holder.txt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete_surface(position, home.getId(), list_expense.get(position).getType());
                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView img_logo;
            TextView txt_type;
            TextView txt_num;
            TextView txt_add;
            TextView txt_delete;

            LinearLayout layout_normal;
            LinearLayout layout_edit;
        }

    }

    /**
     * 缴费信息的点击事件
     */
    class ExpensesOnclickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Expense bean = list_expense.get(position);
            if ("".equals(bean.getSurface())) {
                //无缴费账户，需添加
                Intent intent = new Intent(getActivity(), AddAccountActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("account", bean);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                //已有缴费账户
                Intent intent = new Intent(getActivity(), FeeListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("account", bean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    /**
     * 删除表号
     *
     * @param index
     * @param id
     * @param type
     */
    private void delete_surface(final int index, String id, int type) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("delete_surface", data);
                list_expense.get(index).setSurface("");
                Log.i("delete_surface", list_expense.toString());
                adapter.is_edit = false;
                txtEdit.setText("编辑");
                adapter.notifyDataSetChanged();
                listExpense.setOnItemClickListener(listener);
            }
        };
        HttpUtil.getInstance().delete_surface(new ProgressSubscriber(onNextListener, getActivity(), "删除中"), id, type);
    }

}
