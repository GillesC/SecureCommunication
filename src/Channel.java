/**
 * Klasse die het kanaal tussen 2 PC's nabootst deze kan worden afgeluisterd via
 * listen() methode, die het meest recente bericht teruggeeft
 */
public class Channel {
    private Message recentMessage = null;

    public void send(Message msg, Person fromPerson, Person toPerson) {
        recentMessage = msg;

        //TODO SEND!

    }

    public void listen() {
        System.out.println("\n"+recentMessage.toString()+"\n");
    }
}
