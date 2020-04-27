package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model = new Model();
	
	private ObservableList<Integer> list = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Integer> boxMese;

    @FXML
    private Button btnUmidita;
    
    @FXML
    private Button btnReset;

    @FXML
    private Button btnCalcola;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	int input = boxMese.getValue();
    	List<Citta> soluzione = new ArrayList<>(model.trovaSequenza(input));
    	String str = "";
    	for(Citta c: soluzione) {
    		str += c.getNome()+"\n";
    	}
    	txtResult.setText(str);
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	int input = boxMese.getValue();
    	
    	txtResult.setText(model.getUmiditaMedia(input));
    }
    

    @FXML
    void doReset(ActionEvent event) {
    	txtResult.clear();
    }

    @FXML
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

        for(int i=1; i<13; i++)
        	list.add(i);
        
        boxMese.setItems(list);
    }
}

