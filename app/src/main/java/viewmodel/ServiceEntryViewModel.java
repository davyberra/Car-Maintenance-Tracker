package viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dao.ServiceEntryDao;
import database.MainDatabase;
import entity.ServiceEntry;

public class ServiceEntryViewModel extends AndroidViewModel {
    private MainDatabase db;
    private ServiceEntryDao serviceEntryDao;
    private Executor executor = Executors.newSingleThreadExecutor();
    private MutableLiveData<String> type;
    private List<ServiceEntry> curServiceEntries;
    private List<ServiceEntry> filteredServiceEntries;

    private static final String PREFS_FILE = "com.davyberra.carmaintenancetracker.preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String KEY_SELECTED_SERVICE_ENTRY = "key_selected_service_entry";

    public ServiceEntryViewModel(@NonNull Application application) {
        super(application);
        db = MainDatabase.getInstance(getApplication().getApplicationContext());
        serviceEntryDao = db.serviceEntryDao();
        sharedPreferences = getApplication().getApplicationContext().getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();
    }

    public List<ServiceEntry> getCurServiceEntries() {
        return curServiceEntries;
    }

    public void setCurServiceEntries(List<ServiceEntry> curServiceEntries) {
        this.curServiceEntries = curServiceEntries;
    }

    public List<ServiceEntry> getFilteredServiceEntries() {
        return filteredServiceEntries;
    }

    public void setFilteredServiceEntries(List<ServiceEntry> filteredServiceEntries) {
        this.filteredServiceEntries = filteredServiceEntries;
    }

    public MutableLiveData<String> getTypeLiveData() {
        if (type == null) {
            type = new MutableLiveData<>();
        }
        return type;
    }

    public LiveData<List<ServiceEntry>> getAllByCarId(int carId) {
        return serviceEntryDao.getAllByCarId(carId);
    }

    public LiveData<List<ServiceEntry>> getAllByType(int carId, String type) {
        return serviceEntryDao.getAllByType(carId, type);
    }

    public LiveData<ServiceEntry> getServiceEntryById(int id) {
        return serviceEntryDao.getServiceEntryById(id);
    }

    public void insertServiceEntry(ServiceEntry serviceEntry) {
        executor.execute(() -> db.serviceEntryDao().insertServiceEntry(serviceEntry));
    }

    public void selectServiceEntry(ServiceEntry serviceEntry) {
        editor.putInt(KEY_SELECTED_SERVICE_ENTRY, serviceEntry.serviceId);
        editor.commit();
    }

    public LiveData<ServiceEntry> getSelectedServiceEntry() {
        int id = getSelectedServiceEntryId();
        return db.serviceEntryDao().getServiceEntryById(id);

    }

    private int getSelectedServiceEntryId() {
        return sharedPreferences.getInt(KEY_SELECTED_SERVICE_ENTRY, -1);
    }

    public void deleteServiceEntry(ServiceEntry serviceEntry) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.serviceEntryDao().delete(serviceEntry);
            }
        });

    }
}
