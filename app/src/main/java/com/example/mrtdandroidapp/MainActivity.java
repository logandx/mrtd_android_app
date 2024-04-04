package com.example.mrtdandroidapp;

import android.os.Bundle;

import com.example.mrtdandroidapp.ui.main.PlaceholderFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.fragment.app.FragmentManager;

import com.example.mrtdandroidapp.ui.main.SectionsPagerAdapter;
import com.example.mrtdandroidapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager(); // Update here
            fragmentManager.beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

}