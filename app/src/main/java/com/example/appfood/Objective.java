package com.example.appfood;

import java.util.Objects;

public class Objective {
    private String name;
    private int color;
    private String id;
    private boolean isChecked; // Thêm trạng thái checkbox

    public Objective(String id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.isChecked = false;  // Mặc định là chưa được chọn
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public String getId() {
        return id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Objective objective = (Objective) o;
        return color == objective.color &&
                id.equals(objective.id) &&
                name.equals(objective.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
