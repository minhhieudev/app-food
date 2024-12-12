package com.example.appfood;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ObjectiveAdapter extends RecyclerView.Adapter<ObjectiveAdapter.ObjectiveViewHolder> {

    private List<Objective> objectiveList;
    private boolean showCheckbox;

    public ObjectiveAdapter(List<Objective> objectiveList, boolean showCheckbox) {
        this.objectiveList = objectiveList;
        this.showCheckbox = showCheckbox;
    }

    @NonNull
    @Override
    public ObjectiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle, parent, false);
        return new ObjectiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectiveViewHolder holder, int position) {
        Objective objective = objectiveList.get(position);

        // Set tên mục tiêu
        holder.itemText.setText(objective.getName());

        // Đặt màu cho đường viền dưới cùng
        holder.bottomLine.setBackgroundColor(objective.getColor());

        // Đặt màu cho hình tròn
        GradientDrawable drawable = (GradientDrawable) holder.circleView.getBackground();
        drawable.setColor(objective.getColor()); // Thay đổi màu theo mục tiêu

        // Kiểm soát hiển thị của CheckBox
        if (showCheckbox) {
            holder.objectiveCheckbox.setVisibility(View.VISIBLE);
            holder.objectiveCheckbox.setChecked(objective.isChecked());  // Cập nhật trạng thái checkbox
        } else {
            holder.objectiveCheckbox.setVisibility(View.GONE);
        }

        // Cập nhật trạng thái checkbox khi người dùng thay đổi
        holder.objectiveCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            objective.setChecked(isChecked);  // Cập nhật trạng thái của objective
        });
    }

    @Override
    public int getItemCount() {
        return objectiveList.size();
    }

    static class ObjectiveViewHolder extends RecyclerView.ViewHolder {

        ImageView circleView; // Đảm bảo đây là ImageView
        TextView itemText;
        View bottomLine;
        CheckBox objectiveCheckbox;

        public ObjectiveViewHolder(@NonNull View itemView) {
            super(itemView);
            circleView = itemView.findViewById(R.id.circle_view); // ID này cần phải đúng
            itemText = itemView.findViewById(R.id.item_text);
            bottomLine = itemView.findViewById(R.id.bottom_line);
            objectiveCheckbox = itemView.findViewById(R.id.objectiveCheckbox);
        }
    }
}
