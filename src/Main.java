import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Main {
	/**
	 * @mabonillagi tercer commit
	 */

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("Hola");
		ActorRef helloActor = system.actorOf(new Props(null, HelloActor.class,null), "hola"); //esta linea esta diferente
		helloActor.tell("hello", null);
		helloActor.tell("chao", null);
		system.shutdown();
		system.awaitTermination();// JOIN
	}
}

class HelloActor extends UntypedActor {
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String s = (String) message;
			switch (s) {
			case "hello": {
				System.out.println("hello you");
				break;
			}
			default:
				System.out.println("huu?");
			}
		} else {
			unhandled(message);
		}
	}
}
