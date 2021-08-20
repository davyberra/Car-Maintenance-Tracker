package viewmodel;

import android.app.Application;

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
    private GasEntryDao gasEntryDao;
    private LiveData<List<GasEntry>> gasEntryLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();


    public GasEntryViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        db = MainDatabase.getInstance(getApplication().getApplicationContext());
        gasEntryDao = db.gasEntryDao();
    }

    public LiveData<List<GasEntry>> getAllByCarId(int carId) {
        return gasEntryDao.getAllByCarId(carId);
    }

    public void insertGasEntry(GasEntry gasEntry) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.gasEntryDao().insertGasEntry(gasEntry);
            }
        });
    }
}
