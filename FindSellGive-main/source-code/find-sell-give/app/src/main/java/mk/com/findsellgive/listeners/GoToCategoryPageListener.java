package mk.com.findsellgive.listeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import mk.com.findsellgive.activities.CategoryPostsActivity;
import mk.com.findsellgive.activities.RegisterActivity;
import mk.com.findsellgive.utills.UtilitiesHelper;


public class GoToCategoryPageListener implements View.OnClickListener {
    private Context context;
    private int category;

    public GoToCategoryPageListener(Context context, int category) {
        this.context = context;
        this.category = category;
    }

    @Override
    public void onClick(View v) {
        Intent gotoCategoryPosts = new Intent(context, CategoryPostsActivity.class);
        gotoCategoryPosts.putExtra("category",category);
        context.startActivity(gotoCategoryPosts);
    }
}
