import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        //----------------------------------

		system.shutdown(); //espera que Antonio y Blanca acaben de comunicarse 
		system.awaitTermination();// JOIN //Termina la instancia de system
		
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
					System.out.println("Se modifico "+Integer.parseInt(s[1])+" en la cuenta");
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

/*
3
2
47
10
-5
-15
5
*/