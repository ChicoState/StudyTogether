package com.studytogether.studytogether.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.studytogether.studytogether.Models.Course;
import com.studytogether.studytogether.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<Course> courses;

    // CourseAdapter Constructor
    public CourseAdapter(Context mContext, List<Course> courses) {
        this.mContext = mContext;
        this.courses = courses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Declare viewHolder and initialize to null
        RecyclerView.ViewHolder viewHolder = null;
        // Declare view and initialize to null
        View view = null;

        // Inflate with row_group_item_new view
        view = LayoutInflater.from(mContext).inflate(R.layout.row_course,parent,false);
        // Pass the view into viewHolder
        viewHolder = new CourseViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Course course = courses.get(position);

        CourseViewHolder courseViewHolder = (CourseViewHolder) holder;

        if(course != null) {
            if(course.getSubject() != null && course.getCourseTitle() != null) {
                courseViewHolder.tvSubject.setText(course.getSubject());
                courseViewHolder.tvCategoryNum.setText(String.valueOf(course.getCategoryNum()));
                courseViewHolder.tvCourseTitle.setText(course.getCourseTitle());
            }
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    // Create myViewHolder as RecyclerView.ViewHolder
    public class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject;
        TextView tvCategoryNum;
        TextView tvCourseTitle;



        // Create myViewHolder
        public CourseViewHolder(View itemView) {
            super(itemView);

            // Set the attributes with each item
            tvSubject = itemView.findViewById(R.id.row_subject);
            tvCategoryNum = itemView.findViewById(R.id.row_category_num);
            tvCourseTitle = itemView.findViewById(R.id.row_course_title);
            Toast.makeText(mContext," Adapter in CourseViewHolder",Toast.LENGTH_LONG).show();


        }
    }
}
