package viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dao.GasEntryDao;
import database.MainDatabase;
import entity.GasEntry;

public class GasEntryViewModel extends AndroidViewModel {
    private MainDatabase db;
    private static final String PREFS_FILE = "com.davyberra.carmaintenancetracker.preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String KEY_SELECTED_GAS_ENTRY = "key_selected_gas_entry";
    private GasEntryDao gasEntryDao;
    private LiveData<GasEntry> selectedGasEntry;
    private LiveData<List<GasEntry>> gasEntryLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();


    public GasEntryViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        db = MainDatabase.getInstance(getApplication().getApplicationContext());
        gasEntryDao = db.gasEntryDao();
        sharedPreferences = getApplication().getApplicationContext().getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();
    }

    public LiveData<List<GasEntry>> getAllByCarId(int carId) {
        return gasEntryDao.getAllByCarId(carId);
    }

    public LiveData<GasEntry> getGasEntryById(int id) {
        return gasEntryDao.getGasEntryById(id);
    }

    public LiveData<GasEntry> getLatestGasEntry(int carId) {
        return gasEntryDao.getLatestGasEntry(carId);
    }

    public void insertGasEntry(GasEntry gasEntry) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.gasEntryDao().insertGasEntry(gasEntry);
            }
        });
    }

    public void selectGasEntry(GasEntry gasEntry) {
        editor.putInt(KEY_SELECTED_GAS_ENTRY, gasEntry.gasEntryId);
        editor.commit();
    }

    public int getSelectedGasEntryId() {
        return sharedPreferences.getInt(KEY_SELECTED_GAS_ENTRY, 0);
    }

    public LiveData<GasEntry> getSelectedGasEntry() {
        int id = getSelectedGasEntryId();
        selectedGasEntry = gasEntryDao.getGasEntryById(id);
        return selectedGasEntry;
    }


    public void deleteGasEntry(GasEntry gasEntry) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.gasEntryDao().delete(gasEntry);
            }
        });
    }
}
