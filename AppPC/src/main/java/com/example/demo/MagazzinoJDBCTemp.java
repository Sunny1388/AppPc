package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class MagazzinoJDBCTemp {

	private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public void setJdbcTemplateObject(JdbcTemplate jdbcTemplateObject) {
        this.jdbcTemplateObject = jdbcTemplateObject;
    }

    public int insertComputer(String marca , String tipologia, String modello, String descrizione, int qnt, String url, double prezzo) {
        String query = "INSERT INTO magazzino (marca, tipologia, modello, descrizione, qnt, url, prezzo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplateObject.update(query, marca, tipologia, modello, descrizione, qnt, url, prezzo);
    }
	
    public ArrayList<Computer> ritornaPC(){
    	ResultSet rs = null;
    		    	
            String query = "SELECT * FROM magazzino";
            return jdbcTemplateObject.query(query, new ResultSetExtractor<ArrayList<Computer>>() {
               
            	@Override
                public ArrayList<Computer> extractData(ResultSet rs) throws SQLException {
                	ArrayList <Computer> magazzino = new ArrayList<>();
                   
                	while (rs.next()) {
                    		                    	
                        Computer pc1 = new Computer();
                       
                        pc1.setId(rs.getInt("id"));
                        pc1.setMarca(rs.getString("marca"));
                        pc1.setTipologia(rs.getString("tipologia"));
                        pc1.setModello(rs.getString("modello"));
                        pc1.setDescrizione(rs.getString("descrizione"));
                        pc1.setQnt(rs.getInt("qnt"));
                        pc1.setUrl(rs.getString("url"));
                        pc1.setPrezzo(rs.getDouble("prezzo"));
                        
                        magazzino.add(pc1);
                    }
                    return magazzino;
                }
            });
        }
    
    public int updatePezzi(int pezzi, int id) {
        String query = "UPDATE magazzino SET qnt = qnt - ? WHERE id = ?";
        return jdbcTemplateObject.update(query, pezzi, id);
    }
    
    
    
    
    
    // Metodo per eseguire query DDL
    public void executeDDLQuery(String query) {
        jdbcTemplateObject.execute(query);
    }
    
    
}
