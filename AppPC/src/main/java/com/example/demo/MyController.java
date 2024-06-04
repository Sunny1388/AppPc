package com.example.demo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MyController {

	@Autowired
    private EmailService emailService;
	
	private final MagazzinoJDBCTemp magazzinoJDBCTemp;

	private final pcOrdJDBCTemp pcOrdJDBCTemp;
	
	@Autowired
	public MyController (MagazzinoJDBCTemp magazzinoJDBCTemp, pcOrdJDBCTemp pcOrdJDBCTemp) {
		this.magazzinoJDBCTemp = magazzinoJDBCTemp;
		this.pcOrdJDBCTemp = pcOrdJDBCTemp;
	}
	
    @GetMapping("/")
	 public String store(Model model) {
		
    	ArrayList <Computer> magazzino = magazzinoJDBCTemp.ritornaPC();
		 model.addAttribute("magazzino", magazzino);
    	
		 return "store";	
	 }
    
	 @GetMapping("/formPc")
	 public String formPC() {
			 			
		 return "formPC";	
	 }
	
	 @PostMapping("/addPC")
	 public String addPC(@RequestParam("marca") String marca,
			 @RequestParam("tipologia") String modello, 
			 @RequestParam("modello") String tipologia,
			 @RequestParam("descrizione") String descrizione,
			 @RequestParam("qnt") String qnt, 
			 @RequestParam("url") String url, 
			 @RequestParam("prezzo") String prezzo, 
			 Model model) {
		 
		 String [] ImgUrl = url.split("\"");
		 String urlImg = ImgUrl[3];
		 
		Computer pc1 = new Computer();
		 pc1.setMarca(marca);
		 pc1.setTipologia(tipologia);
		 pc1.setModello(modello);
		 pc1.setQnt(Integer.parseInt(qnt));
		 pc1.setDescrizione(descrizione);
		 pc1.setUrl(urlImg);
		 pc1.setPrezzo(Double.parseDouble(prezzo));
		 
		 model.addAttribute("pc1", pc1);
		 magazzinoJDBCTemp.insertComputer(marca, tipologia, modello, descrizione, pc1.getQnt(), urlImg, pc1.getPrezzo());
		
		 return "addPC";
	 }
	 
	 @GetMapping("/Magazzino")
	 public String magazzino(Model model){
		
		 ArrayList <Computer> magazzino = magazzinoJDBCTemp.ritornaPC();
		 model.addAttribute("magazzino", magazzino);
		
		 return "Magazzino";
		 
	 }
	 
	 @GetMapping("/change")
	 public String change(Model model) {
		
		 ArrayList <Computer> magazzino = magazzinoJDBCTemp.ritornaPC();
		 model.addAttribute("magazzino", magazzino);
		 
		 return "change";
	 }
	
	 @PostMapping("/changeP")
	 public ResponseEntity<String> changeP(@RequestParam("pc") String [] ordini,
			 @RequestParam("pezzi") String [] pezzi,Model model){
 
		 ArrayList<Integer> pc = new ArrayList<>();
		
		 ArrayList<Integer> qnt = new ArrayList <>();
 
		 for (String s: pezzi) {
			 if (!s.isEmpty()) {
			int x = Integer.parseInt(s);
			qnt.add(x);
		 }
			 }
		
		 for (String s: ordini) {
			 if (!s.isEmpty()) {
			int x = Integer.parseInt(s);
			pc.add(x);
		 }
			 }
		 
		 for (int i = 0; i < qnt.size(); i++) {
			 
			 magazzinoJDBCTemp.updatePezzi(qnt.get(i), pc.get(i));
			 
		 }
		 
		 return ResponseEntity.ok("Articoli aggiunti con successo");
		 
	 }
	
		 
	 @PostMapping("/store")
	 public String store(@RequestParam("ordini") String [] ordini,
			 @RequestParam("pezzi") String [] pezzi, Model model){
 
		 ArrayList<Computer> lista = magazzinoJDBCTemp.ritornaPC();
		 ArrayList<Integer> qnt = new ArrayList <>();
		 ArrayList<pcOrd> pc = new ArrayList <>();
 
		 for (String s: pezzi) {
			 if (!s.isEmpty()) {
			int x = Integer.parseInt(s);
			qnt.add(x);
		 }}
		 double prezzo = 0;
  
			 
			 for (int j = 0; j < ordini.length; j++) {
				 for (int i = 0; i < lista.size(); i++) {
				 if (lista.get(i).id ==(Integer.parseInt(ordini[j]))) {
				
					 magazzinoJDBCTemp.updatePezzi(qnt.get(j), lista.get(i).id)	;			
					 prezzo += lista.get(i).getPrezzo() * qnt.get(j);
					 pcOrd computer = new pcOrd();
					 computer.setModello(lista.get(i).getModello());
					 computer.setQnt(qnt.get(j));
					 pc.add(computer);
					 
				 }
	     	 }
		 }
 
			 model.addAttribute("lista", pc);
			 model.addAttribute("qnt", qnt);
			 model.addAttribute("prezzo", prezzo);
			 
		 return "conferma";
	 }
	 
	 
	 @PostMapping("/confPc")
	 public ResponseEntity <String> confPC(@RequestParam("modello") String [] ordini,
			 @RequestParam("qnt") String [] pezzi, 
			 @RequestParam("prezzo") String prezzo,
			 @RequestParam("email") String email, Model model){
	 		 
		 ArrayList<pcOrd> pc = new ArrayList <>();
		 ArrayList<Integer> qnt = new ArrayList <>();
		 ArrayList <Computer> lista = magazzinoJDBCTemp.ritornaPC();
		    	 
				 for (String s: pezzi) {
					 if (!s.isEmpty()) {
					int x = Integer.parseInt(s);
					qnt.add(x);
					
				 }}
						 
					 for (int j = 0; j < ordini.length; j++) {
						 for (int i = 0; i < lista.size(); i++) {
						 if (lista.get(i).getModello().equals(ordini[j])) {
							 
							 
							 pcOrd computer = new pcOrd();
							 computer.setModello(lista.get(i).getModello());
							 computer.setQnt(qnt.get(j));
							 pc.add(computer);
							 
						 }
						 
					 }}
				//andiamo ad aggiungere i pezzi dei prodotti acquistati nella tabella pcorders	 
					 for (pcOrd comp : pc) {
						 pcOrdJDBCTemp.updatePezzi(comp.getQnt(), comp.getModello());}
					 
	
		 String to = email;
		 String subject = "Ordine da PCSeller confermato";
		 String text = "";
		 for (pcOrd comp : pc) {
			 text+="Hai acquistato: \n";
			 text+= comp.getModello() + "\n";
			 text+= comp.getQnt() + "\n";
			 
		 }
		 
		 text+= "Il prezzo totale da pagare è " + prezzo + " euro";
		  emailService.sendSimpleEmail(to, subject, text);
		  return ResponseEntity.ok("Mail inviata correttamente. Grazie dell'acquisto.");
	 }
	 
	 
	 @GetMapping("/formEmail")
		public String formEmail(){
			
			return "formEmail";
		}
			
	    @PostMapping("sendEmail")
	    public ResponseEntity<String> sendEmail(@RequestParam("to") String to, @RequestParam("subject") String subject, @RequestParam("text") String text) {
	        emailService.sendSimpleEmail(to, subject, text);
	       
	        return ResponseEntity.ok("Email sent successfully");
	    }
	 
	 
	    @GetMapping("/showPcOrders")
	    public String showPcOrders(Model model) {
	        ArrayList<pcOrd> pcOrders = new ArrayList<>();
	        pcOrders = pcOrdJDBCTemp.ritornaOrdPc();
	        
	        // Convertiamo la lista in una stringa JSON
	        ObjectMapper objectMapper = new ObjectMapper();
	        String pcOrdersJson = "";
	    	try {
				pcOrdersJson = objectMapper.writeValueAsString(pcOrders);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       

	        // Passiamo la stringa JSON al modello
	        model.addAttribute("pcOrdersJson", pcOrdersJson);
	        
	        model.addAttribute("pcOrders", pcOrders);
	        return "pcOrders";
	    }
	    
}
