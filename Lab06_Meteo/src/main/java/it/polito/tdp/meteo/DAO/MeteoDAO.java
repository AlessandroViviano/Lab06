package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		final String sql = "SELECT situazione.Localita, situazione.`Data`, situazione.Umidita FROM situazione WHERE MONTH (DATA) = ? AND localita = ?";
		
		List<Rilevamento> rilevamenti = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, localita);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				String citta = rs.getString("localita");
				Date data = rs.getDate("data");
				int umidita = rs.getInt("umidita");
				
				Rilevamento r = new Rilevamento(citta, data, umidita);
				rilevamenti.add(r);
			}
			conn.close();
			return rilevamenti;
		}catch(SQLException e) {
			throw new RuntimeException("Errore db", e);
		}
	}

	/*public List<Rilevamento> getRilevamentiPrimiQuindici(int mese){
		
		final String sql = "SELECT situazione.Localita, situazione.`Data`, situazione.Umidita FROM situazione WHERE DAY(DATA) <= ? AND MONTH(DATA) = ?";
		
		List<Rilevamento> rilevamenti = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, 15);
			st.setInt(2, mese);
			
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				String localita = rs.getString("localita");
				Date data = rs.getDate("data");
				int umidita = rs.getInt("umidita");
				
				Rilevamento r = new Rilevamento(localita, data, umidita);
				rilevamenti.add(r);
			}
			conn.close();
			return rilevamenti;
		}catch(SQLException e) {
			throw new RuntimeException("Errore db", e);
		}
	}*/
	
    public List<Citta> getAllCitta(){
    	
    	final String sql = "SELECT DISTINCT localita FROM situazione ORDER BY localita";
    	
    	List<Citta> citta = new ArrayList<>();
    	
    	try {
    		Connection conn = ConnectDB.getConnection();
    		PreparedStatement st = conn.prepareStatement(sql);
    		
    		ResultSet rs = st.executeQuery();
    		
    		while(rs.next()) {
    			String nome = rs.getString("localita");
    			
    			Citta c = new Citta(nome);
    			
    			citta.add(c);
    		}
    		conn.close();
    		return citta;
    	}catch(SQLException e) {
    		throw new RuntimeException("Errore db", e);
    	}
    }

}
