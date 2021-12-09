package sample.javafxbackgroundservice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class BackgroundServiceController {
	
	
	@FXML
	private TableView tableView;
	
	@FXML
	private TableColumn empEmail;
	
	@FXML
	private TableColumn joinDate, empName;
	
	@FXML
	private StackPane stackPane;
	
 public void initialize() {       
		 
     empEmail.setCellValueFactory(new PropertyValueFactory("empMail"));     
     empName.setCellValueFactory(new PropertyValueFactory("empName"));    
     joinDate.setCellValueFactory(new PropertyValueFactory("date"));
      
	 }
	
		
	@FXML
	private void loadDataProcessStart(ActionEvent event) {
	
	final GetDailyEmpDataService service = new GetDailyEmpDataService();
	Region veil = new Region();
        veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
        veil.setPrefSize(400, 440);
        ProgressIndicator p = new ProgressIndicator();
        p.setMaxSize(140, 140);
        p.setStyle(" -fx-progress-color: orange;");
        //change progress color 
       
 
        p.progressProperty().bind(service.progressProperty());
        veil.visibleProperty().bind(service.runningProperty());
        p.visibleProperty().bind(service.runningProperty());
        tableView.itemsProperty().bind(service.valueProperty());
        	
        stackPane.getChildren().addAll(veil, p);
        service.start();
		
	}
	
	
}
