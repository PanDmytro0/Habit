package com.fernfog.habit;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStateAdapter {
    private List<HabitFragment> fragmentList = new ArrayList<>();

    public MyFragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void addFragment(HabitFragment fragment) {
        fragmentList.add(fragment);
        notifyDataSetChanged();
    }

    public void removeFragment(HabitFragment fragment) {
        fragmentList.remove(fragment);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitFragment createFragment(int position) {
        return fragmentList.get(position);
    }

    public List<HabitFragment> getAll() {
        return fragmentList;
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public void removeAll() {
        fragmentList.clear();
        notifyDataSetChanged();
    }
}
