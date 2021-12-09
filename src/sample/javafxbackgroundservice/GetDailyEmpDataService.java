package sample.javafxbackgroundservice;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
 

public class GetDailyEmpDataService extends Service<ObservableList<EmployeeDetails>> {

	    /**
	     * Create and return the task for fetching the data. Note that this method
	     * is called on the background thread (all other code in this application is
	     * on the JavaFX Application Thread!).
	     */
	    @Override
	    protected Task createTask() {
	        return new GetDailyEmpDataTask();
	    }
	}
