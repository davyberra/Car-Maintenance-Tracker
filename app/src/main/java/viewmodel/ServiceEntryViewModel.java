package viewmodel;

import android.app.Application;
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

    public ServiceEntryViewModel(@NonNull Application application) {
        super(application);
        db = MainDatabase.getInstance(getApplication().getApplicationContext());
        serviceEntryDao = db.serviceEntryDao();
    }

    public List<ServiceEntry> getCurServiceEntries() {
        return curServiceEntries;
    }

    public void setCurServiceEntries(List<ServiceEntry> curServiceEntries) {
        this.curServiceEntries = curServiceEntries;
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

}
