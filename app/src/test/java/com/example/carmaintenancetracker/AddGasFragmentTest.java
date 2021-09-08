package com.example.carmaintenancetracker;

import android.content.Context;

import androidx.fragment.app.Fragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import androidx.test.core.app.ApplicationProvider;
import androidx.fragment.app.testing.FragmentScenario;

import ui.AddGasFragment;

@RunWith(JUnit4.class)
public class AddGasFragmentTest {

    @Before
    public void setUp() {
        FragmentScenario<AddGasFragment> scenario = FragmentScenario.launch(AddGasFragment.class);
    }

    @Test
    public void gallonsAndTotalCostCalculateCorrectPpg() throws Exception {

    }
}
