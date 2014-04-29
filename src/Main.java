
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Main {
	
	static Cuenta cuenta = new Cuenta(0);

	static ActorSystem system = ActorSystem.create("Persona");
	
	static ActorRef Antonio = system.actorOf(Props.create(Actor.class), "Antonio"); 
	static ActorRef Blanca = system.actorOf(Props.create(Actor.class), "Blanca"); 
	static ActorRef Banco = system.actorOf(Props.create(Banco.class), "Banco"); 

	public static void main(String[] args) throws NumberFormatException, IOException {
		
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        int m = Integer.parseInt(br.readLine());
        
        String cambiosAntonio = "";
        String cambiosBlanca = "";
        
        for (int i = 0; i < n; i++) {
            cambiosAntonio += br.readLine()+" ";
        }
        
        for (int i = 0; i < m; i++) {
        	cambiosBlanca += br.readLine()+" ";
        }

		//---------- sección de mensajes
        Antonio.tell("Cambios "+cambiosAntonio, null);
        Blanca.tell("Cambios "+cambiosBlanca, null);
        
		//estos son los mensajes
		

		system.shutdown(); //espera que Antonio y Blanca acaben de comunicarse 
		system.awaitTermination();// JOIN //Termina la instancia de system
		
		//----------//
	}
	
	static class Actor extends UntypedActor {
		
		String inversionAux = "";

		@Override
		public void onReceive(Object message) throws Exception {
			
			if (message instanceof String) {
				String[] s = ((String) message).split(" ");
				switch (s[0]) {
				case "Cambios": {
					for (int i = 1; i < s.length; i++) {
						//inversionAux += s[i]+" ";
						Banco.tell("Modificar "+s[i],null);
					}
					break;
				}
				default:
					System.out.println("No puede modificar.");
				}
			} else {
				unhandled(message);
			}
			
			
			/*String[] inversion = inversionAux.split(" ");
			
			for (int i = 0; i < inversion.length; i++) {
				Banco.tell("Modificar "+inversion[i], null);
			}*/
			
		}

	}
	
	static class Banco extends UntypedActor {
		
		@Override
		public void onReceive(Object message) throws Exception {
			if (message instanceof String) {
				String[] s = ((String) message).split(" ");
				switch (s[0]) {
				case "Modificar": {
					cuenta.cambio(Integer.parseInt(s[1]));
					System.out.println(Banco.this.getSender().path().name()+" modifico "+Integer.parseInt(s[1])+" en la cuenta");
					System.out.println("Valor cuenta = "+cuenta.getValor());
					break;
				}
				default:
					System.out.println("No puede modificar.");
				}
			} else {
				unhandled(message);
			}
		}

	}
	
	static class Cuenta{

	    public int valor;
	    
		public Cuenta(int valor) {
			this.valor = valor;
		}
	    
	    public void cambio(int cambio){
	    	int temp = valor;
	    	temp = temp + cambio;
	        valor = temp;
	    }
	    
	    public int getValor(){
	        return valor;
	    }

	}
	

	public static void main(String[] args) {
		
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();
        
        int[] cambiosAntonio = new int[n];
        int[] cambiosBlanca = new int[m];
        
        for (int i = 0; i < n; i++) {
            cambiosAntonio[i] = sc.nextInt();
        }
        
        for (int i = 0; i < m; i++) {
            cambiosBlanca[i] = sc.nextInt();
        }

		//---------- sección de mensajes
		ActorSystem system = ActorSystem.create("Persona");
		
		ActorRef Antonio = system.actorOf(Props.create(Actor.class), "Antonio"); 
		ActorRef Blanca = system.actorOf(Props.create(Actor.class), "Blanca"); 

		
		//estos son los mensajes

		for (int i = 0; i < Math.min(n, m); i++) {
			Antonio.tell("AntonioQuiereModificar "+cambiosAntonio[i], Antonio);
			Blanca.tell("BlancaQuiereModificar "+cambiosBlanca[i], Blanca);
		}
		

		if(Math.max(n, m)==n && n!=m){
			for (int j = m; j < n; j++) {
				Antonio.tell("AntonioQuiereModificar "+cambiosAntonio[j], Antonio);
			}
		}else{
			for (int j = n; j < m; j++) {
				Blanca.tell("BlancaQuiereModificar "+cambiosBlanca[j], Blanca);
			}
		}

		system.shutdown(); //espera que Antonio y Blanca acaben de comunicarse 
		system.awaitTermination();// JOIN //Termina la instancia de system
		
		//----------//
	}
	
	static class Actor extends UntypedActor {
		
		@Override
		public void onReceive(Object message) throws Exception {
			if (message instanceof String) {
				String[] s = ((String) message).split(" ");
				switch (s[0]) {
				case "AntonioQuiereModificar": {
					cuenta.cambio(Integer.parseInt(s[1]));
					System.out.println(Actor.this.getSender().path().name()+" modifico "+Integer.parseInt(s[1])+" en la cuenta");
					System.out.println("Valor cuenta = "+cuenta.getValor());
					break;
				}
				case "BlancaQuiereModificar": {
					cuenta.cambio(Integer.parseInt(s[1]));
					System.out.println(Actor.this.getSender().path().name()+" modifico "+Integer.parseInt(s[1])+" en la cuenta");
					System.out.println("Valor cuenta = "+cuenta.getValor());
					break;
				}
				default:
					System.out.println("No puede modificar.");
				}
			} else {
				unhandled(message);
			}
		}

	}
	
	static class Cuenta{

	    public int valor;
	    
		public Cuenta(int valor) {
			this.valor = valor;
		}
	    
	    public void cambio(int cambio){
	    	int temp = valor;
	    	temp = temp + cambio;
	        valor = temp;
	    }
	    
	    public int getValor(){
	        return valor;
	    }

	}

}



