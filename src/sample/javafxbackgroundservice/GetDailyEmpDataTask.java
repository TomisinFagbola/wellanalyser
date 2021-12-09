package sample.javafxbackgroundservice;

import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
 
public class GetDailyEmpDataTask extends Task<ObservableList<EmployeeDetails>> {
 
    @Override
    protected ObservableList<EmployeeDetails> call() throws Exception {
        /*for (int i = 0; i < 500; i++) {
            updateProgress(i, 500);
            Thread.sleep(5);
        }*/
        
         // we can make db connection here and fetch database records // or perform other task as well
          updateProgress(0, 500); //0% progress          
         
          Thread.sleep(1505);          
          
          updateProgress(30, 500); //6% progress
          
          Thread.sleep(1505);
          
          updateProgress(100, 500); //20% progress
          
          Thread.sleep(1505);
          updateProgress(150, 500); //30% progress
          
          Thread.sleep(1505);
          updateProgress(200, 500); //40% progress
           
          ObservableList<EmployeeDetails> empData = FXCollections.observableArrayList();
          empData.add(new EmployeeDetails("Peter", "abc@gmail.com", new Date()));
          empData.add(new EmployeeDetails("Sam", "xyz@gmail.com", new Date(0)));
          
          Thread.sleep(1505);
          updateProgress(500, 500); //100% task complete
                                
        return empData;
    }
}
