package com.example.two.universe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.two.R;

import java.util.List;

/**
 * 适配器
 * Created by YinTao on 2018/1/22.
 */

public class RecycleDataAdapter extends RecyclerView.Adapter<RecycleDataAdapter.MyViewHolder>
{
    private Context context;
    private List<String> data;
    private LayoutInflater inflater;

    public RecycleDataAdapter(Context context, List<String> data)
    {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.item_recycleview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        holder.content.setText(data.get(position));
        holder.any.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                Toast.makeText(context, "longClick" + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        holder.any.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context, "item点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnTop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context, "置顶" + position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnUnRead.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context, "标记" + position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context, "删除" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout any;
        TextView content;
        Button btnDelete;
        Button btnUnRead;
        Button btnTop;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            any = (RelativeLayout) itemView.findViewById(R.id.any);
            content = (TextView) itemView.findViewById(R.id.content);
            btnDelete = (Button) itemView.findViewById(R.id.delete);
            btnUnRead = (Button) itemView.findViewById(R.id.mark);
            btnTop = (Button) itemView.findViewById(R.id.top);
        }
    }
}
